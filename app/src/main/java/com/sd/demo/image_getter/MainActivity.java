package com.sd.demo.image_getter;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.sd.lib.imggetter.ImageGetter;
import com.sd.lib.imggetter.impl.AlbumImageGetter;
import com.sd.lib.imggetter.impl.CameraImageGetter;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_album:
                new AlbumImageGetter(this).onSuccess(new ImageGetter.SuccessCallback()
                {
                    @Override
                    public void onSuccess(String file)
                    {
                        Log.i(TAG, "album onSuccess:" + file);
                    }
                }).onCancel(new ImageGetter.CancelCallback()
                {
                    @Override
                    public boolean onCancel()
                    {
                        Log.i(TAG, "album onCancel");
                        return false;
                    }
                }).onError(new ImageGetter.ErrorCallback()
                {
                    @Override
                    public boolean onError(ImageGetter.Error error, Exception e)
                    {
                        Log.i(TAG, "album onError:" + error + " " + e);
                        return false;
                    }
                }).start();
                break;
            case R.id.btn_camera:
                final File file = new File(getCacheDir(), "camera.jpg");
                new CameraImageGetter(this).fileUri(Uri.fromFile(file)).onSuccess(new ImageGetter.SuccessCallback()
                {
                    @Override
                    public void onSuccess(String file)
                    {
                        Log.i(TAG, "camera onSuccess:" + file);
                    }
                }).onCancel(new ImageGetter.CancelCallback()
                {
                    @Override
                    public boolean onCancel()
                    {
                        Log.i(TAG, "camera onCancel");
                        return false;
                    }
                }).onError(new ImageGetter.ErrorCallback()
                {
                    @Override
                    public boolean onError(ImageGetter.Error error, Exception e)
                    {
                        Log.i(TAG, "camera onError:" + error + " " + e);
                        return false;
                    }
                }).start();
                break;
            default:
                break;
        }
    }
}
