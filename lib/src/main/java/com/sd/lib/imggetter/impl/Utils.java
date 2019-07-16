package com.sd.lib.imggetter.impl;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.UUID;

class Utils
{
    public static File newCameraFile(Context context)
    {
        File dir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        } else
        {
            dir = context.getFilesDir();
        }

        if (dir != null)
            dir = new File(dir, "Camera");

        dir = mkdirs(dir);
        if (dir == null)
        {
            return null;
        }

        final String fileName = UUID.randomUUID().toString() + ".jpg";
        return new File(dir, fileName);
    }

    private static File mkdirs(File dir)
    {
        if (dir == null || dir.exists())
            return dir;

        try
        {
            return dir.mkdirs() ? dir : null;
        } catch (Exception e)
        {
            return null;
        }
    }

    public static Uri getUri(Context context, File file)
    {
        if (file == null)
            throw new IllegalArgumentException("file is null");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            return FileProvider.getUriForFile(context,
                    context.getPackageName() + "." + MyFileProvider.class.getSimpleName().toLowerCase(),
                    file);
        } else
        {
            return Uri.fromFile(file);
        }
    }
}
