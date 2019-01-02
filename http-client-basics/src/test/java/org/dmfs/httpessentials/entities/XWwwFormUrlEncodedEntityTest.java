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
import org.dmfs.iterables.EmptyIterable;
import org.dmfs.iterables.elementary.Seq;
import org.dmfs.jems.pair.elementary.ValuePair;
import org.dmfs.rfc3986.parameters.parametersets.EmptyParameterList;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.dmfs.jems.hamcrest.matchers.optional.PresentMatcher.present;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * @author Marten Gajda
 */
public class XWwwFormUrlEncodedEntityTest
{
    @Test
    public void testContentType()
    {
        assertThat(new XWwwFormUrlEncodedEntity(new EmptyIterable<>()).contentType(),
                is(present(new StringMediaType("application/x-www-form-urlencoded"))));
        assertThat(new XWwwFormUrlEncodedEntity(new EmptyParameterList()).contentType(),
                is(present(new StringMediaType("application/x-www-form-urlencoded"))));
    }


    @Test
    public void testContentLength() throws UnsupportedEncodingException
    {
        assertThat(new XWwwFormUrlEncodedEntity(
                new EmptyIterable<>()).contentLength(), is(present(0L)));

        assertThat(new XWwwFormUrlEncodedEntity(
                        new Seq<>(new ValuePair<>("key", "valueäöü"))).contentLength(),
                is(present((long) "key=value%C3%A4%C3%B6%C3%BC".getBytes("UTF-8").length)));

        assertThat(new XWwwFormUrlEncodedEntity(
                        new Seq<>(
                                new ValuePair<>("key1", "valueäöü"),
                                new ValuePair<>("key2", "value/+ "))).contentLength(),
                is(present((long) "key1=value%C3%A4%C3%B6%C3%BC&key2=value%2F%2B+".getBytes("UTF-8").length)));
    }


    @Test
    public void testEmptyContent() throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new XWwwFormUrlEncodedEntity(new EmptyIterable<>()).writeContent(out);
        assertThat(out.toByteArray(), is("".getBytes("utf-8")));
    }


    @Test
    public void testUtf8Content() throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new XWwwFormUrlEncodedEntity(new Seq<>(new ValuePair<>("key", "valueäöü"))).writeContent(out);
        assertThat(out.toByteArray(), is("key=value%C3%A4%C3%B6%C3%BC".getBytes("utf-8")));
    }


    @Test
    public void testUtf8Content2() throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        new XWwwFormUrlEncodedEntity(new Seq<>(
                new ValuePair<>("key1", "valueäöü"),
                new ValuePair<>("key2", "value/+ "))).writeContent(out);
        assertThat(out.toByteArray(), is("key1=value%C3%A4%C3%B6%C3%BC&key2=value%2F%2B+".getBytes("utf-8")));
    }
}