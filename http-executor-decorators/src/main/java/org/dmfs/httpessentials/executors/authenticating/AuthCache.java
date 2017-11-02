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

package org.dmfs.httpessentials.executors.authenticating;

import org.dmfs.httpessentials.executors.authenticating.strategies.PassThroughStrategy;

import java.net.URI;


/**
 * A cache for premature authentication strategies.
 *
 * @author Marten Gajda
 */
public interface AuthCache
{
    /**
     * Returns an {@link AuthStrategy} for the given URI.
     * <p>
     * Note that this will always return an {@link AuthStrategy}, even if there is none cached yet. In this case a {@link PassThroughStrategy} will be returned
     * as the default.
     *
     * @param uri
     *         The {@link URI} to authenticate.
     *
     * @return An {@link AuthStrategy}.
     */
    AuthStrategy authStrategy(URI uri);

    // TODO: add some way add/update entries
}
