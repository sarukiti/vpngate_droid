package dev.planetdisk.vpngate_droid

import android.app.Application
import com.google.android.material.color.DynamicColors

class VpngateDroid : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}