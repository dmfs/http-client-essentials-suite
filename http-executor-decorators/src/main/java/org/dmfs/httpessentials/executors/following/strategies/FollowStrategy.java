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
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.adapters.Conditional;
import org.dmfs.jems.optional.decorators.Mapped;
import org.dmfs.jems.predicate.composite.AnyOf;
import org.dmfs.jems.predicate.composite.Having;

import java.net.URI;

import static org.dmfs.httpessentials.HttpStatus.FOUND;
import static org.dmfs.httpessentials.HttpStatus.MOVED_PERMANENTLY;
import static org.dmfs.httpessentials.HttpStatus.PERMANENT_REDIRECT;
import static org.dmfs.httpessentials.HttpStatus.TEMPORARY_REDIRECT;


/**
 * A {@link RedirectStrategy} that follows all temporary and permanent redirects.
 */
public final class FollowStrategy implements RedirectStrategy
{
    @Override
    public Optional<URI> location(HttpResponse response, int redirectNumber)
    {
        return new Mapped<>(
                r -> r.responseUri().resolve(r.headers().header(HttpHeaders.LOCATION).value()),
                new Conditional<>(
                        new Having<>(HttpResponse::status, new AnyOf<>(MOVED_PERMANENTLY, FOUND, TEMPORARY_REDIRECT, PERMANENT_REDIRECT)),
                        response));
    }
}
