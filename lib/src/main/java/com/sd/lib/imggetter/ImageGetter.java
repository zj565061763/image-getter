package com.sd.lib.imggetter;

import java.io.File;

public interface ImageGetter
{
    /**
     * 设置成功回调
     *
     * @param callback
     * @return
     */
    ImageGetter onSuccess(SuccessCallback callback);

    /**
     * 设置失败回调
     *
     * @param callback
     * @return
     */
    ImageGetter onError(ErrorCallback callback);

    /**
     * 设置取消回调
     *
     * @param callback
     * @return
     */
    ImageGetter onCancel(CancelCallback callback);

    /**
     * 开始
     */
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
        boolean onCancel();
    }

    enum Error
    {
        Start,
        ResultEmpty,
        Result,
        Other,
    }
}
