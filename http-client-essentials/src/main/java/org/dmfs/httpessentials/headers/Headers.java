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

package org.dmfs.httpessentials.headers;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * Interface of an object that holds a list of {@link Header}s. Instances must be immutable.
 *
 * @author Marten Gajda
 */
public interface Headers extends Iterable<Header<?>>
{
    /**
     * Returns whether these Headers contain a {@link Header} of the given {@link HeaderType}.
     *
     * @param headerType
     *
     * @return
     */
    boolean contains(HeaderType<?> headerType);

    /**
     * Returns the value of the header of the given {@link SingletonHeaderType}.
     *
     * @param headerType
     *         The {@link HeaderType} to look for.
     *
     * @return An {@link Iterator} of {@link Header}s, may be empty, but never <code>null</code>.
     *
     * @throws NoSuchElementException
     *         if no such header exists within these {@link Headers}.
     */
    <T> Header<T> header(SingletonHeaderType<T> headerType);

    /**
     * Returns the value of the header of the given {@link ListHeaderType}.
     *
     * @param headerType
     *         The {@link HeaderType} to look for.
     *
     * @return An {@link Iterator} of {@link Header}s, may be empty, but never <code>null</code>.
     *
     * @throws NoSuchElementException
     *         if no such header exists within these {@link Headers}.
     */
    <T> Header<List<T>> header(ListHeaderType<T> headerType);

    /**
     * Returns {@link Headers} that contain the given header. If another header of the same type already exists in this object, it is overridden with the new
     * header.
     *
     * @param header
     *         The header to add.
     *
     * @return {@link Headers} that contain the given header.
     */
    <T> Headers withHeader(Header<T> header);

    /**
     * Removes any header of the given {@link HeaderType} and returns the resulting {@link Headers}.
     *
     * @param headerType
     *         The {@link HeaderType} of the headers to remove.
     *
     * @return {@link Headers} that don't contain any header of the given type.
     */
    <T> Headers withoutHeaderType(HeaderType<T> headerType);
}
