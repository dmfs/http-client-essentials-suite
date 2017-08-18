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

package org.dmfs.httpessentials.client;

import org.dmfs.httpessentials.types.MediaType;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Defines an interface of an HTTP message request body entity.
 *
 * @author Marten Gajda
 */
public interface HttpRequestEntity
{

    /**
     * Returns the {@link MediaType} of the entity if known.
     *
     * @return The content type or <code>null</code> if no content type is applicable (like when the entity is empty).
     */
    MediaType contentType();

    /**
     * Returns the length of the content or <code>-1</code> if it's not known.
     *
     * @return The content length or <code>-1</code> .
     *
     * @throws IOException
     */
    long contentLength() throws IOException;

    /**
     * Writes the content to the given {@link OutputStream}. Note that the stream is property of the caller and must not be closed by this method.
     *
     * @param out
     *         The {@link OutputStream} to write to.
     *
     * @throws IOException
     */
    void writeContent(OutputStream out) throws IOException;
}
