@file:Suppress("MemberVisibilityCanBePrivate")

package com.cheroliv.koin

import android.app.Application
import android.os.Bundle
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

interface SampleService {
    fun hello(): String
}

class SampleServiceInMemory : SampleService {
    override fun hello(): String = "hello"
}

class SampleViewModel(
    val sampleService: SampleService
) : ViewModel() {
    fun sayHello(activity: AppCompatActivity) {
        makeText(
            activity,
            sampleService.hello(),
            LENGTH_SHORT
        ).show()
    }
}

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SampleApplication)
            androidFileProperties()
            modules(sampleModule)
        }
    }
}

val sampleModule = module {
    single<SampleService> { SampleServiceInMemory() }
    viewModelOf(::SampleViewModel)
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getViewModel<SampleViewModel>().sayHello(this)
    }
}