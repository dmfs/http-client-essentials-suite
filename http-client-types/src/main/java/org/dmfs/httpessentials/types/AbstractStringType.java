/*
 *  Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dmfs.httpessentials.types;

/**
 * An abstract base class for types that represent a {@link CharSequence} and use a wrapped {@link String} value for
 * it.
 *
 * @author Gabor Keszthelyi
 */
public abstract class AbstractStringType implements CharSequence
{
    private final String mValue;


    public AbstractStringType(String value)
    {
        if (value == null)
        {
            throw new IllegalArgumentException(String.format("Value of %s must not be null", getClass()));
        }
        mValue = value;
    }


    @Override
    public final int length()
    {
        return mValue.length();
    }


    @Override
    public final char charAt(int index)
    {
        return mValue.charAt(index);
    }


    @Override
    public final CharSequence subSequence(int start, int end)
    {
        return mValue.subSequence(start, end);
    }


    @Override
    public final String toString()
    {
        return mValue;
    }
}
