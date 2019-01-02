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

import org.dmfs.httpessentials.types.StringMediaType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.dmfs.jems.hamcrest.matchers.optional.PresentMatcher.present;
import static org.dmfs.jems.mockito.doubles.TestDoubles.failingMock;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;


/**
 * @author Marten Gajda
 */
public class JsonRequestEntityTest
{
    @Test
    public void testContentType()
    {
        assertThat(new JsonRequestEntity(new JSONObject()).contentType(), is(present(new StringMediaType("application/json"))));
        assertThat(new JsonRequestEntity(new JSONArray()).contentType(), is(present(new StringMediaType("application/json"))));
    }


    @Test
    public void testJSONObjectContentLength() throws UnsupportedEncodingException
    {
        JSONObject mockObject = failingMock(JSONObject.class);
        doReturn("dummyStringäöü").when(mockObject).toString();
        assertThat(new JsonRequestEntity(mockObject).contentLength(), is(present((long) "dummyStringäöü".getBytes("utf-8").length)));
    }


    @Test
    public void testJSONArrayContentLength() throws UnsupportedEncodingException
    {
        JSONArray mockArray = failingMock(JSONArray.class);
        doReturn("dummyStringäöü").when(mockArray).toString();
        assertThat(new JsonRequestEntity(mockArray).contentLength(), is(present((long) "dummyStringäöü".getBytes("utf-8").length)));
    }


    @Test
    public void testJSONObjectContent() throws IOException
    {
        JSONObject mockObject = failingMock(JSONObject.class);
        doReturn("dummyStringäöü").when(mockObject).toString();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        new JsonRequestEntity(mockObject).writeContent(out);

        assertThat(out.toByteArray(), is("dummyStringäöü".getBytes("utf-8")));
    }


    @Test
    public void testJSONArrayContent() throws IOException
    {
        JSONArray mockArray = failingMock(JSONArray.class);
        doReturn("dummyStringäöü").when(mockArray).toString();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        new JsonRequestEntity(mockArray).writeContent(out);

        assertThat(out.toByteArray(), is("dummyStringäöü".getBytes("utf-8")));
    }
}