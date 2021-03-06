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
import org.dmfs.jems.optional.decorators.Restrained;
import org.dmfs.jems.predicate.composite.AnyOf;
import org.dmfs.jems.predicate.composite.Having;

import static org.dmfs.httpessentials.HttpStatus.MOVED_PERMANENTLY;
import static org.dmfs.httpessentials.HttpStatus.PERMANENT_REDIRECT;


/**
 * A {@link RedirectStrategy} to follow permanent redirects only. Temporary redirects will not be handled with this strategy in charge and the request will be
 * forwarded to the next instance.
 * <p>
 * Example use case:
 * <pre>{@code
 * // follows permanent and secure redirects
 * new Following(executor, new Permanent(new Secure(new FollowStrategy())));
 * }</pre>
 */
public final class Permanent extends DelegatingRedirectStrategy
{
    public Permanent(RedirectStrategy delegate)
    {
        super((response, redirectNumber) ->
                new Restrained<>(
                        new Bool(response, new Having<>(HttpResponse::status, new AnyOf<>(MOVED_PERMANENTLY, PERMANENT_REDIRECT))),
                        delegate.location(response, redirectNumber)));
    }

}
