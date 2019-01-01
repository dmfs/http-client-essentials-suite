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
import org.dmfs.httpessentials.parameters.Parameters;
import org.dmfs.iterators.decorators.Filtered;
import org.dmfs.iterators.decorators.Mapped;
import org.dmfs.iterators.elementary.Seq;

import java.util.Iterator;
import java.util.Locale;


/**
 * A simple media type that's composed from maintype, subtype, an optional charset and other optional parameters.
 *
 * @author Marten Gajda
 */
public final class StructuredMediaType implements MediaType
{
    private final String mMainType;
    private final String mSubType;
    private final Parameter<?>[] mParameters;


    /**
     * Creates a {@link MediaType} for the given maintype and subtype with no particular charset.
     *
     * @param maintype
     *         The maintype of the media.
     * @param subtype
     *         The subtype of the media.
     */
    public StructuredMediaType(String maintype, String subtype)
    {
        this(maintype, subtype, false);
    }


    /**
     * Creates a {@link MediaType} for the given maintype and subtype with the given charset.
     *
     * @param maintype
     *         The maintype of the media.
     * @param subtype
     *         The subtype of the media.
     * @param charset
     *         The character set of the media.
     */
    public StructuredMediaType(String maintype, String subtype, String charset)
    {
        this(maintype, subtype, false, Parameters.CHARSET.entity(charset));
    }


    /**
     * Creates a {@link MediaType} for the given maintype and subtype with the given parameters.
     *
     * @param maintype
     *         The maintype of the media.
     * @param subtype
     *         The subtype of the media.
     * @param parameters
     *         A list of {@link Parameter}.
     */
    public StructuredMediaType(String maintype, String subtype, Parameter<?>... parameters)
    {
        this(maintype, subtype, true, parameters);
    }


    private StructuredMediaType(String maintype, String subtype, boolean cloneParameters, Parameter<?>... parameters)
    {
        // MediaTypes are case insensitive, so we convert them to lower case for easier comparison
        mMainType = maintype.toLowerCase(Locale.ENGLISH);
        mSubType = subtype.toLowerCase(Locale.ENGLISH);
        mParameters = cloneParameters ? parameters.clone() : parameters;
    }


    @Override
    public String type()
    {
        return mMainType + "/" + mSubType;
    }


    @Override
    public String mainType()
    {
        return mMainType;
    }


    @Override
    public String subType()
    {
        return mSubType;
    }


    @Override
    public String charset(String defaultCharset)
    {
        return firstParameter(Parameters.CHARSET, defaultCharset).value();
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> Parameter<T> firstParameter(ParameterType<T> parameterType, T defaultValue)
    {
        for (Parameter<?> param : mParameters)
        {
            if (parameterType.equals(param.type()))
            {
                return (Parameter<T>) param;
            }
        }
        return parameterType.entity(defaultValue);
    }


    @Override
    public <T> Iterator<Parameter<T>> parameters(final ParameterType<T> parameterType)
    {
        return new Mapped<>(
                new Filtered<>(new Seq<>(mParameters),
                        element -> parameterType.equals(element.type())),
                element -> (Parameter<T>) element);
    }


    @Override
    public <T> boolean hasParameter(ParameterType<T> parameterType)
    {
        for (Parameter<?> param : mParameters)
        {
            if (parameterType.equals(param.type()))
            {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder(mMainType);
        result.append('/');
        result.append(mSubType);
        for (Parameter<?> param : mParameters)
        {
            result.append(';');
            result.append(param.type().name());
            result.append('=');
            result.append(param.toString());
        }
        return result.toString();
    }


    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof MediaType))
        {
            return false;
        }
        MediaType other = (MediaType) obj;

        return mMainType.equalsIgnoreCase(other.mainType()) && mSubType.equalsIgnoreCase(other.subType());
    }


    @Override
    public int hashCode()
    {
        return mMainType.hashCode() * 31 + mSubType.hashCode();
    }
}
