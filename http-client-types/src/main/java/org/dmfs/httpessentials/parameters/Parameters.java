/*
 * Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
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

package org.dmfs.httpessentials.parameters;

import org.dmfs.httpessentials.converters.LanguageTagConverter;
import org.dmfs.httpessentials.converters.MediaTypeConverter;
import org.dmfs.httpessentials.converters.OptionallyQuoted;
import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.converters.Quoted;
import org.dmfs.httpessentials.converters.RegexpSplitListConverter;
import org.dmfs.httpessentials.converters.UriConverter;
import org.dmfs.httpessentials.typedentity.EntityConverter;
import org.dmfs.httpessentials.types.MediaType;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


/**
 * Defines a number of standard {@link ParameterType}s.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class Parameters
{
    private final static EntityConverter<String> OPTIONALLY_QUOTED_TEXT = new OptionallyQuoted<>(PlainStringHeaderConverter.INSTANCE);

    public final static ParameterType<String> CHARSET = new BasicParameterType<>("charset", OPTIONALLY_QUOTED_TEXT);

    public final static ParameterType<String> TITLE = new BasicParameterType<>("title", OPTIONALLY_QUOTED_TEXT);

    public final static ParameterType<MediaType> TYPE = new BasicParameterType<>("type", new OptionallyQuoted<>(MediaTypeConverter.INSTANCE));

    public final static ParameterType<Locale> HREFLANG = new BasicParameterType<>("hreflang", LanguageTagConverter.INSTANCE);

    public final static ParameterType<URI> ANCHOR = new BasicParameterType<>("anchor", new Quoted<>(UriConverter.INSTANCE));

    /**
     * {@link ParameterType} to retrieve the raw value of a rel param (i.e. a space delimited list of rel-types).
     */
    public final static ParameterType<List<String>> REL = new BasicParameterType<>("rel",
            new OptionallyQuoted<>(new RegexpSplitListConverter<>(PlainStringHeaderConverter.INSTANCE, Pattern.compile("\\s+"), " ")));

    /**
     * {@link ParameterType} to retrieve the raw value of a rev param (i.e. a space delimited list of reverse rel-types).
     */
    public final static ParameterType<List<String>> REV = new BasicParameterType<>("rev",
            new OptionallyQuoted<>(new RegexpSplitListConverter<>(PlainStringHeaderConverter.INSTANCE, Pattern.compile("\\s+"), " ")));


    /**
     * No instances please.
     */
    private Parameters()
    {
    }

}
