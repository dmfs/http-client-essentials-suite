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
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.elementary.Absent;
import org.dmfs.jems.optional.elementary.Present;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


/**
 * A {@link HttpResponseEntity} with predefined content.
 *
 * @author Marten Gajda
 */
public final class StaticMockResponseEntity implements HttpResponseEntity
{
    private final Optional<MediaType> mContentType;
    private final Optional<Long> mContentLength;
    private final byte[] mContent;


    public StaticMockResponseEntity()
    {
        this(new Absent<>(), new Absent<>(), new byte[0]);
    }


    /**
     * Creates a {@link HttpResponseEntity} that contains the given content. Note that the content will be encoded using the charset specified in the given
     * content-type or UTF-8 if no charset was specified. The content-length will be derived from the encoded data.
     *
     * @param contentType
     *         The {@link MediaType} of the content.
     * @param content
     *         A {@link String} representing the content.
     *
     * @throws UnsupportedEncodingException
     */
    public StaticMockResponseEntity(MediaType contentType, String content) throws UnsupportedEncodingException
    {
        this(contentType, content.getBytes(contentType.charset("UTF-8")));
    }


    /**
     * Creates a {@link HttpResponseEntity} that contains the given content. Note that the content will be encoded using the charset specified in the given
     * content-type or UTF-8 if no charset was specified.
     *
     * @param contentType
     *         The {@link MediaType} of the content.
     * @param contentLength
     *         The content-length to report to the client.
     * @param content
     *         A {@link String} representing the content.
     *
     * @throws UnsupportedEncodingException
     */
    public StaticMockResponseEntity(MediaType contentType, long contentLength, String content) throws UnsupportedEncodingException
    {
        this(contentType, contentLength, content.getBytes(contentType.charset("UTF-8")));
    }


    /**
     * Creates a {@link HttpResponseEntity} that contains the given content. The content-length will be derived from the given array.
     *
     * @param contentType
     *         The {@link MediaType} of the content.
     * @param content
     *         The content.
     */
    public StaticMockResponseEntity(MediaType contentType, byte[] content)
    {
        this(contentType, content.length, content);
    }


    /**
     * Creates a {@link HttpResponseEntity} that contains the given content.
     *
     * @param contentType
     *         The {@link MediaType} of the content.
     * @param contentLength
     *         The content-length to report to the client.
     * @param content
     *         The content.
     */
    public StaticMockResponseEntity(MediaType contentType, long contentLength, byte[] content)
    {
        this(new Present<>(contentType), new Present<>(contentLength), content);
    }


    public StaticMockResponseEntity(Optional<MediaType> contentType, Optional<Long> contentLength, byte[] content)
    {
        mContentType = contentType;
        mContentLength = contentLength;
        mContent = content;
    }


    @Override
    public Optional<MediaType> contentType()
    {
        return mContentType;
    }


    @Override
    public Optional<Long> contentLength()
    {
        return mContentLength;
    }


    @Override
    public InputStream contentStream()
    {
        return new ByteArrayInputStream(mContent);
    }

}
