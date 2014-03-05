package cn.lemon.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import cn.lemon.shopping.model.ModelUtils;

import java.io.File;

public class StaticUtils {


    public static class StaticUtilsHolder {
        protected static StaticUtils mInstance = new StaticUtils();
    }

    public static StaticUtils getInstance() {
        return StaticUtilsHolder.mInstance;
    }

    public void init(Context context) {
        mAppContext = context;
    }

    private Context mAppContext;

    public File getAdFile() {
        File appFileDir = mAppContext.getFilesDir();
        String adFilePath = appFileDir.getAbsolutePath() + ModelUtils.AdFile;
        File adFile = new File(adFilePath);
        return adFile;
    }

    public int getColor(String hexColor) {
        String colorString = hexColor.substring(1);
        try {
            return Integer.valueOf(colorString, 16) | 0xff000000;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void toSetting(Context context) {

        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }
}
