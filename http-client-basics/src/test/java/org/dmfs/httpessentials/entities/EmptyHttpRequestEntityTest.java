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

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.dmfs.optional.hamcrest.AbsentMatcher.isAbsent;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


public class EmptyHttpRequestEntityTest
{

    @Test
    public void testContentType()
    {
        // the entity should not return a content-type, since there is no content
        assertThat(EmptyHttpRequestEntity.INSTANCE.contentType(), isAbsent());
    }


    @Test
    public void testContentLength() throws IOException
    {
        // there is no content, so it can't have any length, not even 0
        assertThat(EmptyHttpRequestEntity.INSTANCE.contentLength(), isAbsent());
    }


    @Test
    public void testWriteContent() throws IOException
    {
        EmptyHttpRequestEntity.INSTANCE.writeContent(new OutputStream()
        {

            @Override
            public void write(int b) throws IOException
            {
                fail("writeContent did write something!");
            }
        });
    }

}
