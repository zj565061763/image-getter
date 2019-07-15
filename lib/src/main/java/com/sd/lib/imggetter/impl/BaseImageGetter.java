package com.sd.lib.imggetter.impl;

import android.app.Activity;
import android.widget.Toast;

import com.sd.lib.imggetter.ImageGetter;
import com.sd.lib.imggetter.R;

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

    protected final void notifyError(Error error, Exception e, int defaultTips)
    {
        if (mErrorCallback != null && mErrorCallback.onError(error, e))
        {
        } else
        {
            Toast.makeText(mActivity, mActivity.getResources().getString(defaultTips), Toast.LENGTH_SHORT).show();
        }
    }

    protected final void notifyCancel()
    {
        if (mCancelCallback != null && mCancelCallback.onCancel())
        {
        } else
        {
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.lib_image_getter_tips_cancel), Toast.LENGTH_SHORT).show();
        }
    }
}
