package cn.lemon.shopping.upgrade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class InstallBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
            UpgradeManager.getInstance().onInstallComplete();
        }

    }

}
