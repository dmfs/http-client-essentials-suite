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

package org.dmfs.httpessentials.executors.following.policies;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.exceptions.RedirectionException;
import org.dmfs.httpessentials.executors.following.RedirectPolicy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.assertSame;


/**
 * Unit test for {@link Secure}.
 *
 * @author Gabor Keszthelyi
 */
@RunWith(JMockit.class)
public class SecureTest
{

    @Injectable
    private RedirectPolicy decoratedPolicy;
    @Injectable
    private HttpResponse response;
    @Injectable
    private URI redirectingLocation;
    @Injectable
    private URI decoratorNewLocation;

    private Secure secure;


    @Before
    public void setup()
    {
        secure = new Secure(decoratedPolicy);
    }


    @Test
    public void testLocation_whenBothUrisAreHttps_shouldReturnDecoratedResult() throws RedirectionException, IOException
    {
        // ARRANGE
        new StrictExpectations()
        {{
            response.requestUri();
            result = redirectingLocation;

            decoratedPolicy.location(response, anyInt);
            result = decoratorNewLocation;

            redirectingLocation.getScheme();
            result = "https";

            decoratorNewLocation.getScheme();
            result = "https";
        }};

        // ACT
        URI actualResult = secure.location(response, 33);

        // ASSERT
        assertSame(decoratorNewLocation, actualResult);
    }


    @Test(expected = RedirectionException.class)
    public void testLocation_whenRequestUriIsNotHttps_shouldThrowException(@Mocked RedirectionException e) throws RedirectionException, IOException
    {
        // ARRANGE
        new StrictExpectations()
        {{
            response.requestUri();
            result = redirectingLocation;

            decoratedPolicy.location(response, anyInt);
            result = decoratorNewLocation;

            redirectingLocation.getScheme();
            result = "http";

            new RedirectionException(response.status(), "Not secure (HTTPS) redirect.", redirectingLocation,
                    decoratorNewLocation);
        }};

        // ACT
        secure.location(response, 33);
    }


    @Test(expected = RedirectionException.class)
    public void testLocation_whenNewLocationIsNotHttps_shouldThrowException(@Mocked RedirectionException e) throws RedirectionException, IOException
    {
        // ARRANGE
        new StrictExpectations()
        {{
            response.requestUri();
            result = redirectingLocation;

            decoratedPolicy.location(response, anyInt);
            result = decoratorNewLocation;

            redirectingLocation.getScheme();
            result = "https";

            decoratorNewLocation.getScheme();
            result = "http";

            new RedirectionException(response.status(), "Not secure (HTTPS) redirect.", redirectingLocation,
                    decoratorNewLocation);
        }};

        // ACT
        secure.location(response, 33);
    }


    @Test(expected = RedirectionException.class)
    public void testLocation_whenBothAreNotHttps_shouldThrowException(@Mocked RedirectionException e) throws RedirectionException, IOException
    {
        // ARRANGE
        new StrictExpectations()
        {{
            response.requestUri();
            result = redirectingLocation;

            decoratedPolicy.location(response, anyInt);
            result = decoratorNewLocation;

            redirectingLocation.getScheme();
            result = "http";

            decoratorNewLocation.getScheme();
            result = "http";
            minTimes = 0;

            new RedirectionException(response.status(), "Not secure (HTTPS) redirect.", redirectingLocation,
                    decoratorNewLocation);
        }};

        // ACT
        secure.location(response, 33);
    }

}