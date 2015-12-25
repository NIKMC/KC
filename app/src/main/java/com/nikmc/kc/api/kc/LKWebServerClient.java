package com.nikmc.kc.api.kc;

import android.content.Context;

import com.nikmc.kc.R;
import com.nikmc.kc.api.WebServerClient;

/**
 * Created by NIKMC-I on 25.12.2015.
 */
public class LKWebServerClient extends WebServerClient {

    protected LKWebServerClient(Context context) {
        super(context);
        this.ServerURL = context.getString(R.string.serverURL);
    }
}
