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

package org.dmfs.httpessentials.executors.retrying;

import org.dmfs.httpessentials.client.HttpRequest;


/**
 * Defines rules for when to retry a failed http request.
 *
 * @author Gabor Keszthelyi
 */
public interface RetryPolicy
{

    /**
     * Tells whether the failed request should be retried or not.
     *
     * @param request
     *         The request that failed. Not null.
     * @param exception
     *         The exception that was thrown. Not null.
     * @param numberOfThisRetryChance
     *         Which number of retry it is. Positive integer.
     *
     * @return true if the request should be retried
     */
    boolean shouldRetry(HttpRequest<?> request, Exception exception, int numberOfThisRetryChance);
}
