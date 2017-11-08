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

package org.dmfs.httpessentials.executors.authorizing.utils;

import org.dmfs.iterators.AbstractBaseIterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * An {@link Iterable} which splits the Challenges in a www-authenticate header.
 * <p>
 * Note: this is supposed to be a temporary solution.
 */
public final class Challenges implements Iterable<CharSequence>
{
    private final static Pattern P = Pattern.compile("\\s*[\\w!#$%&'*+\\.\\-^_`|~]+($| )");
    private final Iterable<CharSequence> mDelegate;


    public Challenges(Iterable<CharSequence> strings)
    {
        mDelegate = strings;
    }


    @Override
    public Iterator<CharSequence> iterator()
    {
        final Iterator<CharSequence> delegate = mDelegate.iterator();
        return new AbstractBaseIterator<CharSequence>()
        {

            @Override
            public boolean hasNext()
            {
                return delegate.hasNext();
            }


            /**
             * "!" / "#" / "$" / "%" / "&" / "'" / "*" / "+" / "-" / "." / "^" / "_" / "`" / "|" / "~" / DIGIT / ALPHA ; any VCHAR, except delimiters
             */
            @Override
            public CharSequence next()
            {
                List<CharSequence> params = new LinkedList<>();
                while (delegate.hasNext())
                {
                    CharSequence n = delegate.next();
                    params.add(0, n);
                    if (P.matcher(n).lookingAt())
                    {
                        break;
                    }
                }
                StringBuilder sb = new StringBuilder(128);
                sb.append(params.remove(0));
                for (CharSequence param : params)
                {
                    sb.append(",");
                    sb.append(param);
                }
                return sb;
            }

        };
    }

}
