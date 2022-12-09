# Setting koin android using viewmodel and Koin dependency injection Where should I look for to get it working?

## **Application** : MainActivity.kt

```
package com.cheroliv.koin

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

interface SampleService {
    fun hello(): String
}

class SampleServiceInMemory : SampleService {
    override fun hello(): String = "hello"
}

class SampleViewModel(val sampleService: SampleService) : ViewModel() {
    fun sayHello(activity: AppCompatActivity) {
        Toast.makeText(
            activity,
            sampleService.hello(),
            Toast.LENGTH_SHORT
        ).show()
    }
}

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@SampleApplication)
            modules(sampleModule)
        }
    }
}

val sampleModule = module {
    single<SampleService> { SampleServiceInMemory() }
    viewModel { SampleViewModel(get()) }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getViewModel<SampleViewModel>().sayHello(this)
    }
}
```


## **layout**: activity_main.xml


```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```


**Manifest**:

```
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.KoinSample"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>

            <meta-data
                android:name="android.app.lib_name"
                android:value="" />
        </activity>
    </application>

</manifest>
```

## **build**: root

```
plugins {
    id 'com.android.application' version '7.3.1' apply false
    id 'com.android.library' version '7.3.1' apply false
    id 'org.jetbrains.kotlin.android' version '1.7.20' apply false
}
```


## **build**: app

```
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.cheroliv.koin'
    compileSdk 33

    defaultConfig {
        applicationId "com.cheroliv.koin"
        minSdk 23
        targetSdk 33
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {

    implementation 'androidx.core:core-ktx:1.9.0'
    implementation 'androidx.appcompat:appcompat:1.5.1'
    implementation 'com.google.android.material:material:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.4'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.0'
    //Koin Core
    implementation "io.insert-koin:koin-core:$koin_version"
    testImplementation "io.insert-koin:koin-test:$koin_version"
    testImplementation "io.insert-koin:koin-test-junit4:$koin_version"
    // Koin Android main features for Android
    implementation "io.insert-koin:koin-android:$koin_android_version"
    // Koin Android Jetpack WorkManager
    implementation "io.insert-koin:koin-androidx-workmanager:$koin_android_version"
    // Koin Android Navigation Graph
    implementation "io.insert-koin:koin-androidx-navigation:$koin_android_version"
}
```


## **Properties**

```
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=official
android.nonTransitiveRClass=true
koin_version=3.2.2
koin_android_version=3.3.0
```





## **android console output**

```
E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.cheroliv.koin, PID: 19388
    java.lang.RuntimeException: Unable to start activity ComponentInfo{com.cheroliv.koin/com.cheroliv.koin.MainActivity}: java.lang.IllegalStateException: KoinApplication has not been started
        at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3556)
        at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3703)
        at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:83)
        at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:135)
        at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:95)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2216)
        at android.os.Handler.dispatchMessage(Handler.java:107)
        at android.os.Looper.loop(Looper.java:237)
        at android.app.ActivityThread.main(ActivityThread.java:7948)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1075)
     Caused by: java.lang.IllegalStateException: KoinApplication has not been started
        at org.koin.core.context.GlobalContext.get(GlobalContext.kt:36)
        at org.koin.android.ext.android.AndroidKoinScopeExtKt.getKoinScope(AndroidKoinScopeExt.kt:18)
        at com.cheroliv.koin.MainActivity.onCreate(MainActivity.kt:67)
        at android.app.Activity.performCreate(Activity.java:7955)
        at android.app.Activity.performCreate(Activity.java:7944)
        at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1307)
        at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:3531)
        at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3703) 
        at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:83) 
        at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:135) 
        at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:95) 
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2216) 
        at android.os.Handler.dispatchMessage(Handler.java:107) 
        at android.os.Looper.loop(Looper.java:237) 
        at android.app.ActivityThread.main(ActivityThread.java:7948) 
        at java.lang.reflect.Method.invoke(Native Method) 
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493) 
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1075) 
```
