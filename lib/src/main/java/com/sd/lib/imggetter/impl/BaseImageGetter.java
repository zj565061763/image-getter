package com.sd.lib.imggetter.impl;

import android.app.Activity;

import com.sd.lib.imggetter.ImageGetter;

public abstract class BaseImageGetter implements ImageGetter, ImageGetterActivity.Callback
{
    protected final Activity mActivity;

    protected SuccessCallback mSuccessCallback;
    protected ErrorCallback mErrorCallback;
    protected CancelCallback mCancelCallback;

    public BaseImageGetter(Activity activity)
    {
        if (activity == null)
            throw new IllegalArgumentException("activity is null");
        mActivity = activity;
    }

    @Override
    public ImageGetter onSuccess(SuccessCallback callback)
    {
        mSuccessCallback = callback;
        return this;
    }

    @Override
    public ImageGetter onError(ErrorCallback callback)
    {
        mErrorCallback = callback;
        return this;
    }

    @Override
    public ImageGetter onCancel(CancelCallback callback)
    {
        mCancelCallback = callback;
        return this;
    }
}
