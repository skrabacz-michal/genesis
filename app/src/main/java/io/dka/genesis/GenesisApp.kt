package io.dka.genesis

import android.app.Application

class GenesisApp : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        Logger.log("Application created")
    }
}