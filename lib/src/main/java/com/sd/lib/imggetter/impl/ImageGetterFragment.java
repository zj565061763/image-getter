package com.sd.lib.imggetter.impl;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public final class ImageGetterFragment extends Fragment
{
    private static final int TYPE_ALBUM = 1;
    private static final int TYPE_CAMERA = 2;

    private static final String EXTRA_TYPE = "extra_type";
    private static final String EXTRA_TYPE_CAMERA_FILE = "extra_type_camera_file";

    static void startAlbum(Activity activity, Callback callback)
    {
        final Fragment fragment = createFragment(activity, TYPE_ALBUM, callback);
        if (fragment != null)
        {
            activity.getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commitAllowingStateLoss();
        }
    }

    static void startCamera(Activity activity, Uri uri, Callback callback)
    {
        final Fragment fragment = createFragment(activity, TYPE_CAMERA, callback);
        if (fragment != null)
        {
            fragment.getArguments().putParcelable(EXTRA_TYPE_CAMERA_FILE, uri);
            activity.getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commitAllowingStateLoss();
        }
    }

    private static Fragment createFragment(Activity activity, int type, Callback callback)
    {
        if (callback == null)
            throw new IllegalArgumentException("callback is null");

        if (activity == null)
            throw new IllegalArgumentException("activity is null");

        final Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TYPE, type);

        final ImageGetterFragment fragment = new ImageGetterFragment();
        fragment.setArguments(bundle);
        fragment.mCallback = callback;
        return fragment;
    }

    private int mType = 0;
    private Uri mCameraFileUri;
    private Callback mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (mCallback == null)
        {
            removeSelf();
            return;
        }

        mType = getArguments().getInt(EXTRA_TYPE, 0);
        if (mType <= 0)
        {
            removeSelf();
            return;
        }

        mCameraFileUri = getArguments().getParcelable(EXTRA_TYPE_CAMERA_FILE);

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
    }

    private void removeSelf()
    {
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
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
            mCallback.onStartError(e);
            removeSelf();
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
            mCallback.onStartError(e);
            removeSelf();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case TYPE_ALBUM:
                mCallback.onActivityResult(resultCode, data);
                break;
            case TYPE_CAMERA:
                mCallback.onActivityResult(resultCode, data);
                break;
            default:
                throw new RuntimeException("Unknown request code:" + requestCode);
        }

        removeSelf();
    }

    interface Callback
    {
        void onStartError(Exception e);

        void onActivityResult(int resultCode, Intent data);
    }
}
