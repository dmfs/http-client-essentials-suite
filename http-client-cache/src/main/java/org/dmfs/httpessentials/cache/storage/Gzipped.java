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

package org.dmfs.httpessentials.cache.storage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


/**
 * {@link Storage} decorator that compresses and decompresses all streams using gzip compression.
 * <p>
 * Note: {@link #inputStream(String)} checks the stream for the gzip magic number and falls back to uncompressed input if the gzip magic number is not found.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class Gzipped implements Storage
{
    private final Storage mDelegate;


    public Gzipped(Storage delegate)
    {
        mDelegate = delegate;
    }


    @Override
    public boolean isEmpty(String name) throws IOException
    {
        return mDelegate.isEmpty(name);
    }


    @Override
    public InputStream inputStream(String name) throws IOException
    {
        BufferedInputStream buffered = new BufferedInputStream(mDelegate.inputStream(name));
        // peek at the first 2 bytes
        byte[] magic = new byte[2];
        buffered.mark(2);
        int read = buffered.read(magic);
        buffered.reset();
        // check for the gzip magic number
        if (read < 2 || magic[0] != 0x01f || magic[1] != (byte) 0x08b)
        {
            return buffered;
        }
        return new GZIPInputStream(buffered);
    }


    @Override
    public OutputStream outputStream(String name) throws IOException
    {
        return new GZIPOutputStream(mDelegate.outputStream(name));
    }


    @Override
    public void wipe(String name) throws IOException
    {
        mDelegate.wipe(name);
    }


    @Override
    public long size(String name) throws IOException
    {
        return mDelegate.size(name);
    }
}
