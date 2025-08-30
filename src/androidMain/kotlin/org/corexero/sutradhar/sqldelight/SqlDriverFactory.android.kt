package org.corexero.sutradhar.sqldelight

import android.content.Context
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.corexero.sutradhar.encryption.EncryptionProvider
import org.corexero.sutradhar.utils.Path
import java.io.File
import net.zetetic.database.sqlcipher.SQLiteDatabase
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory

actual class SqlDriverFactory(
    private val context: Context,
    private val encryptionProvider: EncryptionProvider
) {

    val encryptionKey by lazy { encryptionProvider.getEncryptionKey() }

    actual fun createDriver(
        dbName: Path,
        schema: SqlSchema<QueryResult.Value<Unit>>,
        assetNameToCopyDbFrom: Path?,
    ): SqlDriver {
        val dbFile = context.getDatabasePath(dbName.nameWithExtension)
        dbFile.parentFile?.mkdirs()

        if (!dbFile.exists() && assetNameToCopyDbFrom != null) {
            copyAssetTo(dbFile, assetNameToCopyDbFrom)
        }

        val preEncryptedKey = encryptionKey.toByteArray(Charsets.UTF_8)
        val factory = SupportOpenHelperFactory(preEncryptedKey)

        return AndroidSqliteDriver(schema, context, dbName.nameWithExtension, factory)
    }

    private fun copyAssetTo(outFile: File, assetName: Path) {
        try {
            context.assets.open(assetName.nameWithExtension).use { input ->
                outFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val db = SQLiteDatabase.openDatabase(
                outFile.path,
                encryptionKey,
                null,
                SQLiteDatabase.OPEN_READWRITE,
                null
            )
            db.close()
        } catch (e: Exception) {
            outFile.delete()
            throw RuntimeException("Failed to initialize encrypted database", e)
        }
    }

}