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

/**
 * The user credentials represented by username and password.
 *
 * @author Marten Gajda
 */
public interface UserCredentials
{
    /**
     * Returns the username.
     *
     * @return The {@link CharSequence} of the username.
     */
    CharSequence userName();

    /**
     * Returns the password.
     *
     * @return The {@link CharSequence} of the password.
     */
    CharSequence password();
}