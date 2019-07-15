package com.sd.lib.imggetter;

import java.io.File;

public interface ImageGetter
{
    ImageGetter onSuccess(SuccessCallback callback);

    ImageGetter onError(ErrorCallback callback);

    ImageGetter onCancel(CancelCallback callback);

    void start();

    interface SuccessCallback
    {
        void onSuccess(File file);
    }

    interface ErrorCallback
    {
        boolean onError(Error error, Exception e);
    }

    interface CancelCallback
    {
        void onCancel();
    }

    enum Error
    {
        Start,
    }
}
