/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
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
import org.dmfs.iterators.AbstractConvertedIterator;
import org.dmfs.iterators.AbstractFilteredIterator;
import org.dmfs.iterators.ConvertedIterator;
import org.dmfs.iterators.FilteredIterator;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;


/**
 * Represents a set of key-value pairs parsed and serializable from/to a x-www-form-url-encoded {@link String}.
 * <p>
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class UrlFormEncoded implements Parametrized
{
    private final static String ENCODING = "UTF-8";

    private final static char PAIR_SEPARATOR = '&';
    private final static char VALUE_SEPARATOR = '=';

    private final String mFormEncodedString;
    private final Iterable<String> mParts;


    public UrlFormEncoded(String formEncodedString)
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
        return new ConvertedIterator<Parameter<T>, String>(
                new FilteredIterator<String>(mParts.iterator(),
                        new AbstractFilteredIterator.IteratorFilter<String>()
                        {
                            @Override
                            public boolean iterate(String element)
                            {
                                if (element.isEmpty())
                                {
                                    return false;
                                }

                                int equalsIdx = element.indexOf(VALUE_SEPARATOR);

                                try
                                {
                                    if (equalsIdx < 0)
                                    {
                                        return parameterType.name().equals(URLDecoder.decode(element, ENCODING));
                                    }
                                    return parameterType.name().equals(
                                            URLDecoder.decode(element.substring(0, equalsIdx), ENCODING));
                                }
                                catch (UnsupportedEncodingException e)
                                {
                                    throw new RuntimeException("Runtime doesn't support UTF-8 encoding");
                                }
                            }
                        }),
                new AbstractConvertedIterator.Converter<Parameter<T>, String>()
                {
                    @Override
                    public Parameter<T> convert(String element)
                    {
                        int equalsIdx = element.indexOf(VALUE_SEPARATOR);
                        if (equalsIdx < 0)
                        {
                            return null;
                        }
                        try
                        {
                            return parameterType.entityFromString(
                                    URLDecoder.decode(element.substring(equalsIdx + 1), ENCODING));
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            throw new RuntimeException(String.format("Runtime doesn't support %s encoding", ENCODING));
                        }
                    }
                });
    }


    @Override
    public <T> boolean hasParameter(ParameterType<T> parameterType)
    {
        return parameters(parameterType).hasNext();
    }


    @Override
    public String toString()
    {
        return mFormEncodedString;
    }
}
