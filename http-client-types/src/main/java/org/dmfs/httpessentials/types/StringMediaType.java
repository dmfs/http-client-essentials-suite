/*
 * Copyright 2016 dmfs GmbH
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
import org.dmfs.httpessentials.parameters.Parameters;
import org.dmfs.iterables.CsvIterable;
import org.dmfs.iterators.AbstractConvertedIterator.Converter;
import org.dmfs.iterators.AbstractFilteredIterator.IteratorFilter;
import org.dmfs.iterators.ConvertedIterator;
import org.dmfs.iterators.FilteredIterator;
import org.dmfs.iterators.filters.Skip;

import java.util.Iterator;
import java.util.Locale;


/**
 * An implementation of a {@link MediaType} that parses a media type string.
 *
 * @author Marten Gajda
 */
// TODO: refactor to get rid of the parameter handling code duplication
public final class StringMediaType implements MediaType
{
    private final static char PARAMETER_SEPARATOR = ';';
    private final static char PARAMETER_VALUE_SEPARATOR = '=';

    private final String mMediaType;
    private final Iterable<String> mParts;


    /**
     * Create a new {@link MediaType} from a content type string.
     *
     * @param mediaType
     *         The content type string to parse.
     */
    public StringMediaType(String mediaType)
    {
        mMediaType = mediaType;
        mParts = new CsvIterable(mMediaType, PARAMETER_SEPARATOR);
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
                        new IteratorFilter<String>()
                        {
                            @Override
                            public boolean iterate(String element)
                            {
                                String param = element.trim();
                                int equalsIdx = param.indexOf(PARAMETER_VALUE_SEPARATOR);
                                return parameterType.name().equalsIgnoreCase(param.substring(0, equalsIdx));
                            }
                        }), new Converter<Parameter<T>, String>()
        {
            @Override
            public Parameter<T> convert(String element)
            {
                int equalsIdx = element.indexOf(PARAMETER_VALUE_SEPARATOR);
                return parameterType.entityFromString(element.substring(equalsIdx + 1));
            }
        });
    }


    @Override
    public <T> boolean hasParameter(ParameterType<T> parameterType)
    {
        return parameters(parameterType).hasNext();
    }


    @Override
    public String type()
    {
        return mParts.iterator().next().trim();
    }


    @Override
    public String mainType()
    {
        String type = type();
        return type.substring(0, type.indexOf('/'));
    }


    @Override
    public String subType()
    {
        String type = type();
        int slash = type.indexOf('/');
        return mMediaType.substring(slash + 1, type.length());
    }


    @Override
    public String charset(String defaultCharset)
    {
        return firstParameter(Parameters.CHARSET, defaultCharset).value();
    }


    @Override
    public String toString()
    {
        return mMediaType;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof MediaType))
        {
            return false;
        }
        MediaType other = (MediaType) obj;

        return type().equalsIgnoreCase(other.type());
    }


    @Override
    public int hashCode()
    {
        return mainType().toLowerCase(Locale.ENGLISH).hashCode() * 31 + subType().toLowerCase(Locale.ENGLISH)
                .hashCode();
    }

}
