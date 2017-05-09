/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
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

package org.dmfs.httpessentials.cache.policies;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.cache.CachePolicy;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpResponse;


/**
 * A very simple {@link CachePolicy} that allows all {@link HttpMethod#GET} requests with an {@link HttpStatus#OK} response to be cached. This does not honor
 * any {@code cache-control} or {@code pragma} headers in the request or response.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class AllowGetCachePolicy implements CachePolicy
{
    @Override
    public boolean isCacheable(HttpRequest<?> request)
    {
        return HttpMethod.GET.equals(request.method());
    }


    @Override
    public boolean isCacheable(HttpResponse response)
    {
        // for now always cache the response to a GET request if the status is OK
        return HttpStatus.OK.equals(response.status());
    }
}
