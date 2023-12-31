package main.kotlin.com.carzuiliam.gatekeeper.core.driver.database

import org.sqlite.SQLiteDataSource
import java.sql.Connection
import java.sql.ResultSet

class SqliteDatabase(override val connectionString: String) : SqlDatabase(connectionString) {
    private val dataSource: SQLiteDataSource = SQLiteDataSource()
    private var connection: Connection? = null

    init {
        dataSource.url = connectionString
    }

    override fun beginTransaction(): Boolean {
        try {
            connection = dataSource.connection

            if (connection != null) {
                connection!!.autoCommit = false
                inTransactionMode = true
            }
        } catch (ex: Exception) {
            throw ex
        }

        return true
    }

    override fun commitTransaction(): Boolean {
        try {
            if (connection != null) {
                connection!!.commit()
                connection = null
                inTransactionMode = false
            }
        } catch (ex: Exception) {
            throw ex
        }

        return true
    }

    override fun rollbackTransaction(): Boolean {
        try {
            if (connection != null) {
                connection!!.rollback()
                connection = null
                inTransactionMode = false
            }
        } catch (ex: Exception) {
            throw ex
        }

        return true
    }

    override fun performSQLCommand(sqlCommand: String): Boolean {
        try {
            if (!inTransactionMode) {
                connection = dataSource.connection
            }

            if (connection != null) {
                connection!!.createStatement().execute(sqlCommand)
            }

            if (!inTransactionMode) {
                connection = null
            }
        } catch (ex: Exception) {
            throw ex
        }

        return true
    }

    override fun performSQLQuery(sqlQuery: String): ResultSet? {
        var resultSet: ResultSet? = null

        try {
            if (!inTransactionMode) {
                connection = dataSource.connection
            }

            if (connection != null) {
                resultSet = connection!!.createStatement().executeQuery(sqlQuery)
            }

            if (!inTransactionMode) {
                connection = null
            }
        } catch (ex: Exception) {
            throw ex
        }

        return resultSet
    }
}