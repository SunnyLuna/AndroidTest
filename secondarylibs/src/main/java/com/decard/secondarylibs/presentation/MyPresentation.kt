package com.decard.secondaryterminal.presentation

import android.app.Presentation
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Display
import androidx.lifecycle.*

 open class MyPresentation(outerContext: Context?, display: Display?) :
    Presentation(outerContext, display), LifecycleOwner, ViewModelStoreOwner {
    private var mViewModelStore: ViewModelStore? = null
    private var mLifecycle: Lifecycle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lifecycle = lifecycle
            ?: throw IllegalStateException(
                "getLifecycle() returned null in ComponentActivity's "
                        + "constructor. Please make sure you are lazily constructing your Lifecycle "
                        + "in the first call to getLifecycle() rather than relying on field "
                        + "initialization."
            )
        //noinspection ConstantConditions
        if (Build.VERSION.SDK_INT >= 19) {
            getLifecycle().addObserver(LifecycleEventObserver { source, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    val window = window
                    val decor =
                        window?.peekDecorView()
                    decor?.cancelPendingInputEvents()
                }
            })
        }

    }

    private val mLifecycleRegistry =
        LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    override fun getViewModelStore(): ViewModelStore {
        if (mViewModelStore == null) {
            mViewModelStore = ViewModelStore()
        }
        return mViewModelStore!!
    }
}