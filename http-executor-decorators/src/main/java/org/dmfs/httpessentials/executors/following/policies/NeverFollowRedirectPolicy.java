/*
 * Copyright 2016 dmfs GmbH
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

package org.dmfs.httpessentials.executors.following.policies;

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.TooManyRedirectsException;
import org.dmfs.httpessentials.executors.following.RedirectPolicy;
import org.dmfs.httpessentials.headers.HttpHeaders;

import java.net.URI;


/**
 * A {@link RedirectPolicy} that never allows following redirects (always throws {@link RedirectionException}.
 *
 * @author Gabor Keszthelyi
 */
public final class NeverFollowRedirectPolicy implements RedirectPolicy
{

    @Override
    public URI location(HttpResponse response, int redirectNumber) throws RedirectionException, TooManyRedirectsException
    {
        URI newLocation = response.headers().header(HttpHeaders.LOCATION).value();
        throw new RedirectionException(response.status(), response.requestUri(), newLocation);
    }
}
