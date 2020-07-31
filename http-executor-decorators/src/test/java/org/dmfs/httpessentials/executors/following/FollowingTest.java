/*
 * Copyright 2020 dmfs GmbH
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

package org.dmfs.httpessentials.executors.following;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.entities.EmptyHttpRequestEntity;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.executors.following.strategies.FollowStrategy;
import org.dmfs.httpessentials.executors.following.strategies.Limited;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.httpurlconnection.HttpUrlConnectionExecutor;
import org.dmfs.httpessentials.responsehandlers.FailResponseHandler;
import org.dmfs.httpessentials.responsehandlers.StringResponseHandler;
import org.junit.Test;

import java.net.URI;


/**
 * Unit test for {@link Following}.
 *
 * @author marten
 */
public class FollowingTest
{


    @Test
    public void testDirect() throws Exception
    {
        HttpRequestExecutor executor = new Following(new FollowStrategy(), new HttpUrlConnectionExecutor());

        String response = executor.execute(URI.create("http://localhost:5000/get"), new StringHttpRequest());

        System.out.println(response);
    }



    @Test
    public void testExecuteAbsolute4() throws Exception
    {
        HttpRequestExecutor executor = new Following(new FollowStrategy(), new HttpUrlConnectionExecutor());

        String response = executor.execute(URI.create("http://localhost:5000/absolute-redirect/4"), new StringHttpRequest());

        System.out.println(response);
    }


    @Test
    public void testExecuteRelative4() throws Exception
    {
        HttpRequestExecutor executor = new Following(new FollowStrategy(), new HttpUrlConnectionExecutor());

        String response = executor.execute(URI.create("http://localhost:5000/relative-redirect/4"), new StringHttpRequest());

        System.out.println(response);
    }


    @Test(expected = UnexpectedStatusException.class)
    public void testExecuteLimitedRelative4() throws Exception
    {
        HttpRequestExecutor executor = new Following(new Limited(3, new FollowStrategy()), new HttpUrlConnectionExecutor());

        String response = executor.execute(URI.create("http://localhost:5000/redirect/6"), new StringHttpRequest());

        System.out.println(response);
    }


    private static class StringHttpRequest implements HttpRequest<String>
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
            return response.status().isSuccess() ? new StringResponseHandler() : new FailResponseHandler<>();
        }
    }
}