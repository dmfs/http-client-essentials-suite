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
import org.dmfs.httpessentials.types.Link;
import org.dmfs.httpessentials.types.StringLink;


/**
 * Converts between String and {@link Link} values.
 *
 * @author Marten Gajda
 */
public final class LinkConverter implements EntityConverter<Link>
{
    public final static LinkConverter INSTANCE = new LinkConverter();


    @Override
    public Link value(String headerValueString)
    {
        return new StringLink(headerValueString.trim());
    }


    @Override
    public String valueString(Link headerValue)
    {
        throw new UnsupportedOperationException("serializing Links is not supported yet");
    }

}
