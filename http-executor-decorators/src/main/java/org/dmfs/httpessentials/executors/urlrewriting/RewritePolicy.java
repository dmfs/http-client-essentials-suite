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

package org.dmfs.httpessentials.executors.urlrewriting;

import org.dmfs.httpessentials.client.HttpRequest;

import java.net.URI;


/**
 * A policy for request URL rewrites. A {@link RewritePolicy} knows how to modify the URL of a request.
 *
 * @author Marten Gajda
 */
public interface RewritePolicy
{
    /**
     * Returns the new URI for any given {@link URI}. If the URL is not supposed to be rewritten this has to return the original {@link URI}.
     *
     * @param location
     *         The original request location.
     * @param request
     *         The request that is about to be sent to the given URI. {@link RewritePolicy} can use this to take request headers and method into consideration
     *         for rewrites
     *
     * @return The actual request location.
     */
    URI rewritten(URI location, HttpRequest<?> request);
}
