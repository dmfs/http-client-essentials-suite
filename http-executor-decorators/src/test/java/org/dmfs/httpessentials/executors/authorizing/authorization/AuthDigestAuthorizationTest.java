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

package org.dmfs.httpessentials.executors.authorizing.authorization;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.executors.authorizing.Authorization;
import org.dmfs.httpessentials.executors.authorizing.Parametrized;
import org.dmfs.httpessentials.executors.authorizing.Tokens;
import org.dmfs.httpessentials.executors.authorizing.UserCredentials;
import org.dmfs.httpessentials.executors.authorizing.charsequences.SingleCredentials;
import org.dmfs.httpessentials.types.Token;
import org.dmfs.jems.pair.Pair;
import org.dmfs.optional.Present;
import org.junit.Test;

import java.net.URI;

import static org.dmfs.optional.Absent.absent;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


/**
 * @author Marten Gajda
 */
public class AuthDigestAuthorizationTest
{
    @Test
    public void testParameters()
    {
        Parametrized mockChallenge = mock(Parametrized.class);
        doReturn(new Present<>("testrealm@host.com")).when(mockChallenge).parameter(Tokens.REALM);
        doReturn(new Present<>("dcd98b7102dd2f0e8b11d0f600bfb0c093")).when(mockChallenge).parameter(Tokens.NONCE);
        doReturn(new Present<>("5ccc069c403ebaf9f0171e9517f40e41")).when(mockChallenge).parameter(Tokens.OPAQUE);
        doReturn(new Present<>("MD5")).when(mockChallenge).parameter(Tokens.ALGORITHM);
        doReturn(absent()).when(mockChallenge).parameter(Tokens.USERHASH);

        UserCredentials mockUserCredentials = mock(UserCredentials.class);
        doReturn("Mufasa").when(mockUserCredentials).userName();
        doReturn("Circle Of Life").when(mockUserCredentials).password();

        Authorization authorization = new AuthDigestAuthorization(HttpMethod.GET, URI.create("/dir/index.html"), mockChallenge, mockUserCredentials, "0a4f113b",
                1);
        for (Pair<Token, CharSequence> param : authorization.parameters())
        {
            System.out.println(String.format("%s: %s", param.left(), param.right().toString()));
        }
        System.out.println(new SingleCredentials(authorization).value().toString());
    }


    @Test
    public void testParameters2()
    {
        Parametrized mockChallenge = mock(Parametrized.class);
        doReturn(new Present<>("http-auth@example.org")).when(mockChallenge).parameter(Tokens.REALM);
        doReturn(new Present<>("7ypf/xlj9XXwfDPEoM4URrv/xwf94BcCAzFZH4GiTo0v")).when(mockChallenge).parameter(Tokens.NONCE);
        doReturn(new Present<>("FQhe/qaU925kfnzjCev0ciny7QMkPqMAFRtzCUYo5tdS")).when(mockChallenge).parameter(Tokens.OPAQUE);
        doReturn(new Present<>("SHA-256")).when(mockChallenge).parameter(Tokens.ALGORITHM);
        doReturn(absent()).when(mockChallenge).parameter(Tokens.USERHASH);

        UserCredentials mockUserCredentials = mock(UserCredentials.class);
        doReturn("Mufasa").when(mockUserCredentials).userName();
        doReturn("Circle Of Life").when(mockUserCredentials).password();

        Authorization authorization = new AuthDigestAuthorization(HttpMethod.GET, URI.create("/dir/index.html"), mockChallenge, mockUserCredentials,
                "f2/wE4q74E6zIJEtWaHKaf5wv/H5QzzpXusqGemxURZJ", 1);
        for (Pair<Token, CharSequence> param : authorization.parameters())
        {
            System.out.println(String.format("%s: %s", param.left(), param.right().toString()));
        }
        System.out.println(new SingleCredentials(authorization).value().toString());
    }


    @Test
    public void testParameters3()
    {
        Parametrized mockChallenge = mock(Parametrized.class);
        doReturn(new Present<>("http-auth@example.org")).when(mockChallenge).parameter(Tokens.REALM);
        doReturn(new Present<>("7ypf/xlj9XXwfDPEoM4URrv/xwf94BcCAzFZH4GiTo0v")).when(mockChallenge).parameter(Tokens.NONCE);
        doReturn(new Present<>("FQhe/qaU925kfnzjCev0ciny7QMkPqMAFRtzCUYo5tdS")).when(mockChallenge).parameter(Tokens.OPAQUE);
        doReturn(new Present<>("MD5")).when(mockChallenge).parameter(Tokens.ALGORITHM);
        doReturn(absent()).when(mockChallenge).parameter(Tokens.USERHASH);

        UserCredentials mockUserCredentials = mock(UserCredentials.class);
        doReturn("Mufasa").when(mockUserCredentials).userName();
        doReturn("Circle Of Life").when(mockUserCredentials).password();

        Authorization authorization = new AuthDigestAuthorization(HttpMethod.GET, URI.create("/dir/index.html"), mockChallenge, mockUserCredentials,
                "f2/wE4q74E6zIJEtWaHKaf5wv/H5QzzpXusqGemxURZJ", 1);
        for (Pair<Token, CharSequence> param : authorization.parameters())
        {
            System.out.println(String.format("%s: %s", param.left(), param.right().toString()));
        }
        System.out.println(new SingleCredentials(authorization).value().toString());
    }


    @Test
    public void testParameters4()
    {
        Parametrized mockChallenge = mock(Parametrized.class);
        doReturn(new Present<>("http-auth@example.org")).when(mockChallenge).parameter(Tokens.REALM);
        doReturn(new Present<>("7ypf/xlj9XXwfDPEoM4URrv/xwf94BcCAzFZH4GiTo0v")).when(mockChallenge).parameter(Tokens.NONCE);
        doReturn(new Present<>("FQhe/qaU925kfnzjCev0ciny7QMkPqMAFRtzCUYo5tdS")).when(mockChallenge).parameter(Tokens.OPAQUE);
        doReturn(new Present<>("SHA-256")).when(mockChallenge).parameter(Tokens.ALGORITHM);
        doReturn(new Present<>("true")).when(mockChallenge).parameter(Tokens.USERHASH);

        UserCredentials mockUserCredentials = mock(UserCredentials.class);
        doReturn("Mufasa").when(mockUserCredentials).userName();
        doReturn("Circle Of Life").when(mockUserCredentials).password();

        Authorization authorization = new AuthDigestAuthorization(HttpMethod.GET, URI.create("/dir/index.html"), mockChallenge, mockUserCredentials,
                "f2/wE4q74E6zIJEtWaHKaf5wv/H5QzzpXusqGemxURZJ", 1);
        for (Pair<Token, CharSequence> param : authorization.parameters())
        {
            System.out.println(String.format("%s: %s", param.left(), param.right().toString()));
        }
        System.out.println(new SingleCredentials(authorization).value().toString());
    }
}