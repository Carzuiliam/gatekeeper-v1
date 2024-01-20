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
                isInTransactionMode = true
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
                isInTransactionMode = false
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
                isInTransactionMode = false
            }
        } catch (ex: Exception) {
            throw ex
        }

        return true
    }

    override fun performSQLCommand(sqlCommand: String): Boolean {
        try {
            if (!isInTransactionMode) {
                connection = dataSource.connection
            }

            if (connection != null) {
                connection!!.createStatement().execute(sqlCommand)
            }

            if (!isInTransactionMode) {
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
            if (!isInTransactionMode) {
                connection = dataSource.connection
            }

            if (connection != null) {
                resultSet = connection!!.createStatement().executeQuery(sqlQuery)
            }

            if (!isInTransactionMode) {
                connection = null
            }
        } catch (ex: Exception) {
            throw ex
        }

        return resultSet
    }
}