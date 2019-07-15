package com.sd.lib.imggetter.impl;

import android.app.Activity;
import android.widget.Toast;

import com.sd.lib.imggetter.ImageGetter;
import com.sd.lib.imggetter.R;

public abstract class BaseImageGetter implements ImageGetter
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

    protected void notifyError(Error error, Exception e, int defaultTips)
    {
        if (mErrorCallback != null && mErrorCallback.onError(error, e))
        {
        } else
        {
            Toast.makeText(mActivity, mActivity.getResources().getString(defaultTips), Toast.LENGTH_SHORT).show();
        }
    }

    protected void notifyCancel()
    {
        if (mCancelCallback != null && mCancelCallback.onCancel())
        {
        } else
        {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.lib_image_getter_tips_cancel), Toast.LENGTH_SHORT).show();
        }
    }
}
