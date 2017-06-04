/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
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

package org.dmfs.httpessentials.cache;

import java.io.IOException;
import java.io.InputStream;


/**
 * A Sub-InputStream of another {@link InputStream}. It ends when there are two consecutive newline characters in the original stream.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class PartialInputStream extends InputStream
{
    private final InputStream mDelegate;
    private boolean mHadLf;
    private boolean mTerminated;


    public PartialInputStream(InputStream delegate)
    {
        this.mDelegate = delegate;
    }


    @Override
    public int read() throws IOException
    {
        if (mTerminated)
        {
            return -1;
        }

        int next = mDelegate.read();
        if (next < 0)
        {
            mTerminated = true;
            return -1;
        }

        if (next == 0x0a)
        {
            if (mHadLf)
            {
                mTerminated = true;
                return -1;
            }
            mHadLf = true;
        }
        else
        {
            mHadLf = false;
        }
        return next;
    }
}
