package com.toandoan.ipcclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import com.toandoan.icpsample.IMusicService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String MUSIC_ACTION = "com.toandoan.icpsample.service.MusicService.BIND";
    private static final String MUSIC_PACKAGE = "com.toandoan.icpsample";

    private IMusicService mService;
    private boolean mIsServiceConnected;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = IMusicService.Stub.asInterface(iBinder);
            mIsServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindService();
        initViews();
    }

    private void bindService() {
        Intent intent = new Intent(MUSIC_ACTION);
        intent.setPackage(MUSIC_PACKAGE);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initViews() {
        findViewById(R.id.button_pause).setOnClickListener(this);
        findViewById(R.id.button_play).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!mIsServiceConnected) {
            return;
        }
        switch (view.getId()) {
            case R.id.button_play:
                try {
                    mService.playSong();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.button_pause:
                try {
                    mService.pause();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}
