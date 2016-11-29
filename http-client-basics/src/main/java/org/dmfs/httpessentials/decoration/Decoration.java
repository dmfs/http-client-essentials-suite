/*
 * Copyright 2016 dmfs GmbH
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

package org.dmfs.httpessentials.decoration;

/**
 * General interface for a 'decoration operation' that is taking on object and returning a wrapped or modified version of it.
 *
 * @author Gabor Keszthelyi
 */
public interface Decoration<T>
{

    /**
     * Decorates the given original object.
     *
     * @param original
     *         object to decorate
     *
     * @return the new wrapped object
     */
    T decorated(T original);
}
