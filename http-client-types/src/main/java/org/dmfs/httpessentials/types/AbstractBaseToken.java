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

/**
 * An abstract {@link Token} which serves as the base class for all other {@link Token}s. It takes care of enforcing the {@link #equals(Object)} and {@link
 * #hashCode()} contracts.
 *
 * @author Marten Gajda
 */
public abstract class AbstractBaseToken implements Token
{
    private final CharSequence mDelegate;


    public AbstractBaseToken(CharSequence delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public final int length()
    {
        return mDelegate.length();
    }


    @Override
    public final char charAt(int index)
    {
        return mDelegate.charAt(index);
    }


    @Override
    public final CharSequence subSequence(int start, int end)
    {
        return mDelegate.subSequence(start, end);
    }


    @Override
    public final String toString()
    {
        return mDelegate.toString();
    }


    @Override
    public final int hashCode()
    {
        int result = 0;
        for (int i = 0, len = mDelegate.length(); i < len; ++i)
        {
            result = result * 31 + (mDelegate.charAt(i) | 0x20);
        }
        return result;
    }


    @Override
    public final boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof Token))
        {
            return false;
        }
        Token other = (Token) obj;
        CharSequence delegate = mDelegate;
        int start = 0;
        int len = delegate.length();

        if (len != other.length())
        {
            return false;
        }

        int pos = 0;
        while (pos < len)
        {
            char ours = delegate.charAt(pos + start);
            char theirs = other.charAt(pos);
            // if this condition is not satisfied, the tokens can't be equal
            if ((ours | 0x20) != (theirs | 0x20))
            {
                return false;
            }
            if (ours < '@' && ours != theirs)
            {
                return false;
            }
            if ((ours == 0x5E || ours == 0x7E) && ours != theirs)
            {
                return false;
            }
            ++pos;
        }
        return true;
    }
}
