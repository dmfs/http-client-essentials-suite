package org.dmfs.httpessentials.httpurlconnection;

import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.converters.UserAgentConverter;
import org.dmfs.httpessentials.headers.BasicSingletonHeaderType;
import org.dmfs.httpessentials.headers.EmptyHeaders;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.headers.HttpHeaders;
import org.dmfs.httpessentials.headers.SingletonHeaderType;
import org.dmfs.httpessentials.types.SimpleProduct;
import org.dmfs.httpessentials.types.SingletonUserAgent;
import org.dmfs.httpessentials.types.UserAgent;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;

import static org.junit.Assert.assertEquals;


/**
 * Functional test for checking correct User-Agent header appending in {@link HttpUrlConnectionExecutor}.
 *
 * @author Gabor Keszthelyi
 */
public class HttpUrlConnectionExecutorUserAgentTest
{

    /**
     * User-Agent header type.
     */
    private final static SingletonHeaderType<UserAgent> USER_AGENT_HEADER =
            new BasicSingletonHeaderType<UserAgent>("User-Agent", new UserAgentConverter());

    private static final String CONTENT_LENGTH = "50";
    private static final String PRODUCT_NAME = "high-level-product";

    private HttpUrlConnectionExecutor executorUnderTest;
    private RequestPropertyCapturingURLConnection capturingConnection;


    @Before
    public void setup()
    {
        executorUnderTest = new HttpUrlConnectionExecutor(new HttpUrlConnectionFactory()
        {
            @Override
            public HttpURLConnection httpUrlConnection(URI uri) throws IllegalArgumentException, IOException
            {
                capturingConnection = new RequestPropertyCapturingURLConnection(uri.toURL());
                return capturingConnection;
            }
        });
    }


    @Test
    public void testUserAgentHeaderAppend_whenThereIsOneAlready_shouldAppendCorrectValue() throws Exception
    {
        // ARRANGE
        Headers headers = EmptyHeaders.INSTANCE
                .withHeader(HttpHeaders.CONTENT_LENGTH.entityFromString(CONTENT_LENGTH))
                .withHeader(USER_AGENT_HEADER.entity(new SingletonUserAgent(new SimpleProduct(PRODUCT_NAME))));
        HttpRequest<String> request = new EmptyRequestWithHeaders(headers);

        // ACT
        executorUnderTest.execute(URI.create("http://anything"), request);

        // ASSERT
        assertEquals(2, capturingConnection.capturedHeaders.size());
        assertEquals(expectedUserAgentHeader(), capturingConnection.capturedHeaders.get("User-Agent"));
        assertEquals(CONTENT_LENGTH, capturingConnection.capturedHeaders.get("content-length"));
    }


    private String expectedUserAgentHeader()
    {
        String systemHttpAgent = System.getProperty("http.agent");
        return systemHttpAgent == null ?
                String.format("%s %s/%s", PRODUCT_NAME, BuildConfig.NAME, BuildConfig.VERSION) :
                String.format("%s %s/%s %s", PRODUCT_NAME, BuildConfig.NAME, BuildConfig.VERSION, systemHttpAgent);
    }


    @Test
    public void testUserAgentHeaderAppend_whenThereIsNone_shouldNotAppend() throws Exception
    {
        // ARRANGE
        Headers headers = EmptyHeaders.INSTANCE.withHeader(HttpHeaders.CONTENT_LENGTH.entityFromString(CONTENT_LENGTH));
        HttpRequest<String> request = new EmptyRequestWithHeaders(headers);

        // ACT
        executorUnderTest.execute(URI.create("http://anything"), request);

        // ASSERT
        assertEquals(1, capturingConnection.capturedHeaders.size());
        assertEquals(CONTENT_LENGTH, capturingConnection.capturedHeaders.get("content-length"));
    }

}