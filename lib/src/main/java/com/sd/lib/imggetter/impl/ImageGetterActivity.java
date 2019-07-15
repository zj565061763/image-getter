package com.sd.lib.imggetter.impl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public final class ImageGetterActivity extends Activity
{
    private static final int TYPE_ALBUM = 1;
    private static final int TYPE_CAMERA = 2;

    private static final String EXTRA_TYPE = "extra_type";
    private static final String EXTRA_TYPE_CAMERA_FILE = "extra_type_camera_file";

    private static Callback sCallback;

    static void startAlbum(Activity context, Callback callback)
    {
        final Intent intent = getIntent(context, TYPE_ALBUM, callback);
        if (intent != null)
            context.startActivity(intent);
    }

    static void startCamera(Activity context, Uri uri, Callback callback)
    {
        final Intent intent = getIntent(context, TYPE_CAMERA, callback);
        intent.putExtra(EXTRA_TYPE_CAMERA_FILE, uri);
        if (intent != null)
            context.startActivity(intent);
    }

    private static Intent getIntent(Activity context, int type, Callback callback)
    {
        if (callback == null)
            throw new IllegalArgumentException("callback is null");

        if (sCallback != null)
            return null;

        sCallback = callback;

        final Intent intent = new Intent(context, ImageGetterActivity.class);
        intent.putExtra(EXTRA_TYPE, type);
        return intent;
    }

    private int mType = 0;
    private Uri mCameraFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (sCallback == null)
        {
            finish();
            return;
        }

        mType = getIntent().getIntExtra(EXTRA_TYPE, 0);
        if (mType <= 0)
        {
            finish();
            return;
        }

        mCameraFileUri = getIntent().getParcelableExtra(EXTRA_TYPE_CAMERA_FILE);

        switch (mType)
        {
            case TYPE_ALBUM:
                startAlbum();
                break;
            case TYPE_CAMERA:
                if (mCameraFileUri == null)
                    throw new RuntimeException("Camera file uri is null");
                startCamera();
                break;
            default:
                break;
        }
    }

    private void startAlbum()
    {
        try
        {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, TYPE_ALBUM);
        } catch (Exception e)
        {
            sCallback.onStartError(e);
            sCallback = null;
            finish();
        }
    }

    private void startCamera()
    {
        try
        {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraFileUri);
            startActivityForResult(intent, TYPE_CAMERA);
        } catch (Exception e)
        {
            sCallback.onStartError(e);
            sCallback = null;
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case TYPE_ALBUM:
                sCallback.onActivityResult(resultCode, data);
                break;
            case TYPE_CAMERA:
                sCallback.onActivityResult(resultCode, data);
                break;
            default:
                throw new RuntimeException("Unknown request code:" + requestCode);
        }

        sCallback = null;
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        sCallback = null;
    }

    interface Callback
    {
        void onStartError(Exception e);

        void onActivityResult(int resultCode, Intent data);
    }
}
