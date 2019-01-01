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

import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.jems.single.Single;
import org.dmfs.jems.single.elementary.ValueSingle;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.dmfs.jems.hamcrest.matchers.PresentMatcher.isPresent;
import static org.dmfs.jems.mockito.doubles.TestDoubles.dummy;
import static org.dmfs.jems.mockito.doubles.TestDoubles.failingMock;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;


/**
 * @author Marten Gajda
 */
public class TextRequestEntityTest
{
    @Test
    public void testContentType()
    {
        MediaType dummy = dummy(MediaType.class);
        assertThat(new TextRequestEntity(dummy, dummy(Single.class)).contentType(), isPresent(sameInstance(dummy)));
    }


    @Test
    public void testContentLengthEmpty()
    {
        MediaType mockMediaType = failingMock(MediaType.class);
        doReturn("utf-8").when(mockMediaType).charset("utf-8");
        assertThat(new TextRequestEntity(mockMediaType, new ValueSingle<>("")).contentLength(), isPresent(0L));
    }


    @Test
    public void testContentLengthUtf8()
    {
        // utf-8 encodes äöü as 2 bytes each
        MediaType mockMediaType = failingMock(MediaType.class);
        doReturn("utf-8").when(mockMediaType).charset("utf-8");
        assertThat(new TextRequestEntity(mockMediaType, new ValueSingle<>("abcäöü")).contentLength(), isPresent(9L));
    }


    @Test
    public void testContentLengthLatin1()
    {
        // latin1 encodes äöü as 1 byte each
        MediaType mockMediaType = failingMock(MediaType.class);
        doReturn("latin1").when(mockMediaType).charset("utf-8");
        assertThat(new TextRequestEntity(mockMediaType, new ValueSingle<>("abcäöü")).contentLength(), isPresent(6L));
    }


    @Test
    public void testContentUtf8() throws IOException
    {
        // latin1 encodes äöü as 1 byte each
        MediaType mockMediaType = failingMock(MediaType.class);
        doReturn("utf-8").when(mockMediaType).charset("utf-8");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        new TextRequestEntity(mockMediaType, new ValueSingle<>("abcäöü")).writeContent(out);

        assertThat(out.toByteArray(), is("abcäöü".getBytes("utf-8")));
    }


    @Test
    public void testContentLatin1() throws IOException
    {
        // latin1 encodes äöü as 1 byte each
        MediaType mockMediaType = failingMock(MediaType.class);
        doReturn("latin1").when(mockMediaType).charset("utf-8");
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        new TextRequestEntity(mockMediaType, new ValueSingle<>("abcäöü")).writeContent(out);

        assertThat(out.toByteArray(), is("abcäöü".getBytes("latin1")));
    }
}