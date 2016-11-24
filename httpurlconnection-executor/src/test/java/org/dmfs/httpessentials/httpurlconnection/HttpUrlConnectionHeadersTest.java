/*
 * Copyright 2016 Marten Gajda <marten@dmfs.org>
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

package org.dmfs.httpessentials.httpurlconnection;

import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.dmfs.httpessentials.types.Link;
import org.dmfs.httpessentials.types.StructuredMediaType;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @author Marten Gajda
 */
public class HttpUrlConnectionHeadersTest
{
    @Test
    public void testContainsNoHeaders() throws Exception
    {
        Map<String, List<String>> headers = new HashMap<>();

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);

        Headers testHeaders = new HttpUrlConnectionHeaders(connection);

        assertFalse(testHeaders.contains(HttpHeaders.LINK));
    }


    @Test
    public void testContainsOtherHeaders() throws Exception
    {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("content-type", Arrays.asList("application/octetstream"));

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);

        Headers testHeaders = new HttpUrlConnectionHeaders(connection);

        assertFalse(testHeaders.contains(HttpHeaders.LINK));
    }


    @Test
    public void testContainsMatchingHeaderLowerCase() throws Exception
    {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("content-type", Arrays.asList("application/octet-stream"));
        headers.put("link", Arrays.asList("<test5>; rel=\"relation5\",<test6>; rel=\"relation6\"", "<test7>; rel=\"relation7\",<test8>; rel=\"relation8\""));

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);

        Headers testHeaders = new HttpUrlConnectionHeaders(connection);

        assertTrue(testHeaders.contains(HttpHeaders.LINK));
    }


    @Test
    public void testContainsMatchingHeaderUpperCase() throws Exception
    {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("content-type", Arrays.asList("application/octet-stream"));
        headers.put("LINK", Arrays.asList("<test5>; rel=\"relation5\",<test6>; rel=\"relation6\"", "<test7>; rel=\"relation7\",<test8>; rel=\"relation8\""));

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);

        Headers testHeaders = new HttpUrlConnectionHeaders(connection);

        assertTrue(testHeaders.contains(HttpHeaders.LINK));
    }


    @Test
    public void testContainsMatchingHeaderMixedCase() throws Exception
    {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("content-type", Arrays.asList("application/octet-stream"));
        headers.put("Link", Arrays.asList("<test5>; rel=\"relation5\",<test6>; rel=\"relation6\"", "<test7>; rel=\"relation7\",<test8>; rel=\"relation8\""));

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);

        Headers testHeaders = new HttpUrlConnectionHeaders(connection);

        assertTrue(testHeaders.contains(HttpHeaders.LINK));
    }


    @Test
    public void testContainsMultipleMatchingHeaders() throws Exception
    {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("content-type", Arrays.asList("application/octet-stream"));
        headers.put("link", Arrays.asList("<test5>; rel=\"relation5\",<test6>; rel=\"relation6\"", "<test7>; rel=\"relation7\",<test8>; rel=\"relation8\""));
        headers.put("Link", Arrays.asList("<test1>; rel=\"relation1\",<test2>; rel=\"relation2\"", "<test3>; rel=\"relation3\",<test4>; rel=\"relation4\""));

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);

        Headers testHeaders = new HttpUrlConnectionHeaders(connection);

        assertTrue(testHeaders.contains(HttpHeaders.LINK));
    }


    @Test
    public void testSingleHeaderLowerCase() throws Exception
    {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("content-type", Arrays.asList("application/octetstream"));

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);

        Headers testHeaders = new HttpUrlConnectionHeaders(connection);
        assertEquals(new StructuredMediaType("application", "octetstream"), testHeaders.header(HttpHeaders.CONTENT_TYPE).value());
    }


    @Test
    public void testSingleHeaderUpperCase() throws Exception
    {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("CONTENT-TYPE", Arrays.asList("application/octetstream"));

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);

        Headers testHeaders = new HttpUrlConnectionHeaders(connection);
        assertEquals(new StructuredMediaType("application", "octetstream"), testHeaders.header(HttpHeaders.CONTENT_TYPE).value());
    }


    @Test
    public void testSingleHeaderMixedCase() throws Exception
    {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Content-Type", Arrays.asList("application/octetstream"));

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);

        Headers testHeaders = new HttpUrlConnectionHeaders(connection);
        assertEquals(new StructuredMediaType("application", "octetstream"), testHeaders.header(HttpHeaders.CONTENT_TYPE).value());
    }


    @Test
    public void listHeader() throws Exception
    {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("content-type", Arrays.asList("application/octet-stream"));
        headers.put("Link", Arrays.asList("<test1>; rel=\"relation1\",<test2>; rel=\"relation2\"", "<test3>; rel=\"relation3\",<test4>; rel=\"relation4\""));

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getHeaderFields()).thenReturn(headers);

        Headers testHeaders = new HttpUrlConnectionHeaders(connection);

        List<Link> links = testHeaders.header(HttpHeaders.LINK).value();

        for (int i = 1, count = links.size(); i <= count; ++i)
        {
            assertEquals("test" + i, links.get(i - 1).target().toASCIIString());
        }
    }
}