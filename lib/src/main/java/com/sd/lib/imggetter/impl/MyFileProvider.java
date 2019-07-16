package com.sd.lib.imggetter.impl;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

public class MyFileProvider extends FileProvider
{
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
