package main.kotlin.com.carzuiliam.gatekeeper.core.driver.database

import java.sql.ResultSet

abstract class SqlDatabase(protected open val connectionString: String) {
    protected var isInTransactionMode = false

    abstract fun beginTransaction(): Boolean
    abstract fun commitTransaction(): Boolean
    abstract fun rollbackTransaction(): Boolean
    abstract fun performSQLCommand(sqlCommand: String): Boolean
    abstract fun performSQLQuery(sqlQuery: String): ResultSet?
}