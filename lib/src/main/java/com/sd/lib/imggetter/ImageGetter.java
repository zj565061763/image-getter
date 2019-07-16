package com.sd.lib.imggetter;

public interface ImageGetter<T extends ImageGetter>
{
    /**
     * 设置成功回调
     *
     * @param callback
     * @return
     */
    T onSuccess(SuccessCallback callback);

    /**
     * 设置失败回调
     *
     * @param callback
     * @return
     */
    T onError(ErrorCallback callback);

    /**
     * 设置取消回调
     *
     * @param callback
     * @return
     */
    T onCancel(CancelCallback callback);

    /**
     * 开始
     */
    void start();

    interface SuccessCallback
    {
        void onSuccess(String file);
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
        Permission,
        ResultEmpty,
        Result,
        Other,
    }
}
