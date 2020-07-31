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

package org.dmfs.httpessentials.executors.following.strategies;

import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.executors.following.RedirectStrategy;
import org.dmfs.jems.optional.decorators.Sieved;
import org.dmfs.jems.predicate.Predicate;
import org.dmfs.jems.predicate.composite.AllOf;
import org.dmfs.jems.predicate.composite.Having;
import org.dmfs.jems.predicate.elementary.Equals;

import java.net.URI;


/**
 * A {@link RedirectStrategy} decorator that only follows redirects to the same host and the same scheme.
 */
public final class SameAuthority extends DelegatingRedirectStrategy
{
    public SameAuthority(RedirectStrategy delegate)
    {
        super(((response, redirectNumber) ->
                new Sieved<>(
                        sameHost(response),
                        delegate.location(response, redirectNumber))));
    }


    private static Predicate<URI> sameHost(HttpResponse response)
    {
        return new AllOf<>(
                new Having<>(URI::getScheme, new Equals<>(response.responseUri().getScheme())),
                new Having<>(URI::getAuthority, new Equals<>(response.responseUri().getAuthority())));

    }
}
