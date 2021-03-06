package com.example.commonlibs.mvp;

import androidx.annotation.NonNull;

/**
 * Created by Horrarndoo on 2017/4/25.
 * <p>
 * base presenter
 */

public abstract class BasePresenter<M, V> {
    protected M mIModel;
    protected V mIView;

    /**
     * 返回presenter想持有的Model引用
     *
     * @return presenter持有的Model引用
     */
    protected abstract M getModel();

    /**
     * 绑定IModel和IView的引用
     *
     * @param v view
     */
    public void attachMV(@NonNull V v) {
        this.mIModel = getModel();
        this.mIView = v;
        this.onStart();
    }

    /**
     * 解绑IModel和IView
     */
    public void detachMV() {
        mIView = null;
        mIModel = null;
    }

    /**
     * IView和IModel绑定完成立即执行
     * <p>
     * 实现类实现绑定完成后的逻辑,例如数据初始化等,界面初始化, 更新等
     */
    public abstract void onStart();
}
