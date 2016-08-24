package org.dmfs.httpessentials.httpurlconnection;

import org.dmfs.httpessentials.HttpMethod;
import org.dmfs.httpessentials.client.HttpRequest;
import org.dmfs.httpessentials.client.HttpRequestEntity;
import org.dmfs.httpessentials.client.HttpResponse;
import org.dmfs.httpessentials.client.HttpResponseHandler;
import org.dmfs.httpessentials.exceptions.ProtocolError;
import org.dmfs.httpessentials.exceptions.ProtocolException;
import org.dmfs.httpessentials.headers.Headers;
import org.dmfs.httpessentials.types.MediaType;

import java.io.IOException;
import java.io.OutputStream;


/**
 * {@link HttpRequest} stub that uses the given {@link Headers} value.
 *
 * @author Gabor Keszthelyi
 */
public final class EmptyRequestWithHeaders implements HttpRequest<String>
{

    private final Headers headers;


    public EmptyRequestWithHeaders(Headers headers)
    {
        this.headers = headers;
    }


    @Override
    public HttpMethod method()
    {
        return HttpMethod.GET;
    }


    @Override
    public Headers headers()
    {
        return headers;
    }


    @Override
    public HttpRequestEntity requestEntity()
    {
        return new HttpRequestEntity()
        {
            @Override
            public MediaType contentType()
            {
                return null;
            }


            @Override
            public long contentLength() throws IOException
            {
                return 0;
            }


            @Override
            public void writeContent(OutputStream out) throws IOException
            {

            }
        };
    }


    @Override
    public HttpResponseHandler<String> responseHandler(HttpResponse response) throws IOException, ProtocolError, ProtocolException
    {
        return new HttpResponseHandler<String>()
        {
            @Override
            public String handleResponse(HttpResponse response) throws IOException, ProtocolError, ProtocolException
            {
                return "the result";
            }
        };
    }
}
