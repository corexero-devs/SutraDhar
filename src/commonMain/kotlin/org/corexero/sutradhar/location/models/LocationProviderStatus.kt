package org.corexero.sutradhar.location.models

sealed interface LocationProviderStatus {

    data object NoProviderEnabled : LocationProviderStatus

    data object ProviderEnabled : LocationProviderStatus

}