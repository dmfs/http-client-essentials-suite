/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
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
import java.io.InputStream;


/**
 * Defines an interface of an HTTP response message body entity.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public interface HttpResponseEntity
{

    /**
     * Returns the {@link MediaType} of the entity.
     *
     * @return The content type.
     */
    public MediaType contentType() throws IOException;

    /**
     * Returns the length of the content or <code>-1</code> if the length is not known.
     *
     * @return The content length or <code>-1</code> .
     *
     * @throws IOException
     */
    public long contentLength() throws IOException;

    /**
     * Returns the content {@link InputStream} of the entity. If you don't consume the entire content, make sure you
     * always close the {@link InputStream}.
     *
     * @return An {@link InputStream}, never null.
     *
     * @throws IOException
     */
    public InputStream contentStream() throws IOException;

}
