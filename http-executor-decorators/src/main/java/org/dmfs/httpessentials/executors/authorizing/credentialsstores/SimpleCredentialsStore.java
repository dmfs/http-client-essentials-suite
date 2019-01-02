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

package org.dmfs.httpessentials.executors.authorizing.credentialsstores;

import org.dmfs.httpessentials.executors.authorizing.AuthScope;
import org.dmfs.httpessentials.executors.authorizing.CredentialsStore;
import org.dmfs.httpessentials.executors.authorizing.ServiceScope;
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.elementary.Present;

import static org.dmfs.jems.optional.elementary.Absent.absent;


/**
 * A {@link CredentialsStore} which serves a single credentials for a specific {@link ServiceScope}.
 *
 * @author Marten Gajda
 */
public final class SimpleCredentialsStore<CredentialsType> implements CredentialsStore<CredentialsType>
{
    private final ServiceScope mServiceScope;
    private final CredentialsType mCredentials;


    public SimpleCredentialsStore(ServiceScope serviceScope, CredentialsType credentials)
    {
        mServiceScope = serviceScope;
        mCredentials = credentials;
    }


    @Override
    public Optional<CredentialsType> credentials(AuthScope authScope)
    {
        return mServiceScope.contains(authScope) ? new Present<>(mCredentials) : absent();
    }
}
