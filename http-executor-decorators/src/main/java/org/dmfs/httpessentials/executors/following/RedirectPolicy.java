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

package org.dmfs.httpessentials.executors.following;

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.TooManyRedirectsException;

import java.io.IOException;
import java.net.URI;


/**
 * Interface for defining a redirect policy, i.e. deciding what to do with a redirect response: follow it or not and to what new location. Receiving the entire
 * response allows specifically customized policy implementations.
 *
 * @author Marten Gajda
 * @author Gabor Keszthelyi
 */
public interface RedirectPolicy
{

    /**
     * Determines whether this {@link RedirectPolicy} affects the given {@link HttpResponse}. The result of this is undefined if the response has a status code
     * that is not in the rage of redirection status codes.
     *
     * @param response
     *         An {@link HttpResponse} with any redirection status code (i.e. 3xx).
     *
     * @return {@code true} if this {@link RedirectPolicy} affects the given response, {@code false} otherwise.
     */
    boolean affects(HttpResponse response) throws IOException;

    /***
     * Called when a redirect response (see status codes below) is received. Returns the URI to follow or throws {@link RedirectionException} (which results in
     * not following the redirection).
     *
     * Note, the behavior of this method is undefined if {@link #affects(HttpResponse)} returns {@code false} for the same {@link HttpResponse}.
     *
     * @param response
     *         Response with any redirection status code (i.e. 3xx)
     * @param redirectNumber
     *         the number of this redirect in the call
     *
     * @return the new location to follow (absolute uri).
     *
     * @throws RedirectionException
     *         if policy decides to not to follow the redirect.
     * @throws TooManyRedirectsException
     *         if policy decides this would be too many redirects
     */
    URI location(HttpResponse response, int redirectNumber) throws RedirectionException, TooManyRedirectsException, IOException;

}
