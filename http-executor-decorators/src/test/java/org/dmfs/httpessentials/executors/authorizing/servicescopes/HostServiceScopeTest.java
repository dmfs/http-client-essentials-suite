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

package org.dmfs.httpessentials.executors.authorizing.servicescopes;

import org.dmfs.httpessentials.executors.authorizing.authscopes.UriScope;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * @author Marten Gajda
 */
public class HostServiceScopeTest
{
    @Test
    public void testContains()
    {
        assertThat(new HostServiceScope(URI.create("https://example.com/")).contains(new UriScope(URI.create("https://example.com/path/xyz"))), is(true));
        assertThat(new HostServiceScope(URI.create("https://example.com/")).contains(new UriScope(URI.create("http://example.com/path/xyz"))), is(false));
        assertThat(new HostServiceScope(URI.create("https://example.com/")).contains(new UriScope(URI.create("https://example.org/path/xyz"))), is(false));
        assertThat(new HostServiceScope(URI.create("https://example.com/")).contains(new UriScope(URI.create("https://example.com:8443/path/xyz"))), is(false));
        assertThat(new HostServiceScope(URI.create("http://example.com/")).contains(new UriScope(URI.create("https://example.com/path/xyz"))), is(false));
        assertThat(new HostServiceScope(URI.create("https://example.com:8443/")).contains(new UriScope(URI.create("https://example.com/path/xyz"))), is(false));
        assertThat(new HostServiceScope(URI.create("https://example.org/")).contains(new UriScope(URI.create("https://example.com/path/xyz"))), is(false));
    }

}