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

package org.dmfs.httpessentials.types;

import org.dmfs.httpessentials.parameters.Parameter;
import org.dmfs.httpessentials.parameters.ParameterType;
import org.dmfs.httpessentials.parameters.Parametrized;
import org.dmfs.iterables.CsvIterable;
import org.dmfs.iterators.decorators.Filtered;
import org.dmfs.iterators.decorators.Mapped;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;


/**
 * Represents a set of key-value pairs parsed and serializable from/to a x-www-form-url-encoded {@link String}.
 * <p>
 *
 * @author Marten Gajda
 */
public final class UrlFormEncodedKeyValues implements Parametrized
{
    private final static String ENCODING = "UTF-8";

    private final static char PAIR_SEPARATOR = '&';
    private final static char VALUE_SEPARATOR = '=';

    private final String mFormEncodedString;
    private final Iterable<String> mParts;


    public UrlFormEncodedKeyValues(String formEncodedString)
    {
        mFormEncodedString = formEncodedString;
        mParts = new CsvIterable(formEncodedString, PAIR_SEPARATOR);
    }


    @Override
    public <T> Parameter<T> firstParameter(ParameterType<T> parameterType, T defaultValue)
    {
        Iterator<Parameter<T>> parameters = parameters(parameterType);
        return parameters.hasNext() ? parameters.next() : parameterType.entity(defaultValue);
    }


    @Override
    public <T> Iterator<Parameter<T>> parameters(final ParameterType<T> parameterType)
    {
        return new Mapped<>(
                new Filtered<>(
                        new Mapped<>(mParts.iterator(),
                                element -> new KeyValueStringEntry(element, element.indexOf(VALUE_SEPARATOR))),
                        element -> parameterType.name().equals(element.getKey())),
                element -> parameterType.entityFromString(element.getValue()));
    }


    @Override
    public <T> boolean hasParameter(final ParameterType<T> parameterType)
    {
        return parameters(parameterType).hasNext();
    }


    @Override
    public String toString()
    {
        return mFormEncodedString;
    }


    /**
     * An {@link Map.Entry} that's derived from a string pair separated by an equals sign like in {@code key=value}
     */
    private static class KeyValueStringEntry implements Map.Entry<String, String>
    {
        private final int mEqualsIdx;
        private final String mKeyValueString;


        public KeyValueStringEntry(String keyValueString, int equalsIdx)
        {
            mEqualsIdx = equalsIdx;
            mKeyValueString = keyValueString;
        }


        @Override
        public String getKey()
        {
            return decode(mEqualsIdx < 0 ? mKeyValueString : mKeyValueString.substring(0, mEqualsIdx));
        }


        @Override
        public String getValue()
        {
            return mEqualsIdx < 0 ? null : decode(mKeyValueString.substring(mEqualsIdx + 1));
        }


        @Override
        public String setValue(String s)
        {
            throw new UnsupportedOperationException("Setting the value of this Entry is not supported.");
        }


        private String decode(String string)
        {
            try
            {
                return URLDecoder.decode(string, ENCODING);
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(String.format("Runtime doesn't support %s encoding", ENCODING));
            }
        }
    }
}
