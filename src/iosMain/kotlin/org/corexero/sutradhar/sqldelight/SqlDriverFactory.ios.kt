package org.corexero.sutradhar.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import kotlinx.cinterop.ExperimentalForeignApi
import org.corexero.sutradhar.utils.Logger
import org.corexero.sutradhar.utils.Path
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSBundle
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual class SqlDriverFactory {

    @OptIn(ExperimentalForeignApi::class)
    actual fun createDriver(
        dbName: Path,
        schema: SqlSchema<QueryResult.Value<Unit>>,
        assetNameToCopyDbFrom: Path?,
    ): SqlDriver {

        val fileManager = NSFileManager.defaultManager


        val appSupportDir = NSSearchPathForDirectoriesInDomains(
            NSApplicationSupportDirectory, NSUserDomainMask, true
        ).firstOrNull() as? String
            ?: throw IllegalStateException(EXCEPTION_APP_SUPPORT_DIR)

        val dbFolder = "$appSupportDir/databases"
        val dbPath = "$dbFolder/${dbName.nameWithExtension}"

        if (!fileManager.fileExistsAtPath(dbFolder)) {
            fileManager.createDirectoryAtPath(dbFolder, true, null, null)
            Logger.debug(TAG, "$DATABASE_CREATED $dbFolder $DATABASE_REPLACE_AND_DELETE")
        }

        if (fileManager.fileExistsAtPath(dbPath)) {
            val fileSize =
                fileManager.attributesOfItemAtPath(dbPath, null)?.get("NSFileSize") as? Long ?: 0
            if (fileSize == 0L) {
                Logger.warn(
                    TAG,
                    "$DATABASE_EMPTY $dbPath. "
                )
                fileManager.removeItemAtPath(dbPath, null) // Delete empty DB
            } else {
                Logger.debug(TAG, "$DATABASE_ALREADY_EXISTS $fileSize bytes")
            }
        }

        assetNameToCopyDbFrom?.let { pathToCopyDbFrom ->
            if (!fileManager.fileExistsAtPath(dbPath)) {
                val bundlePath =
                    NSBundle.mainBundle.pathForResource(pathToCopyDbFrom.nameWithoutExtension, ofType = "sqlite")

                if (bundlePath != null) {
                    Logger.debug(TAG, "$DATABASE_COPYING $bundlePath → $dbPath")

                    val success = fileManager.copyItemAtPath(bundlePath, dbPath, null)
                    if (!success) {
                        throw IllegalStateException(DATABASE_COPY_FAILURE)
                    }
                    Logger.debug(TAG, "$DATABASE_COPY_SUCCESS $dbPath")
                } else {
                    throw IllegalStateException(EXCEPTION_MISSING_BUNDLE)
                }
            } else {
                Logger.debug(TAG, "$DATABASE_ALREADY_EXISTS $dbPath")
            }
        }

        return NativeSqliteDriver(
            schema,
            dbName.nameWithExtension
        )
    }

    companion object {

        private const val TAG = "SqlDriverFactoryImpl"

        // ALL MESSAGES
        private const val DATABASE_ALREADY_EXISTS = "✅ Database already exists at: "
        private const val DATABASE_CREATED = "📂 Created databases directory at: "
        private const val DATABASE_COPYING = "📂 Copying database from: "
        private const val DATABASE_COPY_SUCCESS = "✅ Database successfully copied to: "
        private const val DATABASE_COPY_FAILURE = "❌ Failed to copy database from bundle!"
        private const val DATABASE_EMPTY = "⚠️ Found empty database at: "
        private const val DATABASE_REPLACE_AND_DELETE =
            "Deleting and replacing with preloaded database."

        // EXCEPTION MESSAGES
        private const val EXCEPTION_APP_SUPPORT_DIR = "Failed to get Application Support directory"
        private const val EXCEPTION_MISSING_BUNDLE = "❌ Database file missing from bundle!"


    }

}