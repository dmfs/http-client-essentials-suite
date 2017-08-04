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

package org.dmfs.httpessentials.okhttp.utils;

import okio.BufferedSink;

import java.io.IOException;
import java.io.OutputStream;


/**
 * An {@link OutputStream} which writes to a {@link BufferedSink}.
 *
 * @author Marten Gajda
 */
public final class BufferedSinkOutputStream extends OutputStream
{
    private final BufferedSink mBufferedSink;


    public BufferedSinkOutputStream(BufferedSink bufferedSink)
    {
        mBufferedSink = bufferedSink;
    }


    @Override
    public void write(byte[] buffer) throws IOException
    {
        mBufferedSink.write(buffer);
    }


    @Override
    public void write(byte[] buffer, int offset, int count) throws IOException
    {
        mBufferedSink.write(buffer, offset, count);
    }


    @Override
    public void write(int oneByte) throws IOException
    {
        mBufferedSink.writeByte(oneByte);
    }


    @Override
    public void close() throws IOException
    {
        mBufferedSink.close();
    }
}
