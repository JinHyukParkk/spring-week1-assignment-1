package com.codesoom.assignment.models;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.stream.Collectors;

public class HttpRequest {

    private RequestMethod method;

    private String path;

    private String body;

    public HttpRequest(String method, URI uri, InputStream inputStream) {
        this.method = RequestMethod.valueOf(method);
        this.path = uri.getPath();
        this.body = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }

}
