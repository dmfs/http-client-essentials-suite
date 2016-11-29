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


/**
 * Converts between String and {@link Integer} values.
 *
 * @author Marten Gajda
 */
public final class IntegerConverter implements EntityConverter<Integer>
{
    public final static IntegerConverter INSTANCE = new IntegerConverter();


    @Override
    public Integer value(String headerValueString)
    {
        return Integer.parseInt(headerValueString.trim());
    }


    @Override
    public String valueString(Integer headerValue)
    {
        return headerValue.toString();
    }

}
