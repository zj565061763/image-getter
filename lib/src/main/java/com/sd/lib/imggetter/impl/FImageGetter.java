package com.sd.lib.imggetter.impl;

import android.app.Activity;

import com.sd.lib.imggetter.AlbumImageGetter;
import com.sd.lib.imggetter.CameraImageGetter;

public class FImageGetter
{
    private FImageGetter()
    {
    }

    /**
     * 相册
     *
     * @param activity
     * @return
     */
    public static AlbumImageGetter album(Activity activity)
    {
        return new SimpleAlbumImageGetter(activity);
    }

    /**
     * 摄像头拍照
     *
     * @param activity
     * @return
     */
    public static CameraImageGetter camera(Activity activity)
    {
        return new SimpleCameraImageGetter(activity);
    }
}
