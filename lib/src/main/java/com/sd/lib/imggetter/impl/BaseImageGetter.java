package com.sd.lib.imggetter.impl;

import com.sd.lib.imggetter.ImageGetter;

public class BaseImageGetter implements ImageGetter
{
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
