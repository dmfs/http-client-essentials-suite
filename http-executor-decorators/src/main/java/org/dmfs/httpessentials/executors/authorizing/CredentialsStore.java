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

package org.dmfs.httpessentials.executors.authorizing;

import org.dmfs.optional.Optional;


/**
 * A store which can provide credentials for a specific {@link AuthScope}.
 *
 * @author Marten Gajda
 */
public interface CredentialsStore<CredentialsType>
{
    /**
     * Returns the {@link Optional} user credentials for the given {@link AuthScope}.
     *
     * @param authScope
     *         The {@link AuthScope}.
     *
     * @return An {@link Optional} with credentials, it may be absent if the store doesn't have user credentials for this {@link AuthScope}.
     */
    Optional<CredentialsType> credentials(AuthScope authScope);
}
