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
 * @author Marten Gajda
 */
public class TemporaryTest
{

    @Test
    public void testFollow()
    {
        assertThat(new Temporary(new FollowPolicy()),
                follows(
                        mockRedirect(
                                HttpStatus.TEMPORARY_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("/new")),
                        URI.create("https://example.com/new"),
                        1));
        assertThat(new Temporary(new FollowPolicy()),
                follows(
                        mockRedirect(
                                HttpStatus.FOUND,
                                URI.create("https://example.com/test"),
                                URI.create("/new")),
                        URI.create("https://example.com/new"),
                        1));
    }


    @Test
    public void testNoFollow()
    {
        assertThat(new Temporary(new NeverFollowRedirectPolicy()),
                not(follows(
                        mockRedirect(
                                HttpStatus.TEMPORARY_REDIRECT,
                                URI.create("https://example.com/test"),
                                URI.create("/new")),
                        URI.create("https://example.com/new"),
                        1)));
        assertThat(new Temporary(new FollowPolicy()),
                not(follows(
                        mockRedirect(
                                HttpStatus.MOVED_PERMANENTLY,
                                URI.create("https://example.com/test"),
                                URI.create("/new")),
                        URI.create("https://example.com/new"),
                        1)));
        assertThat(new Temporary(new FollowPolicy()),
                not(follows(
                        mockRedirect(
                                HttpStatus.MOVED_PERMANENTLY,
                                URI.create("https://example.com/test"),
                                URI.create("/new")),
                        URI.create("https://example.com/new"),
                        1)));
    }

}