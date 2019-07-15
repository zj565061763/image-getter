package com.sd.lib.imggetter.impl;

import android.app.Activity;

import com.sd.lib.imggetter.ImageGetter;

public class BaseImageGetter implements ImageGetter
{
    private final Activity mActivity;

    public BaseImageGetter(Activity activity)
    {
        if (activity == null)
            throw new IllegalArgumentException("activity is null");
        mActivity = activity;
    }

    private ResultCallback mResultCallback;
    private ErrorCallback mErrorCallback;

    @Override
    public ImageGetter setResultCallback(ResultCallback callback)
    {
        return this;
    }

    @Override
    public ImageGetter setErrorCallback(ErrorCallback callback)
    {
        return this;
    }

    @Override
    public void start()
    {

    }
}
