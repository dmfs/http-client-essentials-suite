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

package org.dmfs.httpessentials.executors.authenticating.charsequences;

import org.dmfs.httpessentials.types.Token;

import java.util.Locale;


/**
 * A simple (very inefficient) {@link Token}.
 *
 * @author Marten Gajda
 */
public final class CharSequenceToken implements Token
{
    private final CharSequence mValue;


    public CharSequenceToken(CharSequence value)
    {
        mValue = value;
    }


    @Override
    public int length()
    {
        return mValue.length();
    }


    @Override
    public char charAt(int index)
    {
        return mValue.charAt(index);
    }


    @Override
    public CharSequence subSequence(int start, int end)
    {
        return new CharSequenceToken(mValue.subSequence(start, end));
    }


    @Override
    public int hashCode()
    {
        return mValue.toString().toLowerCase(Locale.ENGLISH).hashCode();
    }


    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof CharSequenceToken && ((CharSequenceToken) obj).mValue.toString().equalsIgnoreCase(mValue.toString());
    }


    @Override
    public String toString()
    {
        return mValue.toString();
    }
}
