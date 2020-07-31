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
import org.dmfs.jems.optional.decorators.Sieved;
import org.dmfs.jems.predicate.Predicate;
import org.dmfs.jems.predicate.composite.Having;

import java.net.URI;


/**
 * A {@link RedirectStrategy} that only follows redirects from and to secure addresses. (Source and destination both need to be https urls.)
 */
public final class Secure extends DelegatingRedirectStrategy
{
    private static final Predicate<URI> IS_HTTPS_SCHEME = new Having<>(URI::getScheme, "https"::equalsIgnoreCase);


    public Secure(RedirectStrategy delegate)
    {
        super(((response, redirectNumber) ->
                new Restrained<>(
                        new Bool(response, new Having<>(HttpResponse::responseUri, IS_HTTPS_SCHEME)),
                        new Sieved<>(IS_HTTPS_SCHEME, delegate.location(response, redirectNumber)))));
    }
}
