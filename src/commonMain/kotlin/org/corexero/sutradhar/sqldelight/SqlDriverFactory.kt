package org.corexero.sutradhar.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import org.corexero.sutradhar.utils.Path

expect class SqlDriverFactory {

    fun createDriver(
        dbName: Path,
        schema: SqlSchema<QueryResult.Value<Unit>>,
        assetNameToCopyDbFrom: Path?,
    ): SqlDriver

}