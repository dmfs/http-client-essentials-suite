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

package org.dmfs.httpessentials;

import org.dmfs.httpessentials.status.NoneHttpStatus;
import org.dmfs.httpessentials.status.SimpleHttpStatus;


/**
 * Interface of an HTTP status. Instances must be immutable. By convention, all instances MUST return the status code when {@link #hashCode()} is called.
 *
 * @author Marten Gajda
 */
public interface HttpStatus
{
    /*
     * Dummy HttpStatus for internal use.
	 */

    /**
     * A dummy HTTP status which can be used to return "no status". This is the <code>null</code> object.
     */
    public final static HttpStatus NONE = new NoneHttpStatus();

	/*
     * 1xx Informational status codes, see http://tools.ietf.org/html/rfc7231#section-6.2
	 */

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.2.1">100 Continue</a>
     */
    public final static HttpStatus CONTINUE = new SimpleHttpStatus(100, "Continue");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.2.2">101 Switching Protocols</a>
     */
    public final static HttpStatus SWITCHING_PROTOCOLS = new SimpleHttpStatus(101, "Switching Protocols");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc2518#section-10.1">102 Processing</a> (WebDAV) <p> Note that this has been removed from the WebDAV
     * specification in RFC 4918, see <a href="http://tools.ietf.org/html/rfc4918#section-21.4">RFC 4918, section 21.4</a></p>
     */
    public final static HttpStatus PROCESSING = new SimpleHttpStatus(102, "Processing");

	/*
	 * 2xx Successful status codes, see: http://tools.ietf.org/html/rfc7231#section-6.3
	 */

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.3.1">200 OK</a>
     */
    public final static HttpStatus OK = new SimpleHttpStatus(200, "OK");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.3.2">201 Created</a>
     */
    public final static HttpStatus CREATED = new SimpleHttpStatus(201, "CREATED");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.3.3">202 Accepted</a>
     */
    public final static HttpStatus ACCEPTED = new SimpleHttpStatus(202, "Accepted");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.3.4">203 Non-Authoritative Information</a>
     */
    public final static HttpStatus NON_AUTHORITATIVE_INFORMATION = new SimpleHttpStatus(203,
            "Non-Authoritative Information");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.3.5">204 No Content</a>
     */
    public final static HttpStatus NO_CONTENT = new SimpleHttpStatus(204, "No Content");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.3.6">205 Reset Content</a>
     */
    public final static HttpStatus RESET_CONTENT = new SimpleHttpStatus(205, "Reset Content");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7233#section-4.1">206 Partial Content</a>
     */
    public final static HttpStatus PARTIAL_CONTENT = new SimpleHttpStatus(206, "Partial Content");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc4918#section-11.1">207 Multistatus</a> (WebDAV)
     */
    public final static HttpStatus MULTISTATUS = new SimpleHttpStatus(207, "Multistatus");

	/*
	 * 3xx Redirection status codes, see: http://tools.ietf.org/html/rfc2068#section-10.3
	 */

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.4.1">300 Multiple Choices</a>
     */
    public final static HttpStatus MULTIPLE_CHOICES = new SimpleHttpStatus(300, "Multiple Choices");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.4.2">301 Moved Permanently</a>
     */
    public final static HttpStatus MOVED_PERMANENTLY = new SimpleHttpStatus(301, "Moved Permanently");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.4.3">302 Moved Temporarily</a>
     */
    public final static HttpStatus FOUND = new SimpleHttpStatus(302, "Found");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.4.4">303 See Other</a>
     */
    public final static HttpStatus SEE_OTHER = new SimpleHttpStatus(303, "See Other");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7232#section-4.1">304 Not Modified</a>
     */
    public final static HttpStatus NOT_MODIFIED = new SimpleHttpStatus(304, "Not Modified");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.4.5">305 Use Proxy</a>
     */
    public final static HttpStatus USE_PROXY = new SimpleHttpStatus(305, "Use Proxy");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.4.7">307 Temporary Redirect</a>
     */
    public final static HttpStatus TEMPORARY_REDIRECT = new SimpleHttpStatus(307, "Temporary Redirect");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7238#section-3">308 Permanent Redirect</a>
     */
    public final static HttpStatus PERMANENT_REDIRECT = new SimpleHttpStatus(308, "Permanent Redirect");

	/*
	 * 4xx Client Error status codes, see: http://tools.ietf.org/html/rfc2068#section-10.4
	 */

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.1">400 Bad Request</a>
     */
    public final static HttpStatus BAD_REQUEST = new SimpleHttpStatus(400, "Bad Request");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7235#section-3.1">401 Unauthorized</a>
     */
    public final static HttpStatus UNAUTHORIZED = new SimpleHttpStatus(401, "Unauthorized");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.2">402 Payment Required</a>
     */
    public final static HttpStatus PAYMENT_REQUIRED = new SimpleHttpStatus(402, "Payment Required");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.3">403 Forbidden</a>
     */
    public final static HttpStatus FORBIDDEN = new SimpleHttpStatus(403, "Forbidden");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.4">404 Not Found</a>
     */
    public final static HttpStatus NOT_FOUND = new SimpleHttpStatus(404, "Not Found");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.5">405 Method Not Allowed</a>
     */
    public final static HttpStatus METHOD_NOT_ALLOWED = new SimpleHttpStatus(405, "Method Not Allowed");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.6">406 Not Acceptable</a>
     */
    public final static HttpStatus NOT_ACCEPTABLE = new SimpleHttpStatus(406, "Not Acceptable");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7235#section-3.2">407 Proxy Authentication Required</a>
     */
    public final static HttpStatus PROXY_AUTHENTICATION_REQUIRED = new SimpleHttpStatus(407,
            "Proxy Authentication Required");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.7">408 Request Timeout</a>
     */
    public final static HttpStatus REQUEST_TIMEOUT = new SimpleHttpStatus(408, "Request Timeout");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.8">409 Conflict</a>
     */
    public final static HttpStatus CONFLICT = new SimpleHttpStatus(409, "Conflict");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.10">410 Gone</a>
     */
    public final static HttpStatus GONE = new SimpleHttpStatus(410, "Gone");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-10.4.12">411 Length Required</a>
     */
    public final static HttpStatus LENGTH_REQUIRED = new SimpleHttpStatus(411, "Length Required");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7232#section-4.2">412 Precondition Failed</a>
     */
    public final static HttpStatus PRECONDITION_FAILED = new SimpleHttpStatus(412, "Precondition Failed");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.11">413 Payload Too Large</a>
     */
    public final static HttpStatus PAYLOAD_TOO_LARGE = new SimpleHttpStatus(413, "Payload Too Large");

    /**
     * Old name of {@link #PAYLOAD_TOO_LARGE}.
     */
    @Deprecated
    public final static HttpStatus REQUEST_ENTITY_TOO_LARGE = PAYLOAD_TOO_LARGE;

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.12">414 URI Too Long</a>
     */
    public final static HttpStatus URI_TOO_LONG = new SimpleHttpStatus(414, "URI Too Long");

    /**
     * Old name of {@link #URI_TOO_LONG}.
     */
    @Deprecated
    public final static HttpStatus REQUEST_URI_TOO_LONG = URI_TOO_LONG;

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.13">415 Unsupported Media Type</a>
     */
    public final static HttpStatus UNSUPPORTED_MEDIA_TYPE = new SimpleHttpStatus(415, "Unsupported Media Type");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.14">417 Expectation Failed</a>
     */
    public final static HttpStatus EXPECTATION_FAILED = new SimpleHttpStatus(417, "Expectation Failed");
    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc4918#section-11.2">422 Unprocessable Entity</a> (WebDAV)
     */
    public final static HttpStatus UNPROCESSABLE_ENTITY = new SimpleHttpStatus(422, "Unprocessable Entity");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc4918#section-11.3">423 Locked</a> (WebDAV)
     */
    public final static HttpStatus LOCKED = new SimpleHttpStatus(423, "Locked");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc4918#section-11.4">424 Failed Dependency</a> (WebDAV)
     */
    public final static HttpStatus FAILED_DEPENDENCY = new SimpleHttpStatus(424, "Failed Dependency");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.5.15">426 Upgrade Required</a>
     */
    public final static HttpStatus UPGRADE_REQUIRED = new SimpleHttpStatus(426, "Upgrade Required");

	/*
	 * 5xx Server Error status codes, see: http://tools.ietf.org/html/rfc2068#section-10.5
	 */

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.6.1">500 Internal Server Error</a>
     */
    public final static HttpStatus INTERNAL_SERVER_ERROR = new SimpleHttpStatus(500, "Internal Server Error");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.6.2">501 Not Implemented</a>
     */
    public final static HttpStatus NOT_IMPLEMENTED = new SimpleHttpStatus(501, "Not Implemented");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.6.3">502 Bad Gateway</a>
     */
    public final static HttpStatus BAD_GATEWAY = new SimpleHttpStatus(502, "Bad Gateway");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.6.4">503 Service Unavailable</a>
     */
    public final static HttpStatus SERVICE_UNAVAILABLE = new SimpleHttpStatus(503, "Service Unavailable");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.6.5">504 Gateway Timeout</a>
     */
    public final static HttpStatus GATEWAY_TIMEOUT = new SimpleHttpStatus(504, "Gateway Timeout");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc7231#section-6.6.6">505 HTTP Version Not Supported</a>
     */
    public final static HttpStatus HTTP_VERSION_NOT_SUPPORTED = new SimpleHttpStatus(505, "HTTP Version Not Supported");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc2295#section-8.1">506 Variant Also Negotiates</a> (experimental)
     */
    public final static HttpStatus VARIANT_ALSO_NEGOTIATES = new SimpleHttpStatus(506, "Variant Also Negotiates");

    /**
     * HTTP status: <a href="http://tools.ietf.org/html/rfc4918#section-11.5">507 Insufficient Storage</a> (WebDAV)
     */
    public final static HttpStatus INSUFFICIENT_STORAGE = new SimpleHttpStatus(507, "Insufficient Storage");

    /**
     * Returns the status code.
     *
     * @return
     */
    public int statusCode();

    /**
     * Returns the reason phrase of this status code. <p> <strong>Note:</strong> the reason phrase doesn't contain the status code itself. </p>
     *
     * @return The reason phrase or <code>null</code> if the status code is unknown.
     */
    public String reason();

    /**
     * Returns a status line in the form:
     * <p>
     * <pre>
     * HTTP/VersionMajor.VersionMinor SP Status-Code SP Reason-Phrase
     * </pre>
     *
     * @param httpVersionMajor
     *         The major version number, usually <code>1</code>
     * @param httpVersionMinor
     *         The minor version number, usually <code>1</code>
     *
     * @return
     */
    public String httpStatusLine(int httpVersionMajor, int httpVersionMinor);

    /**
     * Returns whether this represents an informational status code.
     *
     * @return <code>true</code> if this represents an informational status code, <code>false</code> otherwise.
     */
    public boolean isInformational();

    /**
     * Returns whether this status represents a success status code.
     *
     * @return <code>true</code> if this represents a success status code, <code>false</code> otherwise.
     */
    public boolean isSuccess();

    /**
     * Returns whether this status represents a redirection status code.
     *
     * @return <code>true</code> if this represents a redirection status code, <code>false</code> otherwise.
     */
    public boolean isRedirect();

    /**
     * Returns whether this status represents a client error status code.
     *
     * @return <code>true</code> if this represents a client error status code, <code>false</code> otherwise.
     */
    public boolean isClientError();

    /**
     * Returns whether this status represents a server error status code.
     *
     * @return <code>true</code> if this represents a server error status code, <code>false</code> otherwise.
     */
    public boolean isServerError();

    @Override
    public int hashCode();

    @Override
    public boolean equals(final Object obj);
}
