package main.kotlin.com.carzuiliam.gatekeeper.core.controller

import main.kotlin.com.carzuiliam.gatekeeper.core.driver.handler.DatabaseHandler
import main.kotlin.com.carzuiliam.gatekeeper.core.enumerable.EntityDatabaseType
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityClass
import java.sql.SQLException

open class DatabaseEntity(entityDatabaseType: EntityDatabaseType, connectionString: String) {
    private val databaseHandler = DatabaseHandler(entityDatabaseType, connectionString)

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

    fun beginTransaction(): Boolean {
        return try {
            databaseHandler.beginTransaction()
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            false
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            false
        }
    }

    fun commitTransaction(): Boolean {
        return try {
            databaseHandler.commitTransaction()
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            false
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            false
        }
    }

    fun rollbackTransaction(): Boolean {
        return try {
            databaseHandler.rollbackTransaction()
        } catch (sex: SQLException) {
            println("Failed to execute SQL: ${sex.message}")
            false
        } catch (ex: Exception) {
            println("Failed to perform command: ${ex.message}")
            false
        }
    }
}