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

import org.dmfs.httpessentials.converters.IntegerConverter;
import org.dmfs.httpessentials.converters.LinkConverter;
import org.dmfs.httpessentials.converters.MediaTypeConverter;
import org.dmfs.httpessentials.converters.OptionallyQuoted;
import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.converters.UriConverter;
import org.dmfs.httpessentials.converters.UserAgentConverter;
import org.dmfs.httpessentials.typedentity.EntityConverter;
import org.dmfs.httpessentials.types.Link;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.httpessentials.types.UserAgent;

import java.net.URI;


/**
 * Defines {@link HeaderType}s for well known standard headers.
 *
 * @author Marten Gajda
 */
public final class HttpHeaders
{
    /**
     * The content-type header that contains the media-type of the response entity.
     */
    public final static SingletonHeaderType<MediaType> CONTENT_TYPE = new BasicSingletonHeaderType<MediaType>(
            "content-type", MediaTypeConverter.INSTANCE);
    /**
     * The content-length header that contains the number of bytes of the response entity.
     */
    public final static SingletonHeaderType<Integer> CONTENT_LENGTH = new BasicSingletonHeaderType<Integer>(
            "content-length", IntegerConverter.INSTANCE);
    /**
     * The Link header that contains links to documents related to the response.
     */
    public final static ListHeaderType<Link> LINK = new BasicListHeaderType<Link>("link", new LinkConverter());
    /**
     * The location header.
     */
    public final static SingletonHeaderType<URI> LOCATION = new BasicSingletonHeaderType<URI>("location",
            UriConverter.INSTANCE);
    /**
     * User-Agent header type.
     */
    public final static SingletonHeaderType<UserAgent> USER_AGENT = new BasicSingletonHeaderType<UserAgent>(
            "User-Agent", new UserAgentConverter());
    private final static EntityConverter<String> OPTIONALLY_QUOTED_TEXT = new OptionallyQuoted<>(PlainStringHeaderConverter.INSTANCE);
    /**
     * The accept-encoding header that contains the content-encoding of the response entity.
     */
    public final static ListHeaderType<String> ACCEPT_ENCODING = new BasicListHeaderType<String>("accept-encoding",
            OPTIONALLY_QUOTED_TEXT);
    /**
     * The content-encoding header that contains the content encoding of the response entity.
     */
    public final static ListHeaderType<String> CONTENT_ENCODING = new BasicListHeaderType<String>("content-encoding",
            OPTIONALLY_QUOTED_TEXT);


    /**
     * No instances constructor.
     */
    private HttpHeaders()
    {
    }
}
