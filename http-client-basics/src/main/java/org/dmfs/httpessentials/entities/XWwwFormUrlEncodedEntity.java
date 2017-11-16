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

package org.dmfs.httpessentials.entities;

import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.types.StructuredMediaType;
import org.dmfs.iterables.decorators.Mapped;
import org.dmfs.iterators.Function;
import org.dmfs.jems.pair.Pair;
import org.dmfs.jems.single.Single;
import org.dmfs.rfc3986.encoding.XWwwFormUrlEncoded;
import org.dmfs.rfc3986.parameters.Parameter;
import org.dmfs.rfc3986.parameters.ParameterList;
import org.dmfs.rfc3986.parameters.parametersets.BasicParameterList;


/**
 * An {@link HttpRequestEntity} that encodes key-value pairs using <code>application/x-www-form-urlencoded</code> encoding.
 *
 * @author Marten Gajda
 */
public final class XWwwFormUrlEncodedEntity extends DelegatingRequestEntity
{

    public XWwwFormUrlEncodedEntity(Iterable<Pair<CharSequence, CharSequence>> values)
    {
        this(new BasicParameterList(
                new Mapped<>(
                        values,
                        new Function<Pair<CharSequence, CharSequence>, Parameter>()
                        {
                            @Override
                            public Parameter apply(final Pair<CharSequence, CharSequence> argument)
                            {
                                return new PairParameter(argument);
                            }
                        })));
    }


    // TODO: deprecate ParameterList and just use Iterable<Pair<CharSequence, CharSequence>>
    public XWwwFormUrlEncodedEntity(final ParameterList values)
    {
        super(new TextRequestEntity(new StructuredMediaType("application", "x-www-form-urlencoded"),
                new Single<CharSequence>()
                {
                    @Override
                    public CharSequence value()
                    {
                        return new XWwwFormUrlEncoded(values);
                    }
                }));
    }


    private static class PairParameter implements Parameter
    {
        private final Pair<CharSequence, CharSequence> mKeyValuePair;


        public PairParameter(Pair<CharSequence, CharSequence> keyValuePair)
        {
            mKeyValuePair = keyValuePair;
        }


        @Override
        public CharSequence name()
        {
            return mKeyValuePair.left();
        }


        @Override
        public CharSequence textValue()
        {
            return mKeyValuePair.right();
        }
    }
}
