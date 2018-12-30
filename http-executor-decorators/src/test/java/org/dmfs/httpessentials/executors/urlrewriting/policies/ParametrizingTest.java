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
import org.dmfs.rfc3986.parameters.ParameterType;
import org.dmfs.rfc3986.parameters.parametertypes.BasicParameterType;
import org.dmfs.rfc3986.parameters.valuetypes.TextValueType;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author Marten Gajda
 */
public class ParametrizingTest
{
    ParameterType<CharSequence> TEST_PARAM = new BasicParameterType<>("abc", TextValueType.INSTANCE);
    ParameterType<CharSequence> TEST_PARAM2 = new BasicParameterType<>("def", TextValueType.INSTANCE);


    @Test
    public void testAddSoleParam()
    {
        HttpRequest mockRequest = mock(HttpRequest.class);
        RewritePolicy mockPolicy = mock(RewritePolicy.class);
        when(mockPolicy.rewritten(URI.create("http://example.com/test?abc=123"), mockRequest)).thenReturn(URI.create("https://example.org/result"));

        // test that it returns whatever the delegate returns
        assertEquals(URI.create("https://example.org/result"),
                new Parametrizing(mockPolicy, TEST_PARAM.parameter("123")).rewritten(URI.create("http://example.com/test"), mockRequest));
    }


    @Test
    public void testAddParam()
    {
        HttpRequest mockRequest = mock(HttpRequest.class);
        RewritePolicy mockPolicy = mock(RewritePolicy.class);
        when(mockPolicy.rewritten(URI.create("http://example.com/test?xyz=987&abc=123"), mockRequest)).thenReturn(URI.create("https://example.org/result"));

        // test that it returns whatever the delegate returns
        assertEquals(URI.create("https://example.org/result"),
                new Parametrizing(mockPolicy, TEST_PARAM.parameter("123")).rewritten(URI.create("http://example.com/test?xyz=987"), mockRequest));
    }


    @Test
    public void testReplaceSoleParam()
    {
        HttpRequest mockRequest = mock(HttpRequest.class);
        RewritePolicy mockPolicy = mock(RewritePolicy.class);
        when(mockPolicy.rewritten(URI.create("http://example.com/test?abc=123"), mockRequest)).thenReturn(URI.create("https://example.org/result"));

        // test that it returns whatever the delegate returns
        assertEquals(URI.create("https://example.org/result"),
                new Parametrizing(mockPolicy, TEST_PARAM.parameter("123")).rewritten(URI.create("http://example.com/test?abc=456"), mockRequest));
    }


    @Test
    public void testReplaceParam()
    {
        HttpRequest mockRequest = mock(HttpRequest.class);
        RewritePolicy mockPolicy = mock(RewritePolicy.class);
        when(mockPolicy.rewritten(URI.create("http://example.com/test?xyz=987&abc=123"), mockRequest)).thenReturn(URI.create("https://example.org/result"));

        // test that it returns whatever the delegate returns
        assertEquals(URI.create("https://example.org/result"),
                new Parametrizing(mockPolicy, TEST_PARAM.parameter("123")).rewritten(URI.create("http://example.com/test?abc=456&xyz=987"), mockRequest));
    }


    @Test
    public void testMixedUpdate()
    {
        HttpRequest mockRequest = mock(HttpRequest.class);
        RewritePolicy mockPolicy = mock(RewritePolicy.class);
        when(mockPolicy.rewritten(URI.create("http://example.com/test?xyz=987&abc=123&def=444"), mockRequest)).thenReturn(
                URI.create("https://example.org/result"));

        // test that it returns whatever the delegate returns
        assertEquals(URI.create("https://example.org/result"),
                new Parametrizing(mockPolicy, TEST_PARAM.parameter("123"), TEST_PARAM2.parameter("444")).rewritten(
                        URI.create("http://example.com/test?abc=456&xyz=987"), mockRequest));
    }
}