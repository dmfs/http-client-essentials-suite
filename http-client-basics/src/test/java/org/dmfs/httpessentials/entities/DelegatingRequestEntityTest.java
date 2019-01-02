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
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.jems.optional.Optional;
import org.junit.Test;

import java.io.OutputStream;

import static org.dmfs.jems.mockito.doubles.TestDoubles.dummy;
import static org.dmfs.jems.mockito.doubles.TestDoubles.failingMock;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


/**
 * @author Marten Gajda
 */
public class DelegatingRequestEntityTest
{
    @Test
    public void testContentType()
    {
        HttpRequestEntity mockEntity = failingMock(HttpRequestEntity.class);
        Optional<MediaType> dummyOptional = dummy(Optional.class);
        doReturn(dummyOptional).when(mockEntity).contentType();

        assertThat(new DelegatingTestEntity(mockEntity).contentType(), sameInstance(dummyOptional));
    }


    @Test
    public void testContentLength()
    {
        HttpRequestEntity mockEntity = failingMock(HttpRequestEntity.class);
        Optional<Long> dummyOptional = dummy(Optional.class);
        doReturn(dummyOptional).when(mockEntity).contentLength();

        assertThat(new DelegatingTestEntity(mockEntity).contentLength(), sameInstance(dummyOptional));
    }


    @Test
    public void testWriteContent() throws Exception
    {
        OutputStream dummyStream = dummy(OutputStream.class);

        HttpRequestEntity mockEntity = failingMock(HttpRequestEntity.class);
        doNothing().when(mockEntity).writeContent(dummyStream);

        new DelegatingTestEntity(mockEntity).writeContent(dummyStream);
        verify(mockEntity).writeContent(dummyStream);
    }


    private final static class DelegatingTestEntity extends DelegatingRequestEntity
    {

        public DelegatingTestEntity(HttpRequestEntity delegate)
        {
            super(delegate);
        }
    }
}