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


/**
 * A simple {@link Token} based on a String.
 *
 * @author Marten Gajda
 */
public final class StringToken implements Token
{
    private final String mValue;


    public StringToken(String value)
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
        return new StringToken(mValue.substring(start, end));
    }


    @Override
    public int hashCode()
    {
        return super.hashCode();
    }


    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof StringToken && ((StringToken) obj).mValue.equalsIgnoreCase(mValue);
    }


    @Override
    public String toString()
    {
        return mValue;
    }
}
