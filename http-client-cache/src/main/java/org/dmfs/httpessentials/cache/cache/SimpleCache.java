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

package org.dmfs.httpessentials.cache.cache;

import org.dmfs.httpessentials.cache.storage.Storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * A simple {@link Cache}. This cache never removes any elements, so use it with care.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class SimpleCache implements Cache
{
    private final Storage mStorage;


    public SimpleCache(Storage storage)
    {
        this.mStorage = storage;
    }


    @Override
    public CacheEntity cacheEntity(final CacheKey key)
    {
        return new CacheEntity()
        {
            @Override
            public InputStream inputStream() throws IOException
            {
                return mStorage.inputStream(key.toString());
            }


            @Override
            public OutputStream outputStream() throws IOException
            {
                return mStorage.outputStream(key.toString());
            }


            @Override
            public void wipe() throws IOException
            {
                mStorage.wipe(key.toString());
            }


            @Override
            public long size() throws IOException
            {
                return mStorage.size(key.toString());
            }


            @Override
            public boolean isEmpty() throws IOException
            {
                return mStorage.isEmpty(key.toString());
            }
        };
    }
}
