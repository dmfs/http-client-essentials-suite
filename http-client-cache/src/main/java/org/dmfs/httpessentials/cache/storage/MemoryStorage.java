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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * A {@link Storage} implementation that stores objects in memory.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class MemoryStorage implements Storage
{
    private final Map<String, byte[]> mStorage = new HashMap<String, byte[]>(32);


    /**
     * Creates a {@link MemoryStorage}.
     */
    public MemoryStorage()
    {
    }


    @Override
    public boolean isEmpty(final String name)
    {
        synchronized (mStorage)
        {
            byte[] content = mStorage.get(name);
            return content == null || content.length == 0;
        }
    }


    @Override
    public InputStream inputStream(final String name)
    {
        synchronized (mStorage)
        {
            byte[] content = mStorage.get(name);
            if (content == null || content.length == 0)
            {
                return EmptyInputStream.INSTANCE;
            }
            return new ByteArrayInputStream(content);
        }
    }


    @Override
    public OutputStream outputStream(final String name)
    {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        return new FilterOutputStream(outputStream)
        {
            @Override
            public void close() throws IOException
            {
                super.flush();
                // the stream has been closed, store the data
                byte[] data = outputStream.toByteArray();
                synchronized (mStorage)
                {
                    if (data.length == 0)
                    {
                        mStorage.remove(name);
                    }
                    else
                    {
                        mStorage.put(name, data);
                    }
                }
            }
        };
    }


    @Override
    public void wipe(final String name)
    {
        synchronized (mStorage)
        {
            mStorage.remove(name);
        }
    }


    @Override
    public long size(String name) throws IOException
    {
        synchronized (mStorage)
        {
            byte[] content = mStorage.get(name);
            return content == null ? 0 : content.length;
        }
    }
}
