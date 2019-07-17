package com.sd.lib.imggetter.impl;

import android.app.Activity;

import com.sd.lib.imggetter.ImageGetter;

abstract class BaseImageGetter<T extends ImageGetter> implements ImageGetter<T>
{
    private final Activity mActivity;

    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;
    private CancelCallback mCancelCallback;

    public BaseImageGetter(Activity activity)
    {
        if (activity == null)
            throw new IllegalArgumentException("activity is null");
        mActivity = activity;
    }

    @Override
    public final T onSuccess(SuccessCallback callback)
    {
        mSuccessCallback = callback;
        return (T) this;
    }

    @Override
    public final T onError(ErrorCallback callback)
    {
        mErrorCallback = callback;
        return (T) this;
    }

    @Override
    public final T onCancel(CancelCallback callback)
    {
        mCancelCallback = callback;
        return (T) this;
    }

    protected final Activity getActivity()
    {
        return mActivity;
    }

    protected final void notifySuccess(String path)
    {
        if (mSuccessCallback != null)
            mSuccessCallback.onSuccess(path);
    }

    protected final void notifyError(Error error, Exception e, String desc)
    {
        if (mErrorCallback != null)
        {
            if (e != null)
                desc = desc + ":" + e;

            mErrorCallback.onError(error, desc);
        }
    }

    protected final void notifyCancel()
    {
        if (mCancelCallback != null)
            mCancelCallback.onCancel();
    }
}
