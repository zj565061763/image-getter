package com.sd.lib.imggetter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;

public class FImageGetter
{
    public static final int REQUEST_CODE_GET_IMAGE_FROM_CAMERA = 16542;
    public static final int REQUEST_CODE_GET_IMAGE_FROM_ALBUM = REQUEST_CODE_GET_IMAGE_FROM_CAMERA + 1;

    private final Activity mActivity;
    private File mCameraImageDir;
    private File mCameraImageFile;

    private Callback mCallback;

    public FImageGetter(Activity activity)
    {
        if (activity == null)
            throw new IllegalArgumentException("activity is null");

        mActivity = activity;
    }

    /**
     * 设置回调对象
     *
     * @param callback
     */
    public void setCallback(Callback callback)
    {
        mCallback = callback;
    }

    private File getCameraImageDir()
    {
        if (mCameraImageDir == null)
        {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (dir == null)
                dir = mActivity.getCacheDir();

            mCameraImageDir = dir;
        }

        if (mCameraImageDir.exists() || mCameraImageDir.mkdirs())
            return mCameraImageDir;

        return null;
    }

    /**
     * 打开相册获取图片
     */
    public void getImageFromAlbum()
    {
        try
        {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mActivity.startActivityForResult(intent, REQUEST_CODE_GET_IMAGE_FROM_ALBUM);
        } catch (Exception e)
        {
            mCallback.onError(Error.OpenAlbum, String.valueOf(e));
        }
    }

    /**
     * 打开相机拍照
     */
    public void getImageFromCamera()
    {
        if (getCameraImageDir() == null)
        {
            mCallback.onError(Error.CreateDirectory, "创建目录失败");
            return;
        }

        try
        {
            mCameraImageFile = newFileUnderDir(getCameraImageDir(), ".jpg");
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileToUri(mCameraImageFile));
            mActivity.startActivityForResult(intent, REQUEST_CODE_GET_IMAGE_FROM_CAMERA);
        } catch (Exception e)
        {
            mCallback.onError(Error.OpenCamera, String.valueOf(e));
        }
    }

    /**
     * 外部Activity需要调用
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_GET_IMAGE_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK)
                {
                    scanFile(mActivity, mCameraImageFile);
                    mCallback.onResultFromCamera(mCameraImageFile);
                } else if (resultCode == Activity.RESULT_CANCELED)
                {
                    mCallback.onError(Error.ResultCameraCancelled, "取消获取图片");
                } else
                {
                    mCallback.onError(Error.ResultCamera, "获取图片失败");
                }
                break;
            case REQUEST_CODE_GET_IMAGE_FROM_ALBUM:
                if (resultCode == Activity.RESULT_OK)
                {
                    if (data == null)
                    {
                        mCallback.onError(Error.ResultAlbum, "获取图片失败");
                        return;
                    }

                    final Uri uri = data.getData();
                    if (uri == null)
                    {
                        mCallback.onError(Error.ResultAlbum, "获取图片失败");
                        return;
                    }

                    String path = null;
                    try
                    {
                        path = getDataColumn(mActivity, uri, null, null);
                    } catch (Exception e)
                    {
                        mCallback.onError(Error.ResultAlbum, "获取图片失败:" + e);
                        return;
                    }

                    if (TextUtils.isEmpty(path))
                    {
                        mCallback.onError(Error.ResultAlbum, "获取图片失败");
                        return;
                    }

                    mCallback.onResultFromAlbum(new File(path));
                } else if (resultCode == Activity.RESULT_CANCELED)
                {
                    mCallback.onError(Error.ResultAlbumCancelled, "取消获取图片");
                } else
                {
                    mCallback.onError(Error.ResultAlbum, "获取图片失败");
                }
                break;
            default:
                break;
        }
    }

    protected Uri fileToUri(File file)
    {
        return Uri.fromFile(file);
    }

    private static File newFileUnderDir(File dir, String ext)
    {
        if (dir == null)
            return null;

        if (ext == null)
            ext = "";

        long current = System.currentTimeMillis();
        File file = new File(dir, current + ext);
        while (file.exists())
        {
            current++;
            file = new File(dir, current + ext);
        }
        return file;
    }

    private void scanFile(Context context, File file)
    {
        if (file == null || !file.exists())
            return;

        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(fileToUri(file));
        context.sendBroadcast(intent);
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

    public interface Callback
    {
        void onResultFromAlbum(File file);

        void onResultFromCamera(File file);

        void onError(Error error, String desc);
    }

    public enum Error
    {
        /**
         * 创建目录失败
         */
        CreateDirectory,
        /**
         * 打开相册失败
         */
        OpenAlbum,
        /**
         * 调用摄像头失败
         */
        OpenCamera,
        /**
         * 相册获取图片失败
         */
        ResultAlbum,
        /**
         * 拍照获取图片失败
         */
        ResultCamera,
        /**
         * 取消获取相册图片
         */
        ResultAlbumCancelled,
        /**
         * 取消拍照获取图片
         */
        ResultCameraCancelled,
    }
}
