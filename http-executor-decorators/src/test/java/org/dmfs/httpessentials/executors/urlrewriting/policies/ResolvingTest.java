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

package org.dmfs.httpessentials.executors.urlrewriting.policies;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.executors.urlrewriting.RewritePolicy;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author Marten Gajda
 */
public class ResolvingTest
{
    @Test
    public void testRewrittenRelative() throws Exception
    {
        HttpRequest mockRequest = mock(HttpRequest.class);
        RewritePolicy mockPolicy = mock(RewritePolicy.class);
        when(mockPolicy.rewritten(URI.create("http://example.com/test"), mockRequest)).thenReturn(URI.create("https://example.org/result"));

        // test that it returns whatever the delegate returns
        assertEquals(URI.create("https://example.org/result"),
                new Resolving(mockPolicy, URI.create("http://example.com")).rewritten(URI.create("/test"), mockRequest));
    }


    @Test
    public void testRewrittenAbsolute() throws Exception
    {
        HttpRequest mockRequest = mock(HttpRequest.class);
        RewritePolicy mockPolicy = mock(RewritePolicy.class);
        when(mockPolicy.rewritten(URI.create("http://example.org"), mockRequest)).thenReturn(URI.create("https://example.net/result"));

        // test that it returns whatever the delegate returns
        assertEquals(URI.create("https://example.net/result"),
                new Resolving(mockPolicy, URI.create("http://example.com")).rewritten(URI.create("http://example.org"), mockRequest));
    }
}