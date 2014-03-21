package cn.lemon.shopping.db;

import java.util.Observable;

import android.content.Context;
import android.os.Message;

import cn.lemon.framework.INotifyInterface;
import cn.lemon.framework.MessageManager;
import cn.lemon.shopping.MessageConstants;
import cn.lemon.shopping.model.MallTotalInfo;

public class LocalSQLiteOperatorManager implements INotifyInterface {

    private Context mContext;
    private MessageManager mMessageManager;
    private LocalSQLiteOperator mLocalSqliteOperator;

    public LocalSQLiteOperatorManager(Context context) {
        mContext = context;
        mMessageManager = MessageManager.getInstance();
        mLocalSqliteOperator = LocalSQLiteOperator.getInstance(mContext);
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        Message msg = (Message) arg1;

        switch (msg.what) {
            case MessageConstants.MSG_MALL_DATA_RETURN:
                MallTotalInfo mallTotalInfo = (MallTotalInfo) msg.obj;
                insertData(mallTotalInfo);
                break;

            default:
                break;
        }
    }

    private void insertData(MallTotalInfo mallTotalInfo) {

        new Thread(new DatabaseRunnable(mallTotalInfo)).start();
    }

    private class DatabaseRunnable implements Runnable {

        private MallTotalInfo mMallTotalInfo;

        public DatabaseRunnable(MallTotalInfo mallTotalInfo) {
            mMallTotalInfo = mallTotalInfo;
        }

        @Override
        public void run() {
            mLocalSqliteOperator.insertMallTotalInfo(mMallTotalInfo);
        }

    }

    @Override
    public void addObserver() {
        mMessageManager.addObserver(MessageConstants.MSG_MALL_DATA_RETURN, this);
    }

    @Override
    public void deleteObserver() {
        mMessageManager.deleteObserver(MessageConstants.MSG_MALL_DATA_RETURN, this);
    }

}
