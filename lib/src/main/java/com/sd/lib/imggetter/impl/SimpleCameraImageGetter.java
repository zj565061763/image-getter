package com.sd.lib.imggetter.impl;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.sd.lib.imggetter.CameraImageGetter;
import com.sd.lib.imggetter.ImageGetter;
import com.sd.lib.imggetter.R;

import java.io.File;

/**
 * 摄像头获取图片
 */
class SimpleCameraImageGetter extends BaseImageGetter<CameraImageGetter> implements CameraImageGetter
{
    private File mFile;
    private MediaScannerConnection mScannerConnection;

    public SimpleCameraImageGetter(Activity activity)
    {
        super(activity);
    }

    @Override
    public CameraImageGetter file(File file)
    {
        mFile = file;
        return this;
    }

    @Override
    public void start()
    {
        File cameraFile = mFile;
        if (cameraFile == null)
        {
            cameraFile = Utils.newCameraFile(getActivity());
            if (cameraFile == null)
            {
                notifyError(Error.CreateCameraFile, null, R.string.lib_image_getter_tips_error_create_camera_file);
                return;
            }
        }

        final File file = cameraFile;
        final Uri uri = Utils.getUri(getActivity(), file);

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
                    scanFile(file);
                    final String path = file.getAbsolutePath();
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

    private void scanFile(final File file)
    {
        if (mScannerConnection == null)
        {
            mScannerConnection = new MediaScannerConnection(getActivity(), new MediaScannerConnection.MediaScannerConnectionClient()
            {
                @Override
                public void onMediaScannerConnected()
                {
                    final String filePath = file.getAbsolutePath();
                    final String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
                    final String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    mScannerConnection.scanFile(filePath, mimeType);
                }

                @Override
                public void onScanCompleted(String path, Uri uri)
                {
                    mScannerConnection.disconnect();
                    mScannerConnection = null;
                }
            });
        }

        if (!mScannerConnection.isConnected())
            mScannerConnection.connect();
    }
}
