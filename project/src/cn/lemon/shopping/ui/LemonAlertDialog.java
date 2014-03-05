package cn.lemon.shopping.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import cn.lemon.shopping.R;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-1-21
 * Time: 下午5:17
 * To change this template use File | Settings | File Templates.
 */
public class LemonAlertDialog {

    private Context mContext;
    private AlertDialog.Builder mBuilder;
    private Bundle mBundle;
    public static final String TITLE = "title";
    public static final String MESSAGE = "message";

    public LemonAlertDialog(Context context) {
        mContext = context;
        mBuilder = new AlertDialog.Builder(mContext);
    }

    public AlertDialog newInstance(Bundle bundle) {
        String titleString = bundle.getString(TITLE);
        String message = bundle.getString(MESSAGE);
        if (titleString != null) {
            View dialogTitleView = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_title, null, false);
            TextView titleTextView = (TextView) dialogTitleView.findViewById(R.id.id_custom_dialog_title);
            titleTextView.setText(titleString);
            mBuilder.setCustomTitle(dialogTitleView);
        }

        if (message != null) {
            mBuilder.setMessage(message);
        }

        return mBuilder.create();

    }

}
