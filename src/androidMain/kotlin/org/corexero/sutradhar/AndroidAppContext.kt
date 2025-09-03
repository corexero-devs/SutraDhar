package org.corexero.sutradhar

import android.content.Context

class AndroidAppContext(
    private val appContext: Context
) : AppContext {
    fun get(): Context = appContext.applicationContext
}
