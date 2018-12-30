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
import org.dmfs.httpessentials.types.StringMediaType;
import org.dmfs.httpessentials.types.StructuredMediaType;
import org.dmfs.jems.single.Single;
import org.dmfs.jems.single.elementary.ValueSingle;
import org.junit.Test;

import java.io.OutputStream;

import static org.dmfs.jems.hamcrest.matchers.optional.PresentMatcher.present;
import static org.dmfs.jems.mockito.doubles.TestDoubles.dummy;
import static org.dmfs.jems.mockito.doubles.TestDoubles.failingMock;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


/**
 * @author Marten Gajda
 */
public class BinaryRequestEntityTest
{
    @Test
    public void testContentType()
    {
        assertThat(new BinaryRequestEntity(new StructuredMediaType("application", "text"), dummy(Single.class)).contentType(),
                is(present(new StringMediaType("application/text"))));
    }


    @Test
    public void testContentLength()
    {
        assertThat(new BinaryRequestEntity(dummy(MediaType.class), new ValueSingle<>(new byte[0])).contentLength(), is(present(0L)));
        assertThat(new BinaryRequestEntity(dummy(MediaType.class), new ValueSingle<>(new byte[1000])).contentLength(), is(present(1000L)));
    }


    @Test
    public void testWriteContent() throws Exception
    {
        byte[] dummyData = new byte[1000];
        OutputStream mockStream = failingMock(OutputStream.class);
        doNothing().when(mockStream).write(dummyData);

        new BinaryRequestEntity(dummy(MediaType.class), new ValueSingle<>(dummyData)).writeContent(mockStream);

        // make sure the data has actually been written
        verify(mockStream).write(dummyData);
    }

}