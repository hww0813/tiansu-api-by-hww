package com.yuanqing.common.utils.http;

import com.yuanqing.common.enums.HttpMethod;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * Created by xucan on 2020-11-24 10:24
 * @author xucan
 */

public class HttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {

    public HttpGetRequestWithEntity(final URI url) {
        super.setURI(url);
    }

    @Override
    public String getMethod() {
        return HttpMethod.GET.name();
    }
}
