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

package org.dmfs.httpessentials.executors.urlrewriting.policies;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.executors.urlrewriting.RewritePolicy;

import java.net.URI;


/**
 * A {@link RewritePolicy} which resolves any relative input URL against a given base URL.
 * <p>
 * Note, the input URL is resolved before it's being passed on to the decorated {@link RewritePolicy}.
 *
 * @author Marten Gajda
 */
public final class Resolving implements RewritePolicy
{
    private final RewritePolicy mDelegate;
    private final URI mBaseUrl;


    public Resolving(RewritePolicy delegate, URI baseUrl)
    {
        mDelegate = delegate;
        mBaseUrl = baseUrl;
    }


    @Override
    public URI rewritten(URI location, HttpRequest<?> request)
    {
        return mDelegate.rewritten(mBaseUrl.resolve(location), request);
    }
}
