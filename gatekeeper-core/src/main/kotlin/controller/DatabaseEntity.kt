package controller

import driver.handler.DatabaseHandler
import enumerable.DatabaseType
import model.DataEntity

open class DatabaseEntity(databaseType: DatabaseType, connectionString: String) {
    private val databaseHandler = DatabaseHandler(databaseType, connectionString)

    fun create(dataEntity: DataEntity): Boolean {
        return try {
            databaseHandler.create(dataEntity)
        } catch (ex: Exception) {
            false
        }
    }

    fun insert(dataEntity: DataEntity): Boolean {
        return try {
            databaseHandler.insert(dataEntity)
        } catch (ex: Exception) {
            false
        }
    }

    fun update(dataEntity: DataEntity): Boolean {
        return try {
            databaseHandler.update(dataEntity)
        } catch (ex: Exception) {
            false
        }
    }

    fun delete(dataEntity: DataEntity): Boolean {
        return try {
            databaseHandler.delete(dataEntity)
        } catch (ex: Exception) {
            false
        }
    }

    fun select(dataEntity: DataEntity): Boolean {
        return try {
            dataEntity.dataResults = databaseHandler.select(dataEntity)
            true
        } catch (ex: Exception) {
            false
        }
    }
}