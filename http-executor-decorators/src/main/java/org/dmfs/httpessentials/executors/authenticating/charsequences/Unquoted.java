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
 * A {@link CharSequence} which removes any quotes around a given String.
 * <p>
 * TODO: this should also unescape any escape sequences.
 *
 * @author Marten Gajda
 */
public final class Unquoted implements CharSequence
{
    private final CharSequence mDelegate;


    public Unquoted(CharSequence delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public int length()
    {
        int deduct = isQuoted() ? 2 : 0;
        return mDelegate.length() - deduct;
    }


    @Override
    public char charAt(int index)
    {
        if (isQuoted())
        {
            // skip the quotes
            index++;
        }
        return mDelegate.charAt(index);
    }


    @Override
    public CharSequence subSequence(int start, int end)
    {
        if (isQuoted())
        {
            // skip the quotes
            return mDelegate.subSequence(start + 1, end + 1);
        }
        return mDelegate.subSequence(start, end);
    }


    @Override
    public String toString()
    {
        if (isQuoted())
        {
            // skip the quotes
            return mDelegate.toString().substring(1, mDelegate.length() - 1);
        }
        return mDelegate.toString();
    }


    private boolean isQuoted()
    {
        return mDelegate.charAt(0) == '"' && mDelegate.charAt(mDelegate.length() - 1) == '"';
    }
}
