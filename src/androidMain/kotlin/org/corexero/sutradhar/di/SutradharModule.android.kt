package org.corexero.sutradhar.di

import org.corexero.sutradhar.datastore.DataStoreFactory
import org.corexero.sutradhar.location.LocationRepository
import org.corexero.sutradhar.sqldelight.SqlDriverFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        singleOf(::DataStoreFactory)
        singleOf(::LocationRepository)
        singleOf(::SqlDriverFactory)
    }