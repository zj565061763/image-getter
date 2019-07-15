package com.sd.lib.imggetter.impl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.sd.lib.imggetter.ImageGetter;
import com.sd.lib.imggetter.R;

/**
 * 摄像头获取图片
 */
public class CameraImageGetter extends BaseImageGetter
{
    private Uri mFileUri;

    public CameraImageGetter(Activity activity)
    {
        super(activity);
    }

    /**
     * 设置拍照保存文件的Uri
     *
     * @param uri
     * @return
     */
    public ImageGetter fileUri(Uri uri)
    {
        mFileUri = uri;
        return this;
    }

    @Override
    public void start()
    {
        final Uri uri = mFileUri;

        if (uri == null)
            throw new RuntimeException("Camera file uri is null, see fileUri(Uri uri) method");

        ImageGetterActivity.startCamera(mActivity, uri, new ImageGetterActivity.Callback()
        {
            @Override
            public void onStartError(Exception e)
            {
                notifyError(Error.Start, e, R.string.lib_image_getter_tips_error_start_camera);
            }

            @Override
            public void onActivityResult(int resultCode, Intent data)
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    mSuccessCallback.onSuccess(uri.toString());
                } else if (resultCode == Activity.RESULT_CANCELED)
                {
                    notifyCancel();
                } else
                {
                    notifyError(Error.Other, null, R.string.lib_image_getter_tips_error_other);
                }
            }
        });
    }
}
