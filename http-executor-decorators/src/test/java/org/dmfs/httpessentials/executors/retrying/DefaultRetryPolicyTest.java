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

import mockit.Expectations;
import mockit.Injectable;
import mockit.integration.junit4.JMockit;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.executors.retrying.policies.DefaultRetryPolicy;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Unit test for {@link DefaultRetryPolicy}.
 *
 * @author Gabor Keszthelyi
 */
@SuppressWarnings("ThrowableInstanceNeverThrown")
@RunWith(JMockit.class)
public class DefaultRetryPolicyTest
{
    private static final int MAX_NUMBER_OF_RETRIES = 5;
    private static final Exception EXCEPTION = new Exception();
    private static final IOException IO_EXCEPTION = new IOException();
    private static final SSLException SSL_EXCEPTION = new SSLException("");
    private static final SSLHandshakeException SSL_HANDSHAKE_EXCEPTION = new SSLHandshakeException("");

    private RetryPolicy policy = new DefaultRetryPolicy(MAX_NUMBER_OF_RETRIES);

    @Injectable
    private HttpRequest idempotentRequest;
    @Injectable
    private HttpRequest nonIdempotentRequest;


    @Test
    public void testShouldRetry_withVariousCombinations() throws Exception
    {
        new Expectations()
        {{ // @formatter:off
            idempotentRequest.method().isIdempotent(); result = true;
            nonIdempotentRequest.method().isIdempotent(); result = false;
        }}; // @formatter:on

        // Never retry for non-idempotent request
        assertFalse(policy.shouldRetry(nonIdempotentRequest, EXCEPTION, 1));
        assertFalse(policy.shouldRetry(nonIdempotentRequest, IO_EXCEPTION, MAX_NUMBER_OF_RETRIES - 1));
        assertFalse(policy.shouldRetry(nonIdempotentRequest, IO_EXCEPTION, MAX_NUMBER_OF_RETRIES));
        assertFalse(policy.shouldRetry(nonIdempotentRequest, IO_EXCEPTION, MAX_NUMBER_OF_RETRIES + 1));

        // Never retry for NOT IOException
        assertFalse(policy.shouldRetry(nonIdempotentRequest, EXCEPTION, 1));
        assertFalse(policy.shouldRetry(idempotentRequest, EXCEPTION, 1));
        assertFalse(policy.shouldRetry(nonIdempotentRequest, EXCEPTION, MAX_NUMBER_OF_RETRIES - 1));
        assertFalse(policy.shouldRetry(idempotentRequest, EXCEPTION, MAX_NUMBER_OF_RETRIES));
        assertFalse(policy.shouldRetry(nonIdempotentRequest, EXCEPTION, MAX_NUMBER_OF_RETRIES + 1));

        // Idempotent request, IOException, first retry should be true
        assertTrue(policy.shouldRetry(idempotentRequest, IO_EXCEPTION, 1));

        // Idempotent request, IOException, retry count less than max, should be true
        assertTrue(policy.shouldRetry(idempotentRequest, IO_EXCEPTION, MAX_NUMBER_OF_RETRIES - 1));

        // Idempotent request, IOException, retry count equals max, should be true
        assertTrue(policy.shouldRetry(idempotentRequest, IO_EXCEPTION, MAX_NUMBER_OF_RETRIES));

        // Idempotent request, IOException, retry count more than max, should be false
        assertFalse(policy.shouldRetry(idempotentRequest, IO_EXCEPTION, MAX_NUMBER_OF_RETRIES + 1));

        // Don't retry SSLException-s
        assertFalse(policy.shouldRetry(idempotentRequest, SSL_EXCEPTION, 1));
        assertFalse(policy.shouldRetry(idempotentRequest, SSL_HANDSHAKE_EXCEPTION, 1));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_negativeMaxNumber_shouldThrowException()
    {
        new DefaultRetryPolicy(-1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_zeroMaxNumber_shouldThrowException()
    {
        new DefaultRetryPolicy(0);
    }


    @Test(expected = NullPointerException.class)
    public void testShouldRetry_nullRequest_throwsNullPointerException()
    {
        policy.shouldRetry(null, IO_EXCEPTION, 1);
    }


    @Test(expected = NullPointerException.class)
    public void testShouldRetry_requestMethodIsNull_shouldThrowNullPointerException(@Injectable final HttpRequest request)
    {
        new Expectations()
        {{ // @formatter:off
            request.method(); result = null;
        }}; // @formatter:on

        policy.shouldRetry(request, IO_EXCEPTION, 1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testShouldRetry_exceptionIsNull_shouldThrowsNullPointerException()
    {
        policy.shouldRetry(idempotentRequest, null, 1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testShouldRetry_whenRetryCountIsZero_shouldThrowException()
    {
        policy.shouldRetry(idempotentRequest, IO_EXCEPTION, 0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testShouldRetry_whenRetryCountIsNegative_shouldThrowException()
    {
        policy.shouldRetry(idempotentRequest, IO_EXCEPTION, -1);
    }

}