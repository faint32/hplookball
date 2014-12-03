package com.koushikdutta.async.http;

import java.net.URI;
import java.net.URISyntaxException;

import android.util.Log;

public class AsyncHttpPost extends AsyncHttpRequest {
    public static final String METHOD = "POST";
    
    public AsyncHttpPost(String uri) {
        this(URI.create(uri));
//        Log.d("SocketIORequest", uri);
    }

    public AsyncHttpPost(URI uri) {
        super(uri, METHOD);
    }
}
