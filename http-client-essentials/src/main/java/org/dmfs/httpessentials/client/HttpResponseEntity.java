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
import org.dmfs.jems.optional.Optional;

import java.io.IOException;
import java.io.InputStream;


/**
 * Defines an interface of an HTTP response message body entity.
 *
 * @author Marten Gajda
 */
public interface HttpResponseEntity
{

    /**
     * Returns the {@link MediaType} of the entity, if known.
     *
     * @return The {@link Optional} content {@link MediaType}.
     */
    Optional<MediaType> contentType();

    /**
     * Returns the length of the content, if known.
     *
     * @return The {@link Optional} content length.
     */
    Optional<Long> contentLength();

    /**
     * Returns the content {@link InputStream} of the entity. If you don't consume the entire content, make sure you always close the {@link InputStream}.
     *
     * @return An {@link InputStream}.
     *
     * @throws IOException
     *         If an IO error occurred.
     */
    InputStream contentStream() throws IOException;

}
