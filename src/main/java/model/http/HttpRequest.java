package model.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private HttpRequestHeader httpRequestHeader;
    private HttpRequestBody httpRequestBody;

    private HttpRequest(HttpRequestHeader httpRequestHeader) {
        this.httpRequestHeader = httpRequestHeader;
    }

    private HttpRequest(HttpRequestHeader httpRequestHeader, HttpRequestBody httpRequestBody) {
        this.httpRequestHeader = httpRequestHeader;
        this.httpRequestBody = httpRequestBody;
    }

    public static HttpRequest of(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        HttpRequestHeader httpRequestHeader = makeHttpRequestHeader(reader);

        if (httpRequestHeader.containsBody()) {
            HttpRequestBody httpRequestBody = HttpRequestBody.of(reader, httpRequestHeader.getContentLength());
            return new HttpRequest(httpRequestHeader, httpRequestBody);
        }

        return new HttpRequest(httpRequestHeader);
    }

    private static HttpRequestHeader makeHttpRequestHeader(BufferedReader reader) throws IOException {
        HttpRequestHeader httpRequestHeader;
        List<String> headerLines = new ArrayList<>();
        String line;

        while (!StringUtils.isEmpty(line = reader.readLine())) {
            headerLines.add(line);
        }

        logger.debug(headerLines.toString());

        httpRequestHeader = HttpRequestHeader.of(headerLines);
        return httpRequestHeader;
    }

    public static HttpRequest of(HttpRequestHeader httpRequestHeader) {
        return new HttpRequest(httpRequestHeader);
    }

    public RequestLine getRequestLine() {
        return httpRequestHeader.getRequestLine();
    }

    public boolean containsBody() {
        return httpRequestHeader.containsBody() && httpRequestBody != null;
    }

    public Optional<String> findDataValueByName(String name) {
        if (containsBody()) {
            return httpRequestBody.findDataByName(name)
                    .map(QueryParameter::getValue);
        }
        return httpRequestHeader.findQueryParameterByName(name)
                .map(QueryParameter::getValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest that = (HttpRequest) o;
        return Objects.equals(httpRequestHeader, that.httpRequestHeader) &&
                Objects.equals(httpRequestBody, that.httpRequestBody);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpRequestHeader, httpRequestBody);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpRequestHeader=" + httpRequestHeader +
                ", httpRequestBody=" + httpRequestBody +
                '}';
    }
}
