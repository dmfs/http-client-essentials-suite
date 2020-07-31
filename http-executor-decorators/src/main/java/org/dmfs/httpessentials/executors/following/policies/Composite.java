/*
 * Copyright 2019 dmfs GmbH
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
import org.dmfs.httpessentials.executors.following.RedirectPolicy;
import org.dmfs.iterables.elementary.Seq;
import org.dmfs.jems.optional.adapters.First;
import org.dmfs.jems.predicate.Predicate;

import java.net.URI;


/**
 * A {@link RedirectPolicy} which follows the first {@link RedirectPolicy} which returns a result.
 *
 * @author Marten Gajda
 */
@Deprecated
public final class Composite implements RedirectPolicy
{
    private final Iterable<RedirectPolicy> mDelegates;


    public Composite(RedirectPolicy... delegates)
    {
        this(new Seq<>(delegates));
    }


    public Composite(Iterable<RedirectPolicy> delegates)
    {
        mDelegates = delegates;
    }


    @Override
    public boolean affects(HttpResponse response)
    {
        return new First<>(mDelegates, (Predicate<RedirectPolicy>) testedInstance -> testedInstance.affects(response)).isPresent();
    }


    @Override
    public URI location(HttpResponse response, int redirectNumber) throws RedirectionException
    {
        return new First<>(
                mDelegates,
                (Predicate<RedirectPolicy>) testedInstance -> testedInstance.affects(response)).value().location(response, redirectNumber);
    }
}
