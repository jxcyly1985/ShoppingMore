package cn.lemon.framework;

import android.os.Message;

public class FramewokUtils {

    public static Message makeMessage(int what, Object obj, int arg1, int arg2) {

        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        return msg;
    }

}
