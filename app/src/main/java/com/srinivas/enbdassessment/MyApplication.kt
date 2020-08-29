package com.srinivas.enbdassessment

import android.app.Application
import com.srinivas.enbdassessment.data.db.AppDataBase
import com.srinivas.enbdassessment.data.network.ApiInterface
import com.srinivas.enbdassessment.data.network.NetworkConnectionInterceptor
import com.srinivas.enbdassessment.data.repositories.PixabayRepository
import com.srinivas.enbdassessment.ui.PixabayViewModelFactory
import com.srinivas.enbdassessment.util.Constants
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class MyApplication:Application(),KodeinAware {

    /**
     * used   Kodein for dependency Injection
     */
    override val kodein: Kodein= Kodein.lazy {
        import(androidXModule(this@MyApplication))
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiInterface(instance()) }
        bind() from singleton { AppDataBase(instance()) }
        bind() from singleton { PixabayRepository(instance(), instance()) }
        bind() from provider { PixabayViewModelFactory(instance()) }

        if(BuildConfig.DEBUG) {
            Constants.initStetho(this@MyApplication)
        }
    }

}