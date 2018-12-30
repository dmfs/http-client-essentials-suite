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

package org.dmfs.httpessentials.apache4;

import org.apache.http.impl.client.DefaultHttpClient;
import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.converters.PlainStringHeaderConverter;
import org.dmfs.httpessentials.entities.EmptyHttpRequestEntity;
import org.dmfs.httpessentials.headers.BasicSingletonHeaderType;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.responsehandlers.StringResponseHandler;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.httpessentials.types.StringMediaType;
import org.dmfs.jems.optional.Optional;
import org.dmfs.jems.optional.elementary.Present;
import org.dmfs.jems.single.elementary.ValueSingle;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import static org.dmfs.jems.hamcrest.matchers.optional.PresentMatcher.present;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Test PlainApacheExecutor
 * <p>
 * For now we just run few simple tests against http://httpbin.org
 *
 * @author Marten Gajda
 */
public class ApacheExecutorTest
{
    @Test
    public void testGet() throws Exception
    {
        HttpRequestExecutor executor = new ApacheExecutor(new ValueSingle<>(new DefaultHttpClient()));
        String response = executor.execute(URI.create("http://httpbin.org/get"), new HttpRequest<String>()
        {
            @Override
            public HttpMethod method()
            {
                return HttpMethod.GET;
            }


            @Override
            public Headers headers()
            {
                return EmptyHeaders.INSTANCE;
            }


            @Override
            public HttpRequestEntity requestEntity()
            {
                return EmptyHttpRequestEntity.INSTANCE;
            }


            @Override
            public HttpResponseHandler<String> responseHandler(HttpResponse response)
            {
                assertThat(response.responseEntity().contentType(), is(present(new StringMediaType("application/json"))));
                return new StringResponseHandler();
            }
        });
        assertThat(response, CoreMatchers.containsString("\"headers\": {"));
    }


    @Test
    public void testPost() throws Exception
    {
        HttpRequestExecutor executor = new ApacheExecutor(new ValueSingle<>(new DefaultHttpClient()));
        String response = executor.execute(URI.create("http://httpbin.org/post"), new HttpRequest<String>()
        {
            @Override
            public HttpMethod method()
            {
                return HttpMethod.POST;
            }


            @Override
            public Headers headers()
            {
                return EmptyHeaders.INSTANCE;
            }


            @Override
            public HttpRequestEntity requestEntity()
            {
                return new HttpRequestEntity()
                {
                    @Override
                    public Optional<MediaType> contentType()
                    {
                        return new Present<>(new StringMediaType("text/plain"));
                    }


                    @Override
                    public Optional<Long> contentLength()
                    {
                        try
                        {
                            return new Present<>((long) "HelloWorld".getBytes("UTF-8").length);
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            throw new RuntimeException();
                        }
                    }


                    @Override
                    public void writeContent(OutputStream out) throws IOException
                    {
                        out.write("HelloWorld".getBytes("UTF-8"));
                    }
                };
            }


            @Override
            public HttpResponseHandler<String> responseHandler(HttpResponse response)
            {
                assertThat(response.responseEntity().contentType(), is(present(new StringMediaType("application/json"))));
                // header names should be case insensitive
                assertThat(response.headers().header(new BasicSingletonHeaderType<>("Content-type", PlainStringHeaderConverter.INSTANCE)).value(),
                        is("application/json"));
                assertThat(response.headers().header(new BasicSingletonHeaderType<>("content-TYPE", PlainStringHeaderConverter.INSTANCE)).value(),
                        is("application/json"));
                return new StringResponseHandler();
            }
        });
        assertThat(response, CoreMatchers.containsString("HelloWorld"));
    }

}