package driver.database

import java.sql.ResultSet

abstract class SqlDatabase(protected open val connectionString: String) {
    abstract fun performSQLCommand(sqlCommand: String): Boolean
    abstract fun performSQLQuery(sqlQuery: String): ResultSet?
}