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

package org.dmfs.httpessentials.executors.authorizing.authstates;

import org.dmfs.httpessentials.exceptions.UnauthorizedException;
import org.dmfs.httpessentials.executors.authorizing.AuthInfo;
import org.dmfs.httpessentials.executors.authorizing.AuthState;
import org.dmfs.httpessentials.executors.authorizing.AuthStrategy;
import org.dmfs.httpessentials.executors.authorizing.Authorization;
import org.dmfs.httpessentials.executors.authorizing.Challenge;
import org.dmfs.jems.optional.Optional;

import static org.dmfs.jems.optional.elementary.Absent.absent;


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
    public AuthState withChallenges(Iterable<Challenge> challenges) throws UnauthorizedException
    {
        // TODO: add challenges and URI to Exception
        throw new UnauthorizedException();
    }


    @Override
    public Optional<Authorization> authorization()
    {
        return absent();
    }


    @Override
    public AuthStrategy prematureAuthStrategy(Optional<AuthInfo> authInfo)
    {
        throw new UnsupportedOperationException("A FailedAuthState is not suitable for premature authentication.");
    }
}
