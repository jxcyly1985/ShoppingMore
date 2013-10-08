package cn.lemon.shopping.upgrade;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

final class InstallManager {

    private static InstallManager sInstance;
    private Context mContext;
    private InstallBroadcastReceiver mInstallBroadcastReceiver;

    private InstallManager() {

    }

    public synchronized static InstallManager getInstance() {

        if (sInstance == null) {

            sInstance = new InstallManager();
        }
        return sInstance;
    }

    public void init(Context context) {

        mContext = context;
        mInstallBroadcastReceiver = new InstallBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        mContext.registerReceiver(mInstallBroadcastReceiver, intentFilter);
    }

    public void install(String path) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

}
