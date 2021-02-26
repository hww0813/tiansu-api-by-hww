package com.yuanqing.common.utils.http;

import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;

/**
 * Created by xucan on 2020-11-24 10:29
 * @author xucan
 */
public class HttpComponentClientHttpRequestWithBodyFactory extends HttpComponentsClientHttpRequestFactory {

    @Override
    protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
        if(httpMethod == HttpMethod.GET){
            return new HttpGetRequestWithEntity(uri);
        }
        return super.createHttpUriRequest(httpMethod, uri);
    }
}
