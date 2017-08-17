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

package org.dmfs.httpessentials.mockutils.entities;

import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.types.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * A {@link HttpResponseEntity} that reads its content from a {@link File}.
 *
 * @author Marten Gajda
 */
public final class FileMockResponseEntity implements HttpResponseEntity
{
    private final MediaType mContentType;
    private final long mContentLenth;
    private final File mContent;


    /**
     * Creates a {@link HttpResponseEntity} that reads the response data from the given file.
     *
     * @param contentType
     *         The {@link MediaType} of the file.
     * @param content
     *         The {@link File} that contains the data.
     */
    public FileMockResponseEntity(MediaType contentType, File content)
    {
        this(contentType, content.length(), content);
    }


    /**
     * Creates a {@link HttpResponseEntity} that reads the response data from the given file.
     *
     * @param contentType
     *         The {@link MediaType} of the file.
     * @param contentLength
     *         The content-length to report to the client or -1 to not report any content-length.
     * @param content
     *         The {@link File} that contains the data.
     */
    public FileMockResponseEntity(MediaType contentType, long contentLength, File content)
    {
        mContentType = contentType;
        mContentLenth = contentLength;
        mContent = content;
    }


    @Override
    public MediaType contentType()
    {
        return mContentType;
    }


    @Override
    public long contentLength() throws IOException
    {
        return mContentLenth;
    }


    @Override
    public InputStream contentStream() throws IOException
    {
        return new FileInputStream(mContent);
    }

}
