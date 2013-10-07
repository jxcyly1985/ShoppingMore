package cn.lemon.shopping.upgrade;

import java.util.Observable;
import java.util.Observer;

import cn.lemon.shopping.upgrade.UpgradeManager.DownloadStatusE;

final class UpgradeDomainData {

	private Observable mSizeChangeObservable;
	private Observable mStatusChangeObservable;

	protected UpgradeDomainData() {
		mSizeChangeObservable = new Observable();
		mStatusChangeObservable = new Observable();
	}

	protected void addSizeChangeObserver(Observer observer) {
		mSizeChangeObservable.addObserver(observer);
	}

	protected void addStatusChangeObserver(Observer observer) {
		mStatusChangeObservable.addObserver(observer);
	}

	protected void deleteSizeChangeObserver(Observer observer) {
		mSizeChangeObservable.deleteObserver(observer);
	}

	protected void deleteStatusChangeObserver(Observer observer) {
		mStatusChangeObservable.deleteObserver(observer);
	}

	protected void onSizeChange(long size) {
		mSizeChangeObservable.notifyObservers(size);
	}

	protected void onStatusChange(DownloadStatusE status) {
		mStatusChangeObservable.notifyObservers(status);
	}
}
