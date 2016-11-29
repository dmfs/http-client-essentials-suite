/*
 * Copyright 2016 dmfs GmbH
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

package org.dmfs.httpessentials.executors.retrying;

import mockit.Injectable;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;


/**
 * Unit test for {@link HttpRequestExecutor}.
 *
 * @author Gabor Keszthelyi
 */
@SuppressWarnings("ThrowableInstanceNeverThrown")
@RunWith(JMockit.class)
public class RetryingExecutorTest
{

    private static final URI URRI = URI.create("http://google.com");
    private static final String RESULT = "result object";
    private static final IOException IO_EXCEPTION = new IOException();
    private static final IOException IO_EXCEPTION_2 = new IOException("2");

    @Injectable
    private HttpRequest<String> request;
    @Injectable
    private RetryPolicy retryPolicy;
    @Injectable
    private HttpRequestExecutor decoratedExecutor;

    private Retrying retryingExecutor;


    @Before
    public void setUp()
    {
        retryingExecutor = new Retrying(decoratedExecutor, retryPolicy);
    }


    @Test
    public void testExecute_whenDecoratedExecutorDoesNotThrowException_shouldNotRetry_andJustReturnTheResult() throws Exception
    {
        new StrictExpectations()
        {{ // @formatter:off
            decoratedExecutor.execute(URRI, request); result = RESULT;
        }}; // @formatter:on

        String result = retryingExecutor.execute(URRI, request);

        assertEquals(RESULT, result);
    }


    @Test
    public void testExecute_whenDecoratedExecutorAlwaysThrows_shouldRetryAccordingToPolicy_andRethrowInTheEnd() throws Exception
    {
        new StrictExpectations()
        {{ // @formatter:off
            decoratedExecutor.execute(URRI, request); result = IO_EXCEPTION;
            retryPolicy.shouldRetry(request, IO_EXCEPTION, 1); result = true;
            decoratedExecutor.execute(URRI, request); result = IO_EXCEPTION;
            retryPolicy.shouldRetry(request, IO_EXCEPTION, 2); result = true;
            decoratedExecutor.execute(URRI, request); result = IO_EXCEPTION_2;
            retryPolicy.shouldRetry(request, IO_EXCEPTION_2, 3); result = false;
        }}; // @formatter:on

        try
        {
            retryingExecutor.execute(URRI, request);
            fail();
        }
        catch (Exception e)
        {
            assertSame(IO_EXCEPTION_2, e);
        }
    }


    @Test
    public void testExecute_whenDecoratedExecutorThrowsButThenSucceeds_shouldRetryAndReturnResultInTheEnd() throws Exception
    {
        new StrictExpectations()
        {{ // @formatter:off
            decoratedExecutor.execute(URRI, request); result = IO_EXCEPTION;
            retryPolicy.shouldRetry(request, IO_EXCEPTION, 1); result = true;
            decoratedExecutor.execute(URRI, request); result = IO_EXCEPTION;
            retryPolicy.shouldRetry(request, IO_EXCEPTION, 2); result = true;
            decoratedExecutor.execute(URRI, request); result = RESULT;
        }}; // @formatter:on

        String result = retryingExecutor.execute(URRI, request);

        assertEquals(RESULT, result);
    }

}