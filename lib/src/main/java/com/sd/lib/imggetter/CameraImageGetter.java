package com.sd.lib.imggetter;

import java.io.File;

public interface CameraImageGetter extends ImageGetter<CameraImageGetter>
{
    /**
     * 设置拍照保存文件
     *
     * @param file
     * @return
     */
    CameraImageGetter file(File file);
}
