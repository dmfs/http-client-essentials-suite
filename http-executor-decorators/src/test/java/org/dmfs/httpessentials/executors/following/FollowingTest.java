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

package org.dmfs.httpessentials.executors.following;

import mockit.Injectable;
import mockit.StrictExpectations;
import mockit.Verifications;
import mockit.VerificationsInOrder;
import mockit.integration.junit4.JMockit;
import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.exceptions.RedirectionLoopException;
import org.dmfs.httpessentials.exceptions.UnexpectedStatusException;
import org.hamcrest.CustomMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertSame;


/**
 * Functional test for {@link Following}.
 *
 * @author Gabor Keszthelyi
 */
@RunWith(JMockit.class)
public class FollowingTest
{

    private static final URI ORIGINAL_URI = URI.create("originaluri");
    private static final URI REDIRECT_URI_1 = URI.create("redirecturi1");
    private static final URI REDIRECT_URI_2 = URI.create("redirecturi2");
    private static final URI REDIRECT_URI_3 = URI.create("redirecturi3");

    @Injectable
    private RedirectPolicy redirectPolicy;
    @Injectable
    private HttpRequest<Result> originalRequest;
    @Injectable
    private HttpResponse response;
    @Injectable
    private HttpResponse response2;
    @Injectable
    private HttpResponseHandler originalResponseHandler;

    private Result expectedResult;

    private FakeExecutor fakeExecutor;

    private Following following;


    @Before
    public void setup()
    {
        fakeExecutor = new FakeExecutor();
        following = new Following(fakeExecutor, redirectPolicy);
    }


    @Test
    public void testExecute_whenResponseIsNotRedirect_handleItWithOriginalResponseHandler() throws ProtocolException, ProtocolError, IOException
    {
        new StrictExpectations(fakeExecutor)
        {{ // @formatter:off
            response.status(); result = HttpStatus.ACCEPTED;

            originalRequest.responseHandler(withAny(response));

            originalResponseHandler.handleResponse(withAny(response)); result = expectedResult;
        }}; // @formatter:on

        Result actualResult = following.execute(ORIGINAL_URI, originalRequest);

        assertSame(expectedResult, actualResult);
        new Verifications()
        {{
            fakeExecutor.execute(ORIGINAL_URI, withAny(originalRequest));
        }};
    }


    @Test
    public void testExecute_whenResponseIsRedirect_andPolicyTellsFollow_shouldCallExecutorAgain() throws ProtocolException, ProtocolError, IOException
    {
        new StrictExpectations(fakeExecutor)
        {{ // @formatter:off
            response.status(); result = HttpStatus.TEMPORARY_REDIRECT;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 1); result = REDIRECT_URI_1;

            response.status(); result = HttpStatus.ACCEPTED;

            originalRequest.responseHandler(withAny(response)); result = originalResponseHandler;
            originalResponseHandler.handleResponse(withAny(response)); result = expectedResult;
        }}; // @formatter:on

        Result actualResult = following.execute(ORIGINAL_URI, originalRequest);

        assertSame(expectedResult, actualResult);
        new VerificationsInOrder()
        {{
            fakeExecutor.execute(ORIGINAL_URI, withAny(originalRequest));
            fakeExecutor.execute(REDIRECT_URI_1, withAny(originalRequest));
        }};
    }


    @Test
    public void testExecute_whenFirstThreeResponsesAreRedirect_andPolicyTellsFollow_shouldCallExecutorAgain() throws ProtocolException, ProtocolError, IOException
    {
        new StrictExpectations(fakeExecutor)
        {{ // @formatter:off
            response.status(); result = HttpStatus.MOVED_PERMANENTLY;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 1); result = REDIRECT_URI_1;

            response.status(); result = HttpStatus.PERMANENT_REDIRECT;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 2); result = REDIRECT_URI_2;

            response.status(); result = HttpStatus.FOUND;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 3); result = REDIRECT_URI_3;

            response.status(); result = HttpStatus.ACCEPTED;

            originalRequest.responseHandler(withArgThat(new UriMatcher(ORIGINAL_URI))); result = originalResponseHandler;

            originalResponseHandler.handleResponse(withArgThat(new UriMatcher(ORIGINAL_URI))); result = expectedResult;
        }}; // @formatter:on

        Result actualResult = following.execute(ORIGINAL_URI, originalRequest);

        assertSame(expectedResult, actualResult);
        new VerificationsInOrder()
        {{
            fakeExecutor.execute(ORIGINAL_URI, withAny(originalRequest));
            fakeExecutor.execute(REDIRECT_URI_1, withAny(originalRequest));
            fakeExecutor.execute(REDIRECT_URI_2, withAny(originalRequest));
            fakeExecutor.execute(REDIRECT_URI_3, withAny(originalRequest));
        }};
    }


    @Test(expected = RedirectionException.class)
    public void testExecute_whenResponseIsRedirect_andPolicyThrowsException_shouldPropagateIt() throws ProtocolException, ProtocolError, IOException
    {
        new StrictExpectations(fakeExecutor)
        {{ // @formatter:off
            response.status(); result = HttpStatus.SEE_OTHER;
            response.responseEntity().contentStream().close();

            redirectPolicy.location(response, 1);
            result = new RedirectionException(HttpStatus.TEMPORARY_REDIRECT, withAny(ORIGINAL_URI), withAny(ORIGINAL_URI));
        }}; // @formatter:on

        following.execute(ORIGINAL_URI, originalRequest);
    }


    @Test(expected = RedirectionException.class)
    public void testExecute_whenMultipleResponsesAreRedirects_andPolicyThrowsExceptionOnlyLater_shouldPropagateIt() throws ProtocolException, ProtocolError, IOException
    {
        new StrictExpectations(fakeExecutor)
        {{ // @formatter:off
            response.status(); result = HttpStatus.TEMPORARY_REDIRECT;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 1); result = REDIRECT_URI_1;

            response.status(); result = HttpStatus.PERMANENT_REDIRECT;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 2); result = REDIRECT_URI_2;

            response.status(); result = HttpStatus.FOUND;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 3); result = REDIRECT_URI_3;

            response.status(); result = HttpStatus.TEMPORARY_REDIRECT;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 4);
            result = new RedirectionException(HttpStatus.TEMPORARY_REDIRECT, withAny(ORIGINAL_URI), withAny(ORIGINAL_URI));
        }}; // @formatter:on

        following.execute(ORIGINAL_URI, originalRequest);
    }


    @Test(expected = RedirectionLoopException.class)
    public void testExecute_whenTwoRedirectLocationIsTheSame_shouldThrowRedirectionLoopException() throws IOException, ProtocolException, ProtocolError
    {
        new StrictExpectations(fakeExecutor)
        {{ // @formatter:off
            response.status(); result = HttpStatus.TEMPORARY_REDIRECT;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 1); result = REDIRECT_URI_1;

            response.status(); result = HttpStatus.PERMANENT_REDIRECT;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 2); result = REDIRECT_URI_2;

            response.status(); result = HttpStatus.FOUND;
            response.responseEntity().contentStream().close();
            redirectPolicy.location(response, 3); result = REDIRECT_URI_1;

            new RedirectionException(response.status(), response.requestUri(), REDIRECT_URI_1);
        }}; // @formatter:on

        following.execute(ORIGINAL_URI, originalRequest);
    }


    private class FakeExecutor implements HttpRequestExecutor
    {

        @Override
        public <T> T execute(URI uri, HttpRequest<T> request) throws IOException, ProtocolError, ProtocolException, RedirectionException, UnexpectedStatusException
        {
            HttpResponseHandler<T> responseHandler = request.responseHandler(response);
            return responseHandler.handleResponse(response);
        }
    }


    private class Result
    {
    }


    private class UriMatcher extends CustomMatcher<HttpResponse>
    {

        private final URI mRequestUriToMatch;


        public UriMatcher(URI requestUriToMatch)
        {
            super("uri matcher");
            mRequestUriToMatch = requestUriToMatch;
        }


        @Override
        public boolean matches(Object o)
        {
            return ((HttpResponse) o).requestUri().equals(mRequestUriToMatch);
        }
    }

}