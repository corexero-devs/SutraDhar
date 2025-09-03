package org.corexero.sutradhar.di

import org.corexero.sutradhar.datastore.DataStoreManager
import org.corexero.sutradhar.location.LiveLocationRepository
import org.corexero.sutradhar.network.HttpClientProvider
import org.corexero.sutradhar.network.SutradharRepository
import org.corexero.sutradhar.network.SutradharRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sutraDharModule = module {
    // Define your dependencies here
    singleOf(::LiveLocationRepository)

    single {
        HttpClientProvider().client
    }

    singleOf(::DataStoreManager)

    singleOf(::SutradharRepositoryImpl)
        .bind<SutradharRepository>()

    includes(platformModule)

}

expect val platformModule: Module
