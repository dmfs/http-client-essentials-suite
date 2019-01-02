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

package org.dmfs.httpessentials.executors.authorizing.authstrategies;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authorizing.AuthScope;
import org.dmfs.httpessentials.executors.authorizing.AuthState;
import org.dmfs.httpessentials.executors.authorizing.Challenge;
import org.dmfs.httpessentials.executors.authorizing.CredentialsStore;
import org.dmfs.httpessentials.executors.authorizing.UserCredentials;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.jems.optional.elementary.Present;
import org.dmfs.jems.pair.Pair;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.net.URI;

import static org.dmfs.jems.mockito.doubles.TestDoubles.dummy;
import static org.dmfs.jems.mockito.doubles.TestDoubles.failingMock;
import static org.dmfs.jems.optional.elementary.Absent.absent;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;


/**
 * @author Marten Gajda
 */
public class PrematureBasicAuthStrategyTest
{
    /**
     * Test that the AuthStrategy delegates to the fallback if the request can't be authorized due to missing credentials.
     */
    @Test
    public void testFallbackAuthState()
    {
        final URI mockUri = URI.create("https://example.com");
        AuthState mockAuthState = failingMock(AuthState.class);
        CredentialsStore<UserCredentials> mockStore = failingMock(CredentialsStore.class);
        doReturn(absent()).when(mockStore).credentials(ArgumentMatchers.any(AuthScope.class));
        assertThat(new PrematureBasicAuthStrategy(mockStore).authState(dummy(HttpMethod.class), mockUri, mockAuthState), sameInstance(mockAuthState));

        // also check that it actually tried to get credentials from our mock store
        verify(mockStore).credentials(argThat(argument -> argument.uri() == mockUri && !argument.realm().isPresent()));
    }


    /**
     * Test regular authorization with credentials.
     */
    @Test
    public void testAuthState() throws Exception
    {
        final URI mockUri = URI.create("https://example.com");
        AuthState mockAuthState = failingMock(AuthState.class);
        AuthState mockNextAuthState = failingMock(AuthState.class);
        Iterable<Challenge> mockChallenges = failingMock(Iterable.class);
        doReturn(mockNextAuthState).when(mockAuthState).withChallenges(mockChallenges);

        UserCredentials mockCredentials = failingMock(UserCredentials.class);
        doReturn("x").when(mockCredentials).userName();
        doReturn("y").when(mockCredentials).password();

        CredentialsStore<UserCredentials> mockStore = failingMock(CredentialsStore.class);
        doReturn(new Present<>(mockCredentials)).when(mockStore).credentials(argThat(argument -> argument.uri() == mockUri && !argument.realm().isPresent()));

        // get an AuthState from the strategy
        AuthState result = new PrematureBasicAuthStrategy(mockStore).authState(dummy(HttpMethod.class), mockUri, mockAuthState);

        // AuthState should fall back to the fallback state if presented with challenges
        assertThat(result.withChallenges(mockChallenges), sameInstance(mockNextAuthState));
        // It should return a Basic Authorization with the given credentials
        assertThat(result.authorization().value().scheme().toString(), is("Basic"));
        assertThat(result.authorization().value().parameters(), Matchers.<Pair<Token, CharSequence>>emptyIterable());
        assertThat(result.authorization().value().token().value().toString(), is("eDp5"));
        // it should also return a prematureAuthStrategy which behaves exactly the same
        AuthState prematureAuth = result.prematureAuthStrategy(absent()).authState(dummy(HttpMethod.class), mockUri, mockAuthState);
        assertThat(prematureAuth.withChallenges(mockChallenges), sameInstance(mockNextAuthState));
        assertThat(prematureAuth.authorization().value().scheme().toString(), is("Basic"));
        assertThat(prematureAuth.authorization().value().parameters(), Matchers.<Pair<Token, CharSequence>>emptyIterable());
        assertThat(prematureAuth.authorization().value().token().value().toString(), is("eDp5"));
    }

}