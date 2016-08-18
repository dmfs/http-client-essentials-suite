/*
 *  Copyright (C) 2016 Marten Gajda <marten@dmfs.org>
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.dmfs.httpessentials.executors.useragent;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.types.CommentedProduct;
import org.dmfs.httpessentials.types.VersionedProduct;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertEquals;


/**
 * Functional test for {@link Branded}.
 *
 * @author Gabor Keszthelyi
 */
public class BrandedTest
{

    private class CapturingExecutor implements HttpRequestExecutor
    {

        private HttpRequest mCapturedRequest;


        @Override
        public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
        {
            mCapturedRequest = request;
            return null;
        }
    }


    private class EmptyRequest<T> implements HttpRequest<T>
    {
        @Override
        public HttpMethod method()
        {
            return null;
        }


        @Override
        public Headers headers()
        {
            return EmptyHeaders.INSTANCE;
        }


        @Override
        public HttpRequestEntity requestEntity()
        {
            return null;
        }


        @Override
        public HttpResponseHandler<T> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
        {
            return null;
        }
    }


    @Test
    public void test_userAgentHeaderGetsAdded() throws ProtocolException, ProtocolError, IOException
    {
        // ARRANGE
        CapturingExecutor capturingExecutor = new CapturingExecutor();
        Branded wrappedExecutor = new Branded(capturingExecutor, new CommentedProduct("name", "version", "comment"));

        // ACT
        wrappedExecutor.execute(null, new EmptyRequest<String>());

        // ASSERT
        String headerString = capturingExecutor.mCapturedRequest.headers()
                .header(UserAgentHeaderDecoration.USER_AGENT_HEADER)
                .toString();
        assertEquals("name/version (comment)", headerString);
    }


    @Test
    public void test_multipleAgentDecorators() throws ProtocolException, ProtocolError, IOException
    {
        // ARRANGE
        CapturingExecutor capturingExecutor = new CapturingExecutor();
        Branded smoothSyncExecutor = new Branded(capturingExecutor,
                new CommentedProduct("SmoothSync", "1.0", "debug"));
        Branded smoothSycnApiExecutor = new Branded(smoothSyncExecutor,
                new VersionedProduct("smoothsync-api-client", "0.4"));
        Branded oath2Executor = new Branded(smoothSycnApiExecutor, new VersionedProduct("oauth2-essentials", "0.3"));

        // ACT
        oath2Executor.execute(null, new EmptyRequest<String>());

        // ASSERT
        String headerString = capturingExecutor.mCapturedRequest.headers()
                .header(UserAgentHeaderDecoration.USER_AGENT_HEADER)
                .toString();
        assertEquals("SmoothSync/1.0 (debug) smoothsync-api-client/0.4 oauth2-essentials/0.3", headerString);
    }

}