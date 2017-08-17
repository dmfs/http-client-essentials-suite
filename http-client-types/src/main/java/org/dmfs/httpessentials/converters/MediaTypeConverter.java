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

package org.dmfs.httpessentials.converters;

import org.dmfs.httpessentials.typedentity.EntityConverter;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.httpessentials.types.StringMediaType;


/**
 * {@link EntityConverter} for {@link MediaType}s.
 *
 * @author Marten Gajda
 */
public final class MediaTypeConverter implements EntityConverter<MediaType>
{
    public final static MediaTypeConverter INSTANCE = new MediaTypeConverter();


    @Override
    public MediaType value(String mediaTypeString)
    {
        return new StringMediaType(mediaTypeString.trim());
    }


    @Override
    public String valueString(MediaType mediaType)
    {
        return mediaType.toString();
    }
}
