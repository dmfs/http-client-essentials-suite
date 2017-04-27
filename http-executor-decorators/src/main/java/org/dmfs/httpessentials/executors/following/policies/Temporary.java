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

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.TooManyRedirectsException;
import org.dmfs.httpessentials.executors.following.RedirectPolicy;

import java.net.URI;

import static org.dmfs.httpessentials.HttpStatus.FOUND;
import static org.dmfs.httpessentials.HttpStatus.SEE_OTHER;
import static org.dmfs.httpessentials.HttpStatus.TEMPORARY_REDIRECT;


/**
 * A {@link RedirectPolicy} to follow temporary redirects only. Permanent redirects will not be handled with this policy in charge and the request will be
 * forwarded to the next instance.
 * <p>
 * Example use case:
 * <pre>{@code
 * // follows up to 5 temporary and secure redirects
 * new Following(executor, new Temporary(new Secure(new FollowRedirectPolicy())));
 * }</pre>
 *
 * @author Marten Gajda
 */
public final class Temporary implements RedirectPolicy
{
    private final RedirectPolicy mDecoratedPolicy;


    public Temporary(RedirectPolicy decoratedPolicy)
    {
        mDecoratedPolicy = decoratedPolicy;
    }


    @Override
    public boolean affects(HttpResponse response)
    {
        HttpStatus status = response.status();
        // we handle only temporary redirects
        return (FOUND.equals(status)
                || SEE_OTHER.equals(status) // TODO: is this considered "temporary"?
                || TEMPORARY_REDIRECT.equals(status))
                && mDecoratedPolicy.affects(response);
    }


    @Override
    public URI location(HttpResponse response, int redirectNumber) throws RedirectionException, TooManyRedirectsException
    {
        return mDecoratedPolicy.location(response, redirectNumber);
    }

}
