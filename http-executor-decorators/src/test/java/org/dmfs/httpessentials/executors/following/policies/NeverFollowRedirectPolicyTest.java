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

package org.dmfs.httpessentials.executors.following.policies;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URI;


/**
 * Unit test for {@link NeverFollowRedirectPolicy}.
 *
 * @author Gabor Keszthelyi
 */
@RunWith(JMockit.class)
public class NeverFollowRedirectPolicyTest
{

    private NeverFollowRedirectPolicy underTest = new NeverFollowRedirectPolicy();

    @Injectable
    private HttpResponse response;

    @Injectable
    private URI newLocation;


    @Test(expected = RedirectionException.class)
    public void testLocation_shouldAlwaysThrowException(@Mocked RedirectionException e) throws RedirectionException
    {
        // ARRANGE
        new StrictExpectations()
        {{
            response.headers().header(HttpHeaders.LOCATION).value();
            result = newLocation;

            new RedirectionException(response.status(), response.requestUri(), newLocation);
        }};

        // ACT
        underTest.location(response, 1);
    }

}