package cn.lemon.utils;

import android.content.Context;
import cn.lemon.shopping.model.ModelUtils;

import java.io.File;

public class StaticUtils {


    public static class StaticUtilsHolder{
        protected static StaticUtils mInstance = new StaticUtils();
    }

    public static StaticUtils getInstance(){
        return StaticUtilsHolder.mInstance;
    }

    public void init(Context context){
        mAppContext = context;
    }
    private Context mAppContext;

    public File getAdFile(){
        File appFileDir = mAppContext.getFilesDir();
        String adFilePath = appFileDir.getAbsolutePath() + ModelUtils.AdFile;
        File adFile = new File(adFilePath);
        return adFile;
    }

    public int getColor(String hexColor){
        String colorString = hexColor.substring(1);
        try{
            return Integer.valueOf(colorString, 16);
        }catch (NumberFormatException e){
            return 0;
        }
    }
}
