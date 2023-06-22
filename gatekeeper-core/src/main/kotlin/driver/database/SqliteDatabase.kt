package driver.database

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import org.sqlite.SQLiteDataSource

class SqliteDatabase(override val connectionString: String) : SqlDatabase(connectionString) {
    private val dataSource: SQLiteDataSource = SQLiteDataSource()
    private var connection: Connection? = null

    init {
        dataSource.url = connectionString
    }

    override fun performSQLCommand(sqlCommand: String): Boolean {
        try {
            connection = dataSource.connection

            if (connection != null) {
                connection!!.createStatement().execute(sqlCommand)
            }
        } catch (ex: Exception) {
            throw ex
        }

        return true
    }

    override fun performSQLQuery(sqlQuery: String): ResultSet? {
        try {
            connection = dataSource.connection

            if (connection != null) {
                return connection!!.createStatement().executeQuery(sqlQuery)
            }
        } catch (ex: Exception) {
            throw ex
        }

        return null
    }
}