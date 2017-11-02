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

package org.dmfs.httpessentials.executors.authenticating.authschemes;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authenticating.AuthScheme;
import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.CredentialsStore;
import org.dmfs.httpessentials.executors.authenticating.UserCredentials;
import org.dmfs.httpessentials.executors.authenticating.authstates.basic.BasicAuthState;

import java.net.URI;


/**
 * The Basic authentication scheme.
 *
 * @author Marten Gajda
 */
public final class Basic implements AuthScheme<UserCredentials>
{
    @Override
    public AuthState authState(CredentialsStore<UserCredentials> credentialsStore, HttpMethod method, URI uri, AuthState naFallback, AuthState failFallback)
    {
        return new BasicAuthState(credentialsStore, uri, naFallback, failFallback);
    }
}
