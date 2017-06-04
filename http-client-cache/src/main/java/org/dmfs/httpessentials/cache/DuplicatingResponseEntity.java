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

import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.types.MediaType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * An {@link HttpResponseEntity} decorator that copies the payload content to a given {@link OutputStream} as it's read.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class DuplicatingResponseEntity implements HttpResponseEntity
{
    private final HttpResponseEntity mDelegate;
    private final InputStream mCopyStream;


    public DuplicatingResponseEntity(HttpResponseEntity delegate, OutputStream copyStream) throws IOException
    {
        mDelegate = delegate;
        mCopyStream = new DuplicatingInputStream(delegate.contentStream(), copyStream);
    }


    @Override
    public MediaType contentType() throws IOException
    {
        return mDelegate.contentType();
    }


    @Override
    public long contentLength() throws IOException
    {
        return mDelegate.contentLength();
    }


    @Override
    public InputStream contentStream() throws IOException
    {
        return mCopyStream;
    }
}
