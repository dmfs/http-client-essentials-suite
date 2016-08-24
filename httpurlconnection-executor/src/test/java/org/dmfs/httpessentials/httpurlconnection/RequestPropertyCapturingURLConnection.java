package org.dmfs.httpessentials.httpurlconnection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * Stub {@link HttpURLConnection} that captures the request properties that is set for it with {@link
 * HttpURLConnection#setRequestProperty(String, String)}.
 *
 * @author Gabor Keszthelyi
 */
public final class RequestPropertyCapturingURLConnection extends HttpURLConnection
{

    public Map<String, String> capturedHeaders = new HashMap<>();


    public RequestPropertyCapturingURLConnection(URL u)
    {
        super(u);
    }


    @Override
    public void disconnect()
    {
    }


    @Override
    public void setRequestProperty(String key, String value)
    {
        super.setRequestProperty(key, value);
        capturedHeaders.put(key, value);
    }


    @Override
    public boolean usingProxy()
    {
        return false;
    }


    @Override
    public void connect() throws IOException
    {
    }
}
