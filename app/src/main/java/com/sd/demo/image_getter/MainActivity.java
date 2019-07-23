package com.sd.demo.image_getter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.sd.lib.imggetter.ImageGetter;
import com.sd.lib.imggetter.impl.FImageGetter;

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
                FImageGetter.album(this).onSuccess(new ImageGetter.SuccessCallback()
                {
                    @Override
                    public void onSuccess(String file)
                    {
                        Log.i(TAG, "album onSuccess:" + file);
                    }
                }).onCancel(new ImageGetter.CancelCallback()
                {
                    @Override
                    public void onCancel()
                    {
                        Log.i(TAG, "album onCancel");
                    }
                }).onError(new ImageGetter.ErrorCallback()
                {
                    @Override
                    public void onError(ImageGetter.Error error, String desc)
                    {
                        Log.i(TAG, "album onError:" + error + " " + desc);
                    }
                }).start();
                break;
            case R.id.btn_camera:
                FImageGetter.camera(this).onSuccess(new ImageGetter.SuccessCallback()
                {
                    @Override
                    public void onSuccess(String file)
                    {
                        Log.i(TAG, "camera onSuccess:" + file);
                    }
                }).onCancel(new ImageGetter.CancelCallback()
                {
                    @Override
                    public void onCancel()
                    {
                        Log.i(TAG, "camera onCancel");
                    }
                }).onError(new ImageGetter.ErrorCallback()
                {
                    @Override
                    public void onError(ImageGetter.Error error, String desc)
                    {
                        Log.i(TAG, "camera onError:" + error + " " + desc);
                    }
                }).start();
                break;
            default:
                break;
        }
    }
}
