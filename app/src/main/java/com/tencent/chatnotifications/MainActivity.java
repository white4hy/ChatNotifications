package com.tencent.chatnotifications;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    Switch openChatService;
    Switch openKeyword;
    EditText etKeyword;
    Switch openMusic;
    Switch openCallPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openChatService = (Switch) findViewById(R.id.openChatService);
        openKeyword= (Switch) findViewById(R.id.openKeyword);
        etKeyword= (EditText) findViewById(R.id.etKeyword);
        openMusic= (Switch) findViewById(R.id.openMusic);
        openCallPhone= (Switch) findViewById(R.id.openCallPhone);
        openChatService.setChecked(isNotificationServiceEnable());

        if(isNotificationServiceEnable()){
            toggleNotificationListenerService();
        }else {

        }
        openChatService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                openNotificationListenSettings();
            }
        });
        openKeyword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                etKeyword.setVisibility(b?View.VISIBLE:View.GONE);
                etKeyword.setText((String) SPUtils.get(getApplicationContext(), "keyword", ""));
            }
        });
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SPUtils.put(MainActivity.this,"keyword",etKeyword.getText().toString());
                openMusic.setVisibility(TextUtils.isEmpty(etKeyword.getText().toString())?View.GONE:View.VISIBLE);
                openCallPhone.setVisibility(TextUtils.isEmpty(etKeyword.getText().toString())?View.GONE:View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        NotificationReceiver  nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.notifymgr.NOTIFICATION_LISTENER");
        registerReceiver(nReceiver, filter);

    }


    @Override
    protected void onResume() {
        openChatService.setChecked(isNotificationServiceEnable());
        if(isNotificationServiceEnable()){
            toggleNotificationListenerService();
        }
        super.onResume();
    }

    /**
     * 是否已授权
     *
     * @return
     */
    private boolean isNotificationServiceEnable() {
        return NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName());
    }


    //打开通知监听设置页面
    public void openNotificationListenSettings() {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void toggleNotificationListenerService() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(this, com.tencent.chatnotifications.ChatService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(
                new ComponentName(this, com.tencent.chatnotifications.ChatService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }




    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(openMusic.isChecked()){
                MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.gnzw);
                mediaPlayer.start();
            }

        }

    }

}
