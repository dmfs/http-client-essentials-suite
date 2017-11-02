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

/**
 * A {@link CharSequence} decorators which adds quotes.
 * <p>
 * TODO: this should escape any double quote and backslash characters
 *
 * @author Marten Gajda
 */
public final class Quoted implements CharSequence
{
    private final CharSequence mDelegate;


    public Quoted(CharSequence delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public int length()
    {
        return mDelegate.length() + 2;
    }


    @Override
    public char charAt(int index)
    {
        if (index == 0 || index == mDelegate.length() + 1)
        {
            return '"';
        }
        return mDelegate.charAt(index - 1);
    }


    @Override
    public CharSequence subSequence(int start, int end)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    @Override
    public String toString()
    {
        return "\"" + mDelegate.toString() + "\"";
    }
}
