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

package org.dmfs.httpessentials.executors.authorizing;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.entities.EmptyHttpRequestEntity;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.executors.authorizing.strategies.Composite;
import org.dmfs.httpessentials.executors.authorizing.strategies.UserCredentialsAuthStrategy;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.httpurlconnection.HttpUrlConnectionExecutor;
import org.dmfs.httpessentials.responsehandlers.StringResponseHandler;
import org.dmfs.optional.Present;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;


public class AuthorizingTest
{
    @Test
    public void testExecuteDigest() throws Exception
    {
        HttpRequestExecutor executor = new Authorizing(new HttpUrlConnectionExecutor(), new Composite(new UserCredentialsAuthStrategy(
                authScope -> new Present<>(new UserCredentials()
                {
                    @Override
                    public CharSequence userName()
                    {
                        return "user";
                    }


                    @Override
                    public CharSequence password()
                    {
                        return "passwd";
                    }
                }))));

        String response = executor.execute(URI.create("http://localhost:5000/digest-auth/auth/user/passwd/MD5/never"), new StringHttpRequest());
        response = executor.execute(URI.create("http://localhost:5000/digest-auth/auth/user/passwd/MD5/never"), new StringHttpRequest());
        response = executor.execute(URI.create("http://localhost:5000/digest-auth/auth/user/passwd/MD5/never"), new StringHttpRequest());

        System.out.println(response);
    }


    @Test
    public void testExecuteBasic() throws Exception
    {
        HttpRequestExecutor executor = new Authorizing(new HttpUrlConnectionExecutor(), new Composite(new UserCredentialsAuthStrategy(
                authScope -> new Present<>(new UserCredentials()
                {
                    @Override
                    public CharSequence userName()
                    {
                        return "user";
                    }


                    @Override
                    public CharSequence password()
                    {
                        return "passwd";
                    }
                }))));

        String response = executor.execute(URI.create("http://localhost:5000/basic-auth/user/passwd"), new StringHttpRequest());
        response = executor.execute(URI.create("http://localhost:5000/basic-auth/user/passwd"), new StringHttpRequest());
        response = executor.execute(URI.create("http://localhost:5000/basic-auth/user/passwd"), new StringHttpRequest());

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
            return new StringResponseHandler();
        }
    }
}