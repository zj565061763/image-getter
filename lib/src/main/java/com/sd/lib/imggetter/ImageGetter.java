package com.sd.lib.imggetter;

import java.io.File;

public interface ImageGetter
{
    ImageGetter setResultCallback(ResultCallback callback);

    ImageGetter setErrorCallback(ErrorCallback callback);

    void start();

    interface ResultCallback
    {
        void onResult(File file);
    }

    interface ErrorCallback
    {
        void onError(Error error, String msg);
    }

    enum Error
    {
        Start,
    }
}
