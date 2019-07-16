package com.sd.lib.imggetter.impl;

import android.app.Activity;
import android.app.Application;
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

    private static Activity sActivity;
    private static Callback sCallback;

    static void startAlbum(Activity activity, Callback callback)
    {
        final Intent intent = getIntent(activity, TYPE_ALBUM, callback);
        if (intent != null)
            activity.startActivity(intent);
    }

    static void startCamera(Activity activity, Uri uri, Callback callback)
    {
        final Intent intent = getIntent(activity, TYPE_CAMERA, callback);
        if (intent != null)
        {
            intent.putExtra(EXTRA_TYPE_CAMERA_FILE, uri);
            activity.startActivity(intent);
        }
    }

    private static Intent getIntent(Activity activity, int type, Callback callback)
    {
        if (callback == null)
            throw new IllegalArgumentException("callback is null");

        if (activity == null)
            throw new IllegalArgumentException("activity is null");

        if (sCallback != null)
            return null;

        sCallback = callback;
        sActivity = activity;

        final Intent intent = new Intent(activity, ImageGetterActivity.class);
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
                throw new RuntimeException("Unknown type:" + mType);
        }

        getApplication().registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
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
            finish();
        }
    }

    private final Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks()
    {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState)
        {
        }

        @Override
        public void onActivityStarted(Activity activity)
        {
        }

        @Override
        public void onActivityResumed(Activity activity)
        {
        }

        @Override
        public void onActivityPaused(Activity activity)
        {
        }

        @Override
        public void onActivityStopped(Activity activity)
        {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState)
        {
        }

        @Override
        public void onActivityDestroyed(Activity activity)
        {
            if (sActivity == activity)
            {
                finish();
            }
        }
    };

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

        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        sCallback = null;
        sActivity = null;
        getApplication().unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    interface Callback
    {
        void onStartError(Exception e);

        void onActivityResult(int resultCode, Intent data);
    }
}
