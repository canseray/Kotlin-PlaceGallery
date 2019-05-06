package com.example.kplacegallery

import android.app.Application
import com.parse.Parse

class StarterApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG)

        Parse.initialize(Parse.Configuration.Builder(this)
                 .applicationId("dTLWZ15rmIHDgIl3F7JS9w66xyvCY2tgJBPbGiaq")
                 .clientKey("6N50dGfrRRzXPCKCrNMzJOfAjcZGnxAUCcRnl3Mi")
                 .server("https://parseapi.back4app.com/")
                 .build()
        )
    }
}