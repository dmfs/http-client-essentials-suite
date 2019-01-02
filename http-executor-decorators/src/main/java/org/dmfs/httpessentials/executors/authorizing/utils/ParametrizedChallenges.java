/*
 * Copyright 2018 dmfs GmbH
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

package org.dmfs.httpessentials.executors.authorizing.utils;

import org.dmfs.httpessentials.executors.authorizing.Challenge;
import org.dmfs.httpessentials.executors.authorizing.Parametrized;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.iterables.decorators.DelegatingIterable;
import org.dmfs.iterables.decorators.Sieved;
import org.dmfs.jems.iterable.decorators.Mapped;
import org.dmfs.jems.predicate.Predicate;


/**
 * @author Marten Gajda
 */
public final class ParametrizedChallenges extends DelegatingIterable<Parametrized>
{
    public ParametrizedChallenges(Predicate<Challenge> challengePredicate, Iterable<Challenge> delegate)
    {
        super(new Mapped<>(argument -> new SimpleParametrized(argument.challenge()), new Sieved<>(challengePredicate, delegate)));
    }


    public ParametrizedChallenges(Token schemeToken, Iterable<Challenge> delegate)
    {
        this(challenge -> schemeToken.equals(challenge.scheme()), delegate);
    }

}
