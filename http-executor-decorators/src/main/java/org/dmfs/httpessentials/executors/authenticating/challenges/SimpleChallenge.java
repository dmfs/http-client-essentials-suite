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
import org.dmfs.httpessentials.executors.authenticating.charsequences.StringToken;
import org.dmfs.httpessentials.executors.authenticating.charsequences.Unquoted;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.iterables.UnquotedSplit;
import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterators.Filter;
import org.dmfs.iterators.Function;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.pair.elementary.ValuePair;
import org.dmfs.optional.First;
import org.dmfs.optional.Optional;

import java.util.Iterator;

import static org.dmfs.optional.Absent.absent;


/**
 * A {@link Challenge} derived from a header value {@link CharSequence}.
 * <p>
 * TODO: improve this and avoid splitting and iterating over the {@link CharSequence} over and over again.
 *
 * @author Marten Gajda
 */
public final class SimpleChallenge implements Challenge
{
    private final CharSequence mChallengeText;


    public SimpleChallenge(CharSequence challengeText)
    {
        mChallengeText = challengeText;
    }


    @Override
    public Token scheme()
    {
        return new StringToken(mChallengeText.subSequence(0, space()).toString());
    }


    @Override
    public Optional<CharSequence> parameter(final Token token)
    {
        int i = space();
        if (i > 0)
        {
            return new org.dmfs.optional.decorators.Mapped<>(
                    new Function<Pair<CharSequence, CharSequence>, CharSequence>()
                    {
                        @Override
                        public CharSequence apply(Pair<CharSequence, CharSequence> charSequenceCharSequencePair)
                        {
                            return new Unquoted(charSequenceCharSequencePair.right().toString().trim());
                        }
                    },
                    new First<>(new Mapped<>(
                            new UnquotedSplit(mChallengeText.subSequence(space() + 1, mChallengeText.length()), ','),
                            new Function<CharSequence, Pair<CharSequence, CharSequence>>()
                            {
                                @Override
                                public Pair<CharSequence, CharSequence> apply(CharSequence o)
                                {
                                    Iterable<CharSequence> result = new UnquotedSplit(o, '=');
                                    Iterator<CharSequence> it = result.iterator();
                                    return new ValuePair<>(it.next(), it.next());
                                }
                            }), new Filter<Pair<CharSequence, CharSequence>>()
                    {
                        @Override
                        public boolean iterate(Pair<CharSequence, CharSequence> charSequenceCharSequencePair)
                        {
                            return charSequenceCharSequencePair.left().toString().trim().equalsIgnoreCase(token.toString());
                        }
                    }));
        }
        return absent();
    }


    private int space()
    {
        for (int i = 0, count = mChallengeText.length(); i < count; ++i)
        {
            if (mChallengeText.charAt(i) == ' ')
            {
                return i;
            }
        }
        return -1;
    }
}
