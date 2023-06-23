package driver.database

import java.sql.ResultSet

abstract class SqlDatabase(protected open val connectionString: String) {
    protected var inTransactionMode = false

    abstract fun beginTransaction(): Boolean
    abstract fun commitTransaction(): Boolean
    abstract fun rollbackTransaction(): Boolean
    abstract fun performSQLCommand(sqlCommand: String): Boolean
    abstract fun performSQLQuery(sqlQuery: String): ResultSet?
}