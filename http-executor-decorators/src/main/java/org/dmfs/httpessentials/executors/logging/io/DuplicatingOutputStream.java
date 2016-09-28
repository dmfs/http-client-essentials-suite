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
import java.io.OutputStream;


/**
 * @author Gabor Keszthelyi
 */
public final class DuplicatingOutputStream extends OutputStream
{
    private final OutputStream out1;
    private final OutputStream out2;


    public DuplicatingOutputStream(OutputStream out1, OutputStream out2)
    {
        this.out1 = out1;
        this.out2 = out2;
    }


    @Override
    public void write(int b) throws IOException
    {
        out1.write(b);
        out2.write(b);
    }


    @Override
    public void close() throws IOException
    {
        try
        {
            out1.close();
        }
        finally
        {
            out2.close();
        }
    }
}
