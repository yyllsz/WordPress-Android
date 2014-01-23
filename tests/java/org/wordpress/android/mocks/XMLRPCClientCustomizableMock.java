package org.wordpress.android.mocks;

import android.content.Context;

import com.google.gson.Gson;

import org.wordpress.android.util.AppLog;
import org.wordpress.android.util.AppLog.T;
import org.xmlrpc.android.XMLRPCCallback;
import org.xmlrpc.android.XMLRPCClientInterface;
import org.xmlrpc.android.XMLRPCException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

public class XMLRPCClientCustomizableMock implements XMLRPCClientInterface {
    private Context mContext;
    private String mPrefix;

    public XMLRPCClientCustomizableMock(URI uri, String httpUser, String httpPassword) {
    }

    public void setContextAndPrefix(Context context, String prefix) {
        mContext = context;
        mPrefix = prefix;
    }

    public void setPrefix(String prefix) {
        mPrefix = prefix;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void addQuickPostHeader(String type) {
    }

    public void setAuthorizationHeader(String authToken) {
    }

    public Object call(String method, Object[] params) throws XMLRPCException {
        AppLog.v(T.TESTS, "XMLRPCClientCustomizableMock: <call(" + method + ", ...)>");
        if ("login-failure".equals(mPrefix)) {
            // Wrong login
            throw new XMLRPCException("code 403");
        }

        // method example: wp.getUsersBlogs
        // Filename: default-wp.getUsersBlogs.json
        String filename = mPrefix + "-" + method + ".json";
        try {
            Gson gson = new Gson();
            InputStream is = mContext.getAssets().open(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader f = new BufferedReader(inputStreamReader);
            return gson.fromJson(inputStreamReader, Object[].class);
        } catch (IOException e) {
            AppLog.e(T.TESTS, "can't read file: " + filename);
        }
        return null;
    }

    public Object call(String method) throws XMLRPCException {
        return null;
    }

    public Object call(String method, Object[] params, File tempFile) throws XMLRPCException {
        return null;
    }

    public long callAsync(XMLRPCCallback listener, String methodName, Object[] params) {
        return 0;
    }

    public long callAsync(XMLRPCCallback listener, String methodName, Object[] params, File tempFile) {
        return 0;
    }
}