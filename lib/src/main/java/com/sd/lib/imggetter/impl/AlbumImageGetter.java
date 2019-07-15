package com.sd.lib.imggetter.impl;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.sd.lib.imggetter.R;

public class AlbumImageGetter extends BaseImageGetter
{
    public AlbumImageGetter(Activity activity)
    {
        super(activity);
    }

    @Override
    public void start()
    {
        ImageGetterActivity.startAlbum(mActivity, this);
    }

    @Override
    public void onStartError(Exception e)
    {
        if (mErrorCallback != null)
        {
            if (mErrorCallback.onError(Error.Start, e))
                return;
        }

        Toast.makeText(mActivity, mActivity.getResources().getString(R.string.lib_image_getter_open_album_failed), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int resultCode, Intent data)
    {

    }
}
