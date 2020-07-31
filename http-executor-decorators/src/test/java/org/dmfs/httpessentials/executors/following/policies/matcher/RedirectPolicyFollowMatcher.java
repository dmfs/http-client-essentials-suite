/*
 * Copyright 2019 dmfs GmbH
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

package org.dmfs.httpessentials.executors.following.policies.matcher;

import org.dmfs.httpessentials.HttpStatus;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseEntity;
import org.dmfs.httpessentials.executors.following.RedirectPolicy;
import org.dmfs.httpessentials.headers.SingletonHeaders;
import org.dmfs.httpessentials.mockutils.responses.CustomUrisMockResponse;
import org.dmfs.httpessentials.mockutils.responses.StaticMockResponse;
import org.dmfs.httpessentials.types.MediaType;
import org.dmfs.jems.optional.Optional;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.dmfs.httpessentials.headers.HttpHeaders.LOCATION;
import static org.dmfs.jems.optional.elementary.Absent.absent;


/**
 * @author Marten Gajda
 */
public final class RedirectPolicyFollowMatcher extends TypeSafeDiagnosingMatcher<RedirectPolicy>
{
    private final HttpResponse mResponse;
    private final URI mDestination;
    private final int mAttempt;


    public static Matcher<RedirectPolicy> follows(HttpResponse response, URI destination, int attempt)
    {
        return new RedirectPolicyFollowMatcher(response, destination, attempt);
    }


    // TODO: replace destination URI with UriMatcher
    public RedirectPolicyFollowMatcher(HttpResponse response, URI destination, int attempt)
    {
        mResponse = response;
        mDestination = destination;
        mAttempt = attempt;
    }


    @Override
    protected boolean matchesSafely(RedirectPolicy item, Description mismatchDescription)
    {
        if (!item.affects(mResponse))
        {
            mismatchDescription.appendText(
                    String.format("Did not respond to redirect %s, from %s to %s", mResponse.status().reason(), mResponse.requestUri(), mDestination));
            return false;
        }
        try
        {
            if (!mDestination.toString().equals(item.location(mResponse, mAttempt).toString()))
            {
                mismatchDescription.appendText(String.format("Redirected to destination %s", item.location(mResponse, mAttempt)));
                return false;
            }
        }
        catch (Exception e)
        {
            mismatchDescription.appendText(String.format("Threw %s, with message %s", e.getClass(), e.getMessage()));
            return false;
        }
        return true;
    }


    @Override
    public void describeTo(Description description)
    {
        description.appendText("Redirects attempt ")
                .appendText(String.valueOf(mAttempt))
                .appendText(" of ")
                .appendText(mResponse.status().reason())
                .appendText(" from ")
                .appendText(mResponse.requestUri().toString())
                .appendText(" to ")
                .appendText(mDestination.toString());
    }
}
