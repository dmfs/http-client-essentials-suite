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
import org.dmfs.httpessentials.exceptions.TooManyRedirectsException;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URI;

import static org.junit.Assert.assertSame;


/**
 * Unit test for {@link FollowRedirectPolicy}.
 *
 * @author Gabor Keszthelyi
 */
@RunWith(JMockit.class)
public class FollowRedirectPolicyTest
{

    @Injectable
    private HttpResponse response;
    @Injectable
    private URI newLocation;
    @Injectable
    private URI redirectingLocation;
    @Injectable
    private URI expectedResult;


    @Test
    public void testLocation_whenRedirectNumberIsLowerThanMax_shouldReturnResolvedUriFromHeader() throws RedirectionException
    {
        // ARRANGE
        new HeaderReadingExpectations();
        new LocationResolvingExpectations();

        // ACT
        URI actualResult = new FollowRedirectPolicy(10).location(response, 1);

        // ASSERT
        assertSame(expectedResult, actualResult);
    }


    @Test
    public void testLocation_whenRedirectNumberIsEqualToMax_shouldReturnResolvedUriFromHeader() throws RedirectionException
    {
        // ARRANGE
        new HeaderReadingExpectations();
        new LocationResolvingExpectations();

        // ACT
        URI actualResult = new FollowRedirectPolicy(5).location(response, 5);

        // ASSERT
        assertSame(expectedResult, actualResult);
    }


    @Test(expected = TooManyRedirectsException.class)
    public void testLocation_whenRedirectNumberLowerThanMax_shouldThrowTooManyRedirectsException(@Mocked TooManyRedirectsException e) throws RedirectionException
    {
        // ARRANGE
        new HeaderReadingExpectations();
        new StrictExpectations()
        {{
            new TooManyRedirectsException(response.status(), 8, response.requestUri(), newLocation);
        }};

        // ACT
        new FollowRedirectPolicy(2).location(response, 8);
    }


    final class HeaderReadingExpectations extends StrictExpectations
    {
        {
            response.headers().header(HttpHeaders.LOCATION).value();
            result = newLocation;
        }
    }


    final class LocationResolvingExpectations extends StrictExpectations
    {
        {
            response.requestUri();
            result = redirectingLocation;

            redirectingLocation.resolve(newLocation);
            result = expectedResult;
        }
    }

}