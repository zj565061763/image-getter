package com.sd.lib.imggetter;

import android.net.Uri;

public interface CameraImageGetter extends ImageGetter<CameraImageGetter>
{
    /**
     * 设置拍照保存文件的Uri
     *
     * @param uri
     * @return
     */
    CameraImageGetter fileUri(Uri uri);
}
