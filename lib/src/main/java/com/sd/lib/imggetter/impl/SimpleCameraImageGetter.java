package com.sd.lib.imggetter.impl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.sd.lib.imggetter.CameraImageGetter;
import com.sd.lib.imggetter.ImageGetter;
import com.sd.lib.imggetter.R;

/**
 * 摄像头获取图片
 */
class SimpleCameraImageGetter extends BaseImageGetter<CameraImageGetter> implements CameraImageGetter
{
    private Uri mFileUri;

    public SimpleCameraImageGetter(Activity activity)
    {
        super(activity);
    }

    @Override
    public CameraImageGetter fileUri(Uri uri)
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

        ImageGetterFragment.startCamera(getActivity(), uri, new ImageGetterFragment.Callback()
        {
            @Override
            public void onStartError(Exception e)
            {
                notifyError(ImageGetter.Error.Start, e, R.string.lib_image_getter_tips_error_start_camera);
            }

            @Override
            public void onActivityResult(int resultCode, Intent data)
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    final String path = uri.toString().substring(uri.getScheme().length() + 3);
                    notifySuccess(path);
                } else if (resultCode == Activity.RESULT_CANCELED)
                {
                    notifyCancel();
                } else
                {
                    notifyError(ImageGetter.Error.Other, null, R.string.lib_image_getter_tips_error_other);
                }
            }
        });
    }
}
