package com.sd.lib.imggetter.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.sd.lib.imggetter.AlbumImageGetter;
import com.sd.lib.imggetter.ImageGetter;
import com.sd.lib.imggetter.R;

/**
 * 相册获取图片
 */
class SimpleAlbumImageGetter extends BaseImageGetter<AlbumImageGetter> implements AlbumImageGetter
{
    public SimpleAlbumImageGetter(Activity activity)
    {
        super(activity);
    }

    @Override
    public void start()
    {
        ImageGetterFragment.startAlbum(getActivity(), new ImageGetterFragment.Callback()
        {
            @Override
            public void onStartError(Exception e)
            {
                notifyError(ImageGetter.Error.Start, e, R.string.lib_image_getter_tips_error_start_album);
            }

            @Override
            public void onActivityResult(int resultCode, Intent data)
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    onActivityResultOk(data);
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

    private void onActivityResultOk(Intent data)
    {
        if (data == null)
        {
            notifyError(ImageGetter.Error.ResultEmpty, null, R.string.lib_image_getter_tips_error_result_empty);
            return;
        }

        final Uri uri = data.getData();
        if (uri == null)
        {
            notifyError(ImageGetter.Error.ResultEmpty, null, R.string.lib_image_getter_tips_error_result_empty);
            return;
        }

        String path = null;
        try
        {
            path = getDataColumn(getActivity(), uri, null, null);
        } catch (Exception e)
        {
            e.printStackTrace();
            notifyError(ImageGetter.Error.Result, null, R.string.lib_image_getter_tips_error_result);
            return;
        }

        if (TextUtils.isEmpty(path))
        {
            notifyError(ImageGetter.Error.Result, null, R.string.lib_image_getter_tips_error_result);
            return;
        }

        notifySuccess(path);
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs)
    {
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};

        Cursor cursor = null;
        try
        {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor.moveToFirst())
            {
                final int columnIndex = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(columnIndex);
            }
        } finally
        {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
