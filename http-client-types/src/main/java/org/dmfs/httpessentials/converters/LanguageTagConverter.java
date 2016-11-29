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

package org.dmfs.httpessentials.converters;

import org.dmfs.httpessentials.typedentity.EntityConverter;

import java.util.Locale;


/**
 * {@link EntityConverter} for Language Tags ({@link Locale}s).
 *
 * @author Marten Gajda
 */
public final class LanguageTagConverter implements EntityConverter<Locale>
{
    public final static LanguageTagConverter INSTANCE = new LanguageTagConverter();


    @Override
    public Locale value(String languageTagString)
    {
        return Locale.forLanguageTag(languageTagString.trim());
    }


    @Override
    public String valueString(Locale locale)
    {
        return locale.toLanguageTag();
    }

}
