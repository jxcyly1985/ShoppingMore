package cn.lemon.shopping.model;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: leiyong
 * Date: 14-3-15
 * Time: 下午1:08
 * To change this template use File | Settings | File Templates.
 */
public class RequestEntityDelegator<T> {

    public RequestEntityDelegator() {

    }

    public T getRequestEntity(BaseRequestEntity<T> BaseRequestEntity) {

        return BaseRequestEntity.getRequestEntity();

    }
}
