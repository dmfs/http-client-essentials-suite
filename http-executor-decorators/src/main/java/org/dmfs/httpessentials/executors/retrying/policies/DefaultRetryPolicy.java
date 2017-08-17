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

package org.dmfs.httpessentials.executors.retrying.policies;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.executors.retrying.RetryPolicy;

import javax.net.ssl.SSLException;
import java.io.IOException;


/**
 * A standard retry policy that will retry all idempotent requests that failed with an IOException, except SSLExceptions, for the maximum number of times it is
 * configured to.
 *
 * @author Gabor Keszthelyi
 */
public final class DefaultRetryPolicy implements RetryPolicy
{

    private final int mMaxNumberOfRetries;


    /**
     * Constructor.
     *
     * @param maxNumberOfRetries
     *         how many times the retry should be tried before letting the call fail
     */
    public DefaultRetryPolicy(int maxNumberOfRetries)
    {
        if (maxNumberOfRetries <= 0)
        {
            throw new IllegalArgumentException("Max number of retries must be positive.");
        }
        mMaxNumberOfRetries = maxNumberOfRetries;
    }


    @Override
    public boolean shouldRetry(HttpRequest<?> request, Exception exception, int numberOfThisRetryChance)
    {
        if (numberOfThisRetryChance <= 0)
        {
            throw new IllegalArgumentException("The number of this retry chance must be positive.");
        }
        if (exception == null)
        {
            throw new IllegalArgumentException("Exception must not be null.");
        }

        return request.method().isIdempotent()
                && isExceptionToRetry(exception)
                && numberOfThisRetryChance <= mMaxNumberOfRetries;
    }


    private boolean isExceptionToRetry(Exception exception)
    {
        return exception instanceof IOException && !(exception instanceof SSLException);
    }
}
