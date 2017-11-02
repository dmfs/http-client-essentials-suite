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

package org.dmfs.httpessentials.executors.authenticating.authstates.basic;

import org.dmfs.httpessentials.exceptions.UnauthorizedException;
import org.dmfs.httpessentials.executors.authenticating.AuthState;
import org.dmfs.httpessentials.executors.authenticating.Authorization;
import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.executors.authenticating.UserCredentials;
import org.dmfs.httpessentials.executors.authenticating.authorization.BasicAuthorization;
import org.dmfs.optional.Optional;
import org.dmfs.optional.Present;


/**
 * The authenticated Basic AuthState.
 *
 * @author Marten Gajda
 */
public final class AuthenticatedBasicAuthState implements AuthState
{
    private final UserCredentials mCredentials;
    private final AuthState mDelegate;


    AuthenticatedBasicAuthState(UserCredentials credentials, AuthState delegate)
    {
        mCredentials = credentials;
        mDelegate = delegate;
    }


    @Override
    public AuthState withChallenges(Iterable<Challenge> challenges) throws UnauthorizedException
    {
        // apparently Basic auth didn't work (otherwise we wouldn't see more Challenges), proceed with the next AuthState
        return mDelegate.withChallenges(challenges);
    }


    @Override
    public Optional<Authorization> credentials()
    {
        return new Present<Authorization>(new BasicAuthorization(mCredentials));
    }
}
