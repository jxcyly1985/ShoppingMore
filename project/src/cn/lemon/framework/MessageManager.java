package cn.lemon.framework;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * @author leiyong
 */
public class MessageManager {

    private Hashtable<Integer, MessageObservable> mMapObservable;

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            MessageManager.this.notify(msg);
        }
    };

    protected MessageManager() {

        mMapObservable = new Hashtable<Integer, MessageObservable>();
    }

    private static class MessageManagerInner {

        static MessageManager sInstance = new MessageManager();
    }

    public static MessageManager getInstance() {
        return MessageManagerInner.sInstance;
    }

    /**
     * addObserver must called by ui thread if not may cause ConcurrentModificationException Observable
     * addObserver method is not implement by iterator
     *
     * @param what
     * @param observer
     */
    public void addObserver(Integer what, Observer observer) {
        MessageObservable observable;
        if (mMapObservable.containsKey(what)) {
            observable = mMapObservable.get(what);
        } else {
            observable = new MessageObservable();
        }
        observable.addObserver(observer);
        mMapObservable.put(what, observable);
    }

    public void deleteAllObserver() {

        int mapSize = mMapObservable.size();
        for (int i = 0; i < mapSize; ++i) {
            MessageObservable observable = mMapObservable.get(i);
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
    public void deleteObserver(Integer what, Observer observer) {
        if (mMapObservable.containsKey(what)) {
            Observable observable = mMapObservable.get(what);
            observable.deleteObserver(observer);
        }
    }

    public void deleteObserver(Observer observer) {

        Set<Entry<Integer, MessageObservable>> observableEntrys = mMapObservable.entrySet();

        for (Entry<Integer, MessageObservable> entry : observableEntrys) {
            MessageObservable observable = entry.getValue();
            observable.deleteObserver(observer);
        }
    }

    public void notify(Message msg) {
        int what = msg.what;
        Set<Entry<Integer, MessageObservable>> observableEntrys = mMapObservable.entrySet();
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

}
