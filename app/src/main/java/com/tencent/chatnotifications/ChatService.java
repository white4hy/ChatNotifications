package com.tencent.chatnotifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.socks.library.KLog;


/**
 * 通知监听服务
 */
public class ChatService extends NotificationListenerService {


    //来通知时的调用
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
//        StatusBarNotification[] activeNotifications = getActiveNotifications();
//        for (int i = 0; i < activeNotifications.length; i++) {
//            StatusBarNotification s = activeNotifications[i];
//            KLog.e("当前状态栏："+ s.toString());
//        }
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }
        KLog.e(" when" + String.valueOf(notification.when));
        @SuppressLint({"NewApi", "LocalSuppress"}) Bundle extras = notification.extras;
        String content = "";
        String title = "";
        if (extras != null) {
            // 获取通知标题
            title = extras.getString(Notification.EXTRA_TITLE, "");
            KLog.e("获取通知标题：" + title);
            // 获取通知内容
            content = extras.getString(Notification.EXTRA_TEXT, "");
            KLog.e("获取通知内容：" + content);

            KLog.e("获取通知包名：" + sbn.getPackageName());

        }
        switch (sbn.getPackageName()) {
            case "com.tencent.mm":
                KLog.e("微信" + content);
                break;
            case "com.android.mms":
                KLog.e("短信息" + content);
                break;
            case "com.tencent.mqq":
                KLog.e("mqq" + content);
                break;
            case "com.tencent.mobileqq":
                KLog.e("mobileqq" + content);
                String keyword = (String) SPUtils.get(getApplicationContext(), "keyword", "");
                KLog.e("keyword" + keyword);
                if (!TextUtils.isEmpty(keyword)) {
                    if (content.contains(keyword)) {
                        Intent i = new Intent("com.notifymgr.NOTIFICATION_LISTENER");
                        sendBroadcast(i);
                    }
                    if (title.contains(keyword)) {
                        Intent i = new Intent("com.notifymgr.NOTIFICATION_LISTENER");
                        sendBroadcast(i);
                    }
                }
                break;
            case "com.tencent.tim":
                KLog.e("tim" + content);
                break;
            case "com.android.incallui":
                KLog.e("电话" + content);
                break;
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}