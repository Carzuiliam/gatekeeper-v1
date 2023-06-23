package controller

import driver.handler.DatabaseHandler
import enumerable.DatabaseType
import entity.EntityClass
import java.sql.SQLException

open class DatabaseEntity(databaseType: DatabaseType, connectionString: String) {
    private val databaseHandler = DatabaseHandler(databaseType, connectionString)

    fun transaction(): Boolean {
        return try {
            databaseHandler.transaction()
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            false
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            false
        }
    }

    fun commit(): Boolean {
        return try {
            databaseHandler.commit()
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            false
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            false
        }
    }

    fun rollback(): Boolean {
        return try {
            databaseHandler.rollback()
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            false
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            false
        }
    }

    fun create(dataEntity: EntityClass): Boolean {
        return try {
            databaseHandler.create(dataEntity)
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            false
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            false
        }
    }

    fun insert(dataEntity: EntityClass): Boolean {
        return try {
            databaseHandler.insert(dataEntity)
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            false
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            false
        }
    }

    fun update(dataEntity: EntityClass): Boolean {
        return try {
            databaseHandler.update(dataEntity)
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            false
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            false
        }
    }

    fun delete(dataEntity: EntityClass): Boolean {
        return try {
            databaseHandler.delete(dataEntity)
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            false
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            false
        }
    }

    fun select(dataEntity: EntityClass): List<EntityClass> {
        return try {
            databaseHandler.select(dataEntity)
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            listOf()
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            listOf()
        }
    }

    fun join(dataEntity: EntityClass): List<EntityClass> {
        return try {
            databaseHandler.join(dataEntity)
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            listOf()
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            listOf()
        }
    }
}