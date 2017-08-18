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

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.headers.Headers;

import java.net.URI;


/**
 * An interface of an HTTP response object.
 *
 * @author Marten Gajda
 */
public interface HttpResponse
{
    /**
     * Returns the status of the response.
     *
     * @return The status.
     *
     * @see HttpStatus
     */
    HttpStatus status();

    /**
     * Returns the response headers.
     *
     * @return A {@link Headers}.
     */
    Headers headers();

    /**
     * Returns an {@link HttpResponseEntity} representing the data in the response.
     *
     * @return An {@link HttpResponseEntity}.
     */
    HttpResponseEntity responseEntity();

    /**
     * Returns the {@link URI} the request was originally sent to.
     *
     * @return The URI of the request.
     *
     * @see #responseUri()
     */
    URI requestUri();

    /**
     * Returns the {@link URI} of the server instance that actually handled the request. If no redirects have been followed this equals the URI passed to the
     * execute methods of {@link HttpRequestExecutor} otherwise it's the URI of the last location that didn't return a redirect.
     *
     * @return The URI of the responding instance.
     *
     * @see #requestUri()
     */
    URI responseUri();
}
