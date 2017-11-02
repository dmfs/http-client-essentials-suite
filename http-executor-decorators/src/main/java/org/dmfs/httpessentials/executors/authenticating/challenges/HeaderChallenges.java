/*
 * Copyright 2017 dmfs GmbH
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dmfs.httpessentials.executors.authenticating.challenges;

import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.executors.authenticating.utils.Challenges;
import org.dmfs.httpessentials.headers.Header;
import org.dmfs.httpessentials.headers.HeaderType;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.ListHeaderType;
import org.dmfs.iterables.UnquotedSplit;
import org.dmfs.iterables.decorators.DelegatingIterable;
import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterables.decorators.Reverse;
import org.dmfs.iterators.Function;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Marten Gajda
 */
public final class HeaderChallenges extends DelegatingIterable<Challenge>
{
    /**
     * The {@link HeaderType} of the WWW-Authenticate header.
     * <p>
     * We have to implement a custom type because the definition if rather broken. A www-authenticate header has a (comma separated) list of challenges where
     * each challenge may contain a (comma separated) list of parameters. This makes it hard to parse. The only hint for when to split the list of challenges is
     * the space after the authentication scheme token.
     */
    public final static ListHeaderType<Challenge> WWW_AUTHENTICATE = new ListHeaderType<Challenge>()
    {
        @Override
        public Header<List<Challenge>> merged(Header<List<Challenge>> value1, Header<List<Challenge>> value2)
        {
            List<Challenge> list1 = value1.value();
            List<Challenge> list2 = value2.value();
            if (list1.size() == 0)
            {
                return value2;
            }
            if (list2.size() == 0)
            {
                return value1;
            }

            List<Challenge> merged = new ArrayList<>(list1.size() + list2.size());
            merged.addAll(list1);
            merged.addAll(list2);
            return entity(merged);
        }


        @Override
        public String name()
        {
            return "WWW-Authenticate";
        }


        @Override
        public Header<List<Challenge>> entityFromString(final String headerValueString)
        {
            return new Header<List<Challenge>>()
            {
                @Override
                public HeaderType<List<Challenge>> type()
                {
                    return WWW_AUTHENTICATE;
                }


                @Override
                public List<Challenge> value()
                {
                    return valueFromString(headerValueString);
                }
            };
        }


        @Override
        public Header<List<Challenge>> entity(final List<Challenge> value)
        {
            return new Header<List<Challenge>>()
            {
                @Override
                public HeaderType<List<Challenge>> type()
                {
                    return WWW_AUTHENTICATE;
                }


                @Override
                public List<Challenge> value()
                {
                    return value;
                }
            };
        }


        @Override
        public String valueString(List<Challenge> headerValue)
        {
            throw new UnsupportedOperationException();
        }


        @Override
        public List<Challenge> valueFromString(String valueString)
        {
            List<Challenge> challenges = new ArrayList<>();
            for (Challenge challenge : new Mapped<>(new Challenges(new Reverse<>(new UnquotedSplit(valueString, ','))),
                    new Function<CharSequence, Challenge>()
                    {
                        @Override
                        public Challenge apply(CharSequence argument)
                        {
                            return new SimpleChallenge(argument);
                        }
                    }))
            {
                challenges.add(challenge);
            }
            return challenges;
        }


        @Override
        public int hashCode()
        {
            return name().hashCode();
        }


        @Override
        public boolean equals(Object obj)
        {
            return this == obj || (obj instanceof ListHeaderType && name().equals(((HeaderType<?>) obj).name()));
        }

    };


    public HeaderChallenges(Headers headers)
    {
        super(headers.header(WWW_AUTHENTICATE).value());
    }
}
