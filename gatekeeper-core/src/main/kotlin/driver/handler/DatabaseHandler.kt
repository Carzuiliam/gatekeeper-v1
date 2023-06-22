package driver.handler

import driver.command.SqlCommand
import driver.command.SqliteCommand
import driver.database.SqlDatabase
import driver.database.SqliteDatabase
import enumerable.AttributeType
import enumerable.DatabaseType
import java.sql.ResultSet
import model.DataEntity
import model.DataField
import model.DataRecord

class DatabaseHandler(databaseType: DatabaseType, connectionString: String) {
    private var sqlCommand: SqlCommand
    private var sqlDatabase: SqlDatabase

    init {
        when (databaseType) {
            DatabaseType.SQLITE -> {
                sqlCommand = SqliteCommand()
                sqlDatabase = SqliteDatabase(connectionString)
            }
        }
    }

    fun create(dataEntity: DataEntity): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.create(dataEntity))
    }

    fun insert(dataEntity: DataEntity): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.insert(dataEntity))
    }

    fun update(dataEntity: DataEntity): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.update(dataEntity))
    }

    fun delete(dataEntity: DataEntity): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.delete(dataEntity))
    }

    fun select(dataEntity: DataEntity): MutableList<DataRecord> {
        val resultSet = sqlDatabase.performSQLQuery(sqlCommand.select(dataEntity))
        return resultSet?.let { dumpData(dataEntity, it) } ?: mutableListOf()
    }

    private fun dumpData(dataEntity: DataEntity, resultSet: ResultSet): MutableList<DataRecord> {
        val dataRecords = mutableListOf<DataRecord>()

        while (resultSet.next()) {
            val dataRecord = DataRecord()

            dataEntity.dataAttributes.forEachIndexed { index, attr ->
                val value = when (attr.type) {
                    AttributeType.INTEGER ->  resultSet.getInt(index + 1)
                    AttributeType.DOUBLE -> resultSet.getDouble(index + 1)
                    AttributeType.STRING -> resultSet.getString(index + 1)
                    AttributeType.DATETIME -> resultSet.getDate(index + 1)
                }

                dataRecord.dataFields.add(DataField(attr, value))
            }

            dataRecords.add(dataRecord)
        }

        return dataRecords
    }
}