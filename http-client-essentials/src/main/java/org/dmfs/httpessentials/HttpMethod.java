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

package org.dmfs.httpessentials;

import org.dmfs.httpessentials.methods.IdempotentMethod;
import org.dmfs.httpessentials.methods.Method;
import org.dmfs.httpessentials.methods.SafeMethod;


/**
 * Represents an HTTP method and provides static members for HTTP methods defined in <a href="https://tools.ietf.org/html/rfc7231#section-4.3">RFC 7231, section
 * 4.3</a> and <a href="http://tools.ietf.org/html/rfc5789">RFC 5789</a>
 *
 * @author Marten Gajda
 * @see <a href="https://tools.ietf.org/html/rfc7231#section-4">RFC 7231, section 4</a>
 */
public interface HttpMethod
{

    /**
     * HTTP Method GET
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.1">RFC 7231, section 4.3.1</a>
     */
    public final static HttpMethod GET = new SafeMethod("GET", false);

    /**
     * HTTP Method HEAD
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.2">RFC 7231, section 4.3.2</a>
     */
    public final static HttpMethod HEAD = new SafeMethod("HEAD", false);

    /**
     * HTTP Method POST
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.3">RFC 7231, section 4.3.3</a>
     */
    public final static HttpMethod POST = new Method("POST", true);

    /**
     * HTTP Method PUT
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.4">RFC 7231, section 4.3.4</a>
     */
    public final static HttpMethod PUT = new IdempotentMethod("PUT", true);

    /**
     * HTTP Method DELETE
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.5">RFC 7231, section 4.3.5</a>
     */
    public final static HttpMethod DELETE = new IdempotentMethod("DELETE", false);

    /**
     * HTTP Method CONNECT
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.6">RFC 7231, section 4.3.6</a>
     */
    public final static HttpMethod CONNECT = new Method("CONNECT", false);

    /**
     * HTTP Method OPTIONS
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.7">RFC 7231, section 4.3.7</a>
     */
    public final static HttpMethod OPTIONS = new SafeMethod("OPTIONS", true);

    /**
     * HTTP Method TRACE
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.3.8">RFC 7231, section 4.3.8</a>
     */
    public final static HttpMethod TRACE = new SafeMethod("TRACE", false);

    /**
     * HTTP Method PATCH
     *
     * @see <a href="http://tools.ietf.org/html/rfc5789">RFC 5789</a>
     */
    public final static HttpMethod PATCH = new Method("PATCH", true);

    /**
     * Returns the HTTP verb of this method.
     *
     * @return A String containing the verb of the method.
     */
    public String verb();

    /**
     * Returns if this request method is safe, which means that the request is not intended and not expected to change any state on the server. In effect the
     * semantics are to be considered <em>read-only</em>.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.2.1">RFC 7231, Section 4.2.1</a>
     */
    public boolean isSafe();

    /**
     * Returns if this request method is idempotent, which means that sending multiple identical requests with that method has the same effect as sending one
     * single request to the server.
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-4.2.2">RFC 7231, Section 4.2.2</a>
     */
    public boolean isIdempotent();

    /**
     * Returns whether this {@link HttpMethod} allows to send a message body.
     * <p>
     * Note that RFC 7231 does not explicitly forbid a message payload for some methods (in particular these are {@link #GET}, {@link #HEAD}, {@link #DELETE}
     * and {@link #CONNECT}). Instead is says something like this:
     * <p>
     * <pre>
     * A payload within a XXX request message has no defined semantics;
     * sending a payload body on a XXX request might cause some existing
     * implementations to reject the request.
     * </pre>
     * <p>
     * The predefined methods in {@link HttpMethod} interpret this rather strict and return <code>false</code> for such methods.
     *
     * @return <code>true</code> if this method supports a message body, <code>false</code> otherwise.
     */
    public boolean supportsRequestPayload();
}
