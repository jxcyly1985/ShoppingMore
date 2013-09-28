package cn.lemon.framework;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

/**
 * 
 * @author leiyong
 * 
 */
public class MessageManager {

    private Hashtable<Integer, MessageObservable> mMapObervable;

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            MessageManager.this.notify(msg);
        }
    };

    protected MessageManager() {

        mMapObervable = new Hashtable<Integer, MessageObservable>();
    }

    private static class MessageManagerInner {

        static MessageManager mInstance = new MessageManager();
    }

    public static MessageManager getInstance() {
        return MessageManagerInner.mInstance;
    }

    /**
     * addOberver must called by ui thread if not may cause ConcurrentModificationException Observable
     * addObserver method is not implement by iterator
     * 
     * @param what
     * @param observer
     */
    public void addOberver(Integer what, Observer observer) {
        MessageObservable observable;
        if (mMapObervable.containsKey(what)) {
            observable = mMapObervable.get(what);
        } else {
            observable = new MessageObservable();
        }
        observable.addObserver(observer);
        mMapObervable.put(what, observable);
    }

    public void deleteAllOberver() {

        int mapSize = mMapObervable.size();
        for (int i = 0; i < mapSize; ++i) {
            MessageObservable observable = mMapObervable.get(i);
            observable.deleteObservers();
        }
    }

    /**
     * delete observer must called by ui thread if not may cause ConcurrentModificationException Observable
     * deleteObserver method is not implement by iterator
     * 
     * @param what
     * @param observer
     */
    public void deleteOberver(Integer what, Observer observer) {
        if (mMapObervable.containsKey(what)) {
            Observable observable = mMapObervable.get(what);
            observable.deleteObserver(observer);
        }
    }

    public void deleteOberver(Observer observer) {

        Set<Entry<Integer, MessageObservable>> observableEntrys = mMapObervable.entrySet();

        for (Entry<Integer, MessageObservable> entry : observableEntrys) {
            MessageObservable observable = entry.getValue();
            observable.deleteObserver(observer);
        }
    }

    public void notify(Message msg) {
        int what = msg.what;
        Set<Entry<Integer, MessageObservable>> observableEntrys = mMapObervable.entrySet();
        for (Entry<Integer, MessageObservable> entry : observableEntrys) {
            int key = entry.getKey();
            if (key == what) {
                entry.getValue().notifyObservers(msg);
                break;
            }
        }
    }

    public void sendNotifyMessage(Message msg) {
        mMainHandler.sendMessage(msg);
    }

    public void sendNotifyMessageDelay(Message msg, long delayMillis) {
        mMainHandler.sendMessageDelayed(msg, delayMillis);
    }

    /**
     * Name: Description:
     * 
     * @return
     * @return
     * 
     */
    public Handler getHandler() {

        return this.mMainHandler;
    }

    private class MessageObservable extends Observable {

        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }

        public void notifyObservers(Object data) {
            setChanged();
            super.notifyObservers(data);
        }
    }

    public class MainHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            MessageManager.this.notify(msg);
        }

    }

}
