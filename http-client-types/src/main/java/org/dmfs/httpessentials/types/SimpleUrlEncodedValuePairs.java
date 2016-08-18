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
import org.dmfs.iterables.CsvIterable;
import org.dmfs.iterators.AbstractConvertedIterator;
import org.dmfs.iterators.AbstractFilteredIterator;
import org.dmfs.iterators.ConvertedIterator;
import org.dmfs.iterators.FilteredIterator;
import org.dmfs.iterators.filters.Skip;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;


/**
 * Created by marten on 10.06.16.
 */
public final class SimpleUrlEncodedValuePairs implements UrlEncodedValuePairs
{
    private final static char PAIR_SEPARATOR = '&';
    private final static char VALUE_SEPARATOR = '=';

    private final String mQueryString;
    private final Iterable<String> mParts;


    public SimpleUrlEncodedValuePairs(String queryString)
    {
        mQueryString = queryString;
        mParts = new CsvIterable(queryString, PAIR_SEPARATOR);
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
                new FilteredIterator<String>(new FilteredIterator<String>(mParts.iterator(), new Skip<String>(1)),
                        new AbstractFilteredIterator.IteratorFilter<String>()
                        {
                            @Override
                            public boolean iterate(String element)
                            {
                                String param = element.trim();
                                int equalsIdx = param.indexOf(VALUE_SEPARATOR);
                                try
                                {
                                    return parameterType.name()
                                            .equalsIgnoreCase(
                                                    URLDecoder.decode(param.substring(0, equalsIdx), "UTF-8"));
                                }
                                catch (UnsupportedEncodingException e)
                                {
                                    throw new RuntimeException("Runtime doesn't support UTF-8 encoding");
                                }
                            }
                        }), new AbstractConvertedIterator.Converter<Parameter<T>, String>()
        {
            @Override
            public Parameter<T> convert(String element)
            {
                int equalsIdx = element.indexOf(VALUE_SEPARATOR);
                try
                {
                    return parameterType.entityFromString(URLDecoder.decode(element.substring(equalsIdx + 1), "UTF-8"));
                }
                catch (UnsupportedEncodingException e)
                {
                    throw new RuntimeException("Runtime doesn't support UTF-8 encoding");
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
        return mQueryString;
    }
}
