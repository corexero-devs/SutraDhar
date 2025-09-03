// Declarations  
agp = "8.11.0"
sqlDelight = "2.1.0"
datastore = "1.1.7"
kotlinxSerialization = "1.9.0"
ktor = "2.3.8"
firebaseBom = "34.1.0"
playServicesLocation = "21.3.0"
slf4jAndroid = "1.7.36"
sqlcipherAndroid = "4.10.0"
sqlite = "2.5.2"
runner = "1.7.0"
androidx-test-junit = "1.2.1"
androidx-activityCompose = "1.10.1"
core = "1.7.0"
playService = "4.4.3"
googleFirebaseCrashlytics = "3.0.6"

koin = "4.1.0"
koinComposeMultiplatform = "4.1.0"

//libraries
play-services-location = { module = "com.google.android.gms:play-services-location", version.ref = "playServicesLocation" }
slf4j-android = { module = "org.slf4j:slf4j-android", version.ref = "slf4jAndroid" }
sqlcipher-android = { module = "net.zetetic:sqlcipher-android", version.ref = "sqlcipherAndroid" }
kotlin-stdlib = { group = "org.jetbrains.kotlin", name = "kotlin-stdlib", version.ref = "kotlin" }
sqlDelight-common = { module = "app.cash.sqldelight:primitive-adapters", version.ref = "sqlDelight" }
sqlDelight-android = { module = "app.cash.sqldelight:android-driver", version.ref = "sqlDelight" }
sqlDelight-ios = { module = "app.cash.sqldelight:native-driver", version.ref = "sqlDelight" }
datastore = { module = "androidx.datastore:datastore", version.ref = "datastore" }
datastore-preferences = { module = "androidx.datastore:datastore-preferences", version.ref = "datastore" }
kotlinx-serilization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinxSerialization" }
ktor-client-core = { module = "io.ktor:ktor-client-core", version.ref = "ktor" }
ktor-client-okhttp = { module = "io.ktor:ktor-client-okhttp", version.ref = "ktor" }
ktor-client-darwin = { module = "io.ktor:ktor-client-darwin", version.ref = "ktor" }
ktor-client-logging = { module = "io.ktor:ktor-client-logging", version.ref = "ktor" }
ktor-client-content-negotiation = { module = "io.ktor:ktor-client-content-negotiation", version.ref = "ktor" }
ktor-serialization-kotlinx-json = { module = "io.ktor:ktor-serialization-kotlinx-json", version.ref = "ktor" }
firebase-bom = { module = "com.google.firebase:firebase-bom", version.ref = "firebaseBom" }
firebase-analytics = { module = "com.google.firebase:firebase-analytics" }
firebase-crashlytics = { module = "com.google.firebase:firebase-crashlytics" }
firebase-config = { module = "com.google.firebase:firebase-config" }
firebase-messaging = { module = "com.google.firebase:firebase-messaging" }
firebase-inappmessaging-display = { module = "com.google.firebase:firebase-inappmessaging-display" }
androidx-sqlite = { module = "androidx.sqlite:sqlite", version.ref = "sqlite" }
androidx-runner = { group = "androidx.test", name = "runner", version.ref = "runner" }
androidx-test-junit = { group = "androidx.test.ext", name = "junit", version.ref = "androidx-test-junit" }
androidx-activity = { module = "androidx.activity:activity", version.ref = "androidx-activityCompose" }
androidx-core = { group = "androidx.test", name = "core", version.ref = "core" }

koin-android = { module = "io.insert-koin:koin-android", version.ref = "koin" }
koin-androidx-compose = { module = "io.insert-koin:koin-androidx-compose", version.ref = "koin" }
koin-core = { module = "io.insert-koin:koin-core", version.ref = "koin" }
koin-compose = { module = "io.insert-koin:koin-compose", version.ref = "koinComposeMultiplatform" }
koin-compose-viewmodel = { module = "io.insert-koin:koin-compose-viewmodel", version.ref = "koinComposeMultiplatform" }


//plugins
androidKotlinMultiplatformLibrary = { id = "com.android.kotlin.multiplatform.library", version.ref = "agp" }
androidLint = { id = "com.android.lint", version.ref = "agp" }
kotlinSerialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin" }
goggleService = { id = "com.google.gms.google-services", version.ref = "playService" }
googleFirebaseCrashlytics = { id = "com.google.firebase.crashlytics", version.ref = "googleFirebaseCrashlytics" }