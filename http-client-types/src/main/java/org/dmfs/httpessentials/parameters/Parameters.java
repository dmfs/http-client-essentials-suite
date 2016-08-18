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
import org.dmfs.httpessentials.converters.QuotedStringConverter;
import org.dmfs.httpessentials.converters.UriConverter;
import org.dmfs.httpessentials.types.MediaType;

import java.net.URI;
import java.util.Locale;


/**
 * Defines a number of standard {@link ParameterType}s.
 *
 * @author Marten Gajda <marten@dmfs.org>
 */
public final class Parameters
{
    public final static ParameterType<String> CHARSET = new BasicParameterType<String>("charset",
            QuotedStringConverter.INSTANCE);

    public final static ParameterType<String> TITLE = new BasicParameterType<String>("title",
            QuotedStringConverter.INSTANCE);

    public final static ParameterType<MediaType> TYPE = new BasicParameterType<MediaType>("type",
            MediaTypeConverter.INSTANCE);

    public final static ParameterType<Locale> HREFLANG = new BasicParameterType<Locale>("hreflang",
            LanguageTagConverter.INSTANCE);

    public final static ParameterType<URI> ANCHOR = new BasicParameterType<URI>("anchor", UriConverter.INSTANCE);


    /**
     * No instances please.
     */
    private Parameters()
    {
    }

}
