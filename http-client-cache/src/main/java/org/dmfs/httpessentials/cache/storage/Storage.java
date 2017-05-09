/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
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

package org.dmfs.httpessentials.cache.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * The interface of a low level storage object that stores blobs in named slots. Slots can be read and written to using streams. By default each slot is empty.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public interface Storage
{

    /**
     * Checks whether a slot with the given name has any content.
     *
     * @param name
     *         The name of the slot.
     *
     * @return <code>true</code> if the slot has no content, <code>false</code> otherwise.
     *
     * @throws IOException
     */
    boolean isEmpty(String name) throws IOException;

    /**
     * Creates an {@link InputStream} that reads the named slot.
     *
     * @param name
     *         The name of the slot to read.
     *
     * @return An {@link InputStream} that reads the content of the named slot, may be an empty stream.
     *
     * @throws IOException
     *         if the named slot can't be read.
     */
    InputStream inputStream(String name) throws IOException;

    /**
     * Creates an {@link OutputStream} that (over-)writes the given named slot.
     *
     * @param name
     *         The name of the slot to write to.
     *
     * @return An {@link OutputStream} that writes to the named slot.
     *
     * @throws IOException
     *         if the named slot can't be written to.
     */
    OutputStream outputStream(String name) throws IOException;

    /**
     * Removes any content that's stored in the slot with the given name.
     *
     * @param name
     *         The name of the slot to wipe.
     *
     * @throws IOException
     *         if any error occurred during the operation.
     */
    void wipe(String name) throws IOException;

    /**
     * Returns the size of the content that's stored in the slot with the given name.
     *
     * @param name
     *         The name of the slot.
     *
     * @throws IOException
     *         if any error occurred during the operation.
     */
    long size(String name) throws IOException;

}
