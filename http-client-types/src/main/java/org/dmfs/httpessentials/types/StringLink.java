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
import org.dmfs.iterables.CachingIterable;
import org.dmfs.iterables.CsvIterable;
import org.dmfs.iterators.AbstractConvertedIterator.Converter;
import org.dmfs.iterators.AbstractFilteredIterator.IteratorFilter;
import org.dmfs.iterators.ConvertedIterator;
import org.dmfs.iterators.CsvIterator;
import org.dmfs.iterators.FilteredIterator;
import org.dmfs.iterators.filters.Skip;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;


/**
 * An implementation of a {@link Link} that parses a link string.
 *
 * @author Marten Gajda
 */
// TODO: refactor to get rid of the parameter handling code duplication (see StringMediaType)
public final class StringLink implements Link
{
    private final static char PARAMETER_SEPARATOR = ';';
    private final static char PARAMETER_VALUE_SEPARATOR = '=';

    private final String mLink;
    private final Iterable<String> mParts;


    /**
     * Create a new {@link Link} from a link string.
     *
     * @param link
     *         The link string to parse.
     */
    public StringLink(String link)
    {
        mLink = link;
        mParts = mLink.length() > 200 ? new CachingIterable<String>(
                new CsvIterator(mLink, PARAMETER_SEPARATOR)) : new CsvIterable(mLink, PARAMETER_SEPARATOR);
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
        return new ConvertedIterator<>(
                new FilteredIterator<>(new FilteredIterator<>(mParts.iterator(), new Skip<String>(1)),
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
                element = element.trim();
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
    public String toString()
    {
        return mLink;
    }


    @Override
    public URI target()
    {
        String target = mParts.iterator().next().trim();
        if (target.charAt(0) != '<' || target.charAt(target.length() - 1) != '>')
        {
            throw new IllegalArgumentException(String.format("%s is not properly enclosed by '<' and '>'", target));
        }
        return URI.create(target.substring(1, target.length() - 1));
    }


    @Override
    public URI context(URI defaultContext)
    {
        return defaultContext.resolve(firstParameter(Parameters.ANCHOR, defaultContext).value());
    }


    @Override
    public Set<Locale> languages()
    {
        Set<Locale> result = new HashSet<>(16);
        Iterator<Parameter<Locale>> locales = parameters(Parameters.HREFLANG);
        while (locales.hasNext())
        {
            result.add(locales.next().value());
        }
        return result;
    }


    @Override
    public String title()
    {
        return firstParameter(Parameters.TITLE, "").value();
    }


    @Override
    public MediaType mediaType()
    {
        return firstParameter(Parameters.TYPE, null).value();
    }


    @Override
    public Set<String> relationTypes()
    {
        return new HashSet<>(firstParameter(Parameters.REL, Collections.<String>emptyList()).value());
    }


    @Override
    public Set<String> reverseRelationTypes()
    {
        return new HashSet<>(firstParameter(Parameters.REV, Collections.<String>emptyList()).value());
    }

}
