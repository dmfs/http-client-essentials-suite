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

import org.dmfs.httpessentials.HttpStatus;
import org.junit.Test;

import java.net.URI;

import static org.dmfs.httpessentials.executors.following.policies.matcher.RedirectPolicyFollowMatcher.follows;
import static org.dmfs.httpessentials.executors.following.strategies.matcher.RedirectStrategyFollowMatcher.mockRedirect;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;


/**
 * Unit test for {@link FollowRedirectPolicy}.
 *
 * @author Marten Gajda
 */
public class FollowRedirectPolicyTest
{

    @Test
    public void testFollow()
    {
        assertThat(new FollowRedirectPolicy(),
                follows(
                        mockRedirect(
                                HttpStatus.PERMANENT_REDIRECT,
                                URI.create("http://example.com/1"),
                                URI.create("http://example.com/2")),
                        URI.create("http://example.com/2"),
                        1
                ));
        assertThat(new FollowRedirectPolicy(),
                follows(
                        mockRedirect(
                                HttpStatus.TEMPORARY_REDIRECT,
                                URI.create("http://example.com/1"),
                                URI.create("http://example.com/2")),
                        URI.create("http://example.com/2"),
                        2
                ));
        assertThat(new FollowPolicy(),
                follows(
                        mockRedirect(
                                HttpStatus.FOUND,
                                URI.create("http://example.com/1"),
                                URI.create("http://example.com/2")),
                        URI.create("http://example.com/2"),
                        3
                ));
        assertThat(new FollowRedirectPolicy(),
                follows(
                        mockRedirect(
                                HttpStatus.SEE_OTHER,
                                URI.create("http://example.com/1"),
                                URI.create("http://example.com/2")),
                        URI.create("http://example.com/2"),
                        4
                ));
        assertThat(new FollowRedirectPolicy(),
                follows(
                        mockRedirect(
                                HttpStatus.MOVED_PERMANENTLY,
                                URI.create("http://example.com/1"),
                                URI.create("http://example.com/2")),
                        URI.create("http://example.com/2"),
                        5
                ));
    }


    @Test
    public void testNoFollow()
    {
        assertThat(new FollowRedirectPolicy(),
                not(follows(
                        mockRedirect(
                                HttpStatus.OK,
                                URI.create("http://example.com/1"),
                                URI.create("http://example.com/2")),
                        URI.create("http://example.com/2"),
                        1
                )));
        assertThat(new FollowRedirectPolicy(),
                not(follows(
                        mockRedirect(
                                HttpStatus.MOVED_PERMANENTLY,
                                URI.create("http://example.com/1"),
                                URI.create("http://example.com/2")),
                        URI.create("http://example.com/2"),
                        6
                )));
    }
}