package cn.lemon.shopping.model;

public class ShoppingMoreDomainDataManager {

    private ShoppingMoreDomainDataManager() {

    }

    private static class ShoppingMoreDomainDataManagerHolder {

        static ShoppingMoreDomainDataManager sInstance = new ShoppingMoreDomainDataManager();
    }

    public static ShoppingMoreDomainDataManager getInstance() {
        return ShoppingMoreDomainDataManagerHolder.sInstance;
    }
}
