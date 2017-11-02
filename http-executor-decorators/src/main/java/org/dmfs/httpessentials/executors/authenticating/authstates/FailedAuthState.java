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

package org.dmfs.httpessentials.executors.authenticating.authstates;

import org.dmfs.httpessentials.exceptions.UnauthorizedException;
import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.executors.authenticating.Credentials;
import org.dmfs.optional.Optional;

import java.net.URI;

import static org.dmfs.optional.Absent.absent;


/**
 * The ultimate {@link AuthState} in case all others have failed or didn't apply.
 * <p>
 * This always throws an {@link UnauthorizedException}.
 *
 * @author Marten Gajda
 */
public final class FailedAuthState implements AuthState
{

    @Override
    public AuthState withChallenge(org.dmfs.httpessentials.HttpMethod method, URI resourceUri, Iterable<Challenge> challenges) throws UnauthorizedException
    {
        // TODO: add challenges and URI to exception
        throw new UnauthorizedException();
    }


    @Override
    public Optional<Credentials> credentials()
    {
        return absent();
    }
}
