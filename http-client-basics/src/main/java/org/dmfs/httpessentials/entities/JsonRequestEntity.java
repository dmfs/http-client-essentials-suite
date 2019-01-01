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
import org.dmfs.httpessentials.types.StructuredMediaType;
import org.dmfs.jems.single.Single;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * An {@link HttpRequestEntity} for JSON data.
 *
 * @author Marten Gajda
 */
public final class JsonRequestEntity extends DelegatingRequestEntity
{
    public JsonRequestEntity(final JSONArray jsonArray)
    {
        this(jsonArray::toString);
    }


    public JsonRequestEntity(final JSONObject jsonObject)
    {
        this(jsonObject::toString);
    }


    private JsonRequestEntity(final Single<CharSequence> jsonString)
    {
        // Note: There is no charset parameter specified for application/json. A JSON file is supposed to be in Unicode.
        // Whether it's UTF-8, UTF-16BE, UTF-16LE or UTF-32 should be determined by looking at the first 2 or 4 bytes.
        super(new TextRequestEntity(
                new StructuredMediaType("application", "json"),
                jsonString));
    }
}