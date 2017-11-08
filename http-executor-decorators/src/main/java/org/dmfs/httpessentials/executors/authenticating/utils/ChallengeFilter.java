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

package org.dmfs.httpessentials.executors.authenticating.utils;

import org.dmfs.httpessentials.executors.authenticating.Challenge;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.iterators.Filter;


/**
 * Filters {@link Challenge}s by a specific auth scheme token.
 *
 * @author Marten Gajda
 */
public final class ChallengeFilter implements Filter<Challenge>
{
    private final Token mToken;


    public ChallengeFilter(Token token)
    {
        mToken = token;
    }


    @Override
    public boolean iterate(Challenge challenge)
    {
        return mToken.toString().equals(challenge.scheme().toString());
    }
}
