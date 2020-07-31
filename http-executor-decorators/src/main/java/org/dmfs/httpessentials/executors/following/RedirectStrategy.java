/*
 * Copyright 2020 dmfs GmbH
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

package org.dmfs.httpessentials.executors.following;

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.jems.optional.Optional;

import java.net.URI;


/**
 * A strategy to decide what to do with a redirect response: follow it or not and to what new location. Receiving the entire response allows specifically
 * customized policy implementations.
 */
public interface RedirectStrategy
{
    /***
     * Called when a redirect response is received. Returns the {@link Optional} URI to follow or an absent value to not follow the redirect.
     *
     * @param response
     *         The response which may have to be redirected.
     * @param redirectNumber
     *         the number of this redirect in the call
     */
    Optional<URI> location(HttpResponse response, int redirectNumber);

}
