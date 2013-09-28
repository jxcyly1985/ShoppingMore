package cn.lemon.framework;

import java.util.Observer;

/**
 * 
 * @author leiyong
 */
public interface INotifyInterface extends Observer {

    public void addObserver();

    public void deleteObserver();
}
