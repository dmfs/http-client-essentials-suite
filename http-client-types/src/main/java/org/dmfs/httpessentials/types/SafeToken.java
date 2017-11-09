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

import java.util.Arrays;


/**
 * A {@link Token} that uses the given {@link CharSequence} as the underlying value.
 * <p>
 * Characters in the input string that are not allowed for token according to <a href="https://tools.ietf.org/html/rfc7230#section-3.2.6">RFC7230</a> will be
 * replaced with underscore ('_')
 *
 * @author Marten Gajda
 */
public final class SafeToken extends AbstractBaseToken
{
    // TODO: use a bitmap instead
    private final static char[] SAFE_CHARS = "!#$%&'*+-.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ^_`abcdefghijklmnopqrstuvwxyz|~".toCharArray();


    public SafeToken(CharSequence delegate)
    {
        super(new CharToken(new SafeCharSequence(delegate)));
    }


    // TODO: this could be useful as a standalone top level class
    private final static class SafeCharSequence implements CharSequence
    {
        private final CharSequence mDelegate;


        private SafeCharSequence(CharSequence delegate)
        {
            if (delegate.length() == 0)
            {
                throw new IllegalArgumentException("Tokens must not be empty.");
            }
            mDelegate = delegate;
        }


        @Override
        public int length()
        {
            return mDelegate.length();
        }


        @Override
        public char charAt(int i)
        {
            return safe(mDelegate.charAt(i));
        }


        @Override
        public CharSequence subSequence(int i, int i1)
        {
            return new SafeCharSequence(mDelegate.subSequence(i, i1));
        }


        @Override
        public String toString()
        {
            // TODO be more optimistic, assume the delegate is valid and only build a string if it's not
            StringBuilder stringBuilder = new StringBuilder(length());

            for (int i = 0, len = mDelegate.length(); i < len; ++i)
            {
                stringBuilder.append(safe(charAt(i)));
            }

            return stringBuilder.toString();
        }


        private char safe(char c)
        {
            return Arrays.binarySearch(SAFE_CHARS, c) < 0 ? '_' : c;
        }
    }

}
