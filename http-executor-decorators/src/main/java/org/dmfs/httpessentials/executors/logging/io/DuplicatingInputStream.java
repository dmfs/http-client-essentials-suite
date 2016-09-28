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

package org.dmfs.httpessentials.executors.logging.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * An {@link InputStream} that writes every input byte to a given output stream.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class DuplicatingInputStream extends InputStream
{
    private final InputStream mIn;
    private final OutputStream mOut;


    public DuplicatingInputStream(InputStream in, OutputStream out)
    {
        mIn = in;
        mOut = out;
    }


    @Override
    public int read() throws IOException
    {
        int r = mIn.read();
        if (r >= 0)
        {
            mOut.write(r);
        }
        return r;
    }


    @Override
    public void close() throws IOException
    {
        try
        {
            mIn.close();
        }
        finally
        {
            mOut.close();
        }
    }

}