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

package org.dmfs.httpessentials.entities;

import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.jems.single.Single;

import java.io.UnsupportedEncodingException;
import java.util.Locale;


/**
 * An {@link HttpRequestEntity} with character data. If the given {@link MediaType} doesn't specify any encoding this will fall back to utf-8.
 *
 * @author Marten Gajda
 */
public final class TextRequestEntity extends DelegatingRequestEntity
{
    public TextRequestEntity(final MediaType mediaType, final Single<CharSequence> charSequence)
    {
        super(new BinaryRequestEntity(mediaType, () -> {
            try
            {
                return charSequence.value().toString().getBytes(mediaType.charset("utf-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                throw new RuntimeException(String.format(Locale.ENGLISH, "Encoding %s not supported by runtime", mediaType.charset("utf-8")), e);
            }
        }));
    }
}
