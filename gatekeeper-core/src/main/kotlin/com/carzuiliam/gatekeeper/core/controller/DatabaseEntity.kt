package com.carzuiliam.gatekeeper.core.controller

import com.carzuiliam.gatekeeper.core.driver.handler.DatabaseHandler
import com.carzuiliam.gatekeeper.core.enumerable.EntityDatabaseType
import com.carzuiliam.gatekeeper.core.entity.EntityClass
import java.sql.SQLException

open class DatabaseEntity(entityDatabaseType: EntityDatabaseType, connectionString: String) {
    private val databaseHandler = DatabaseHandler(entityDatabaseType, connectionString)

    /**
     *  Starts a new transaction in the current [DatabaseEntity].
     *
     * @return  "true" if the transaction was opened successfully; "false" otherwise.
     */
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

    /**
     *  Commits all the operations performed in a transaction started in the current [DatabaseEntity].
     *
     * @return  "true" if the commit was performed successfully; "false" otherwise.
     */
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

    /**
     *  Rollbacks all the operations performed in a transaction started the current [DatabaseEntity].
     *
     * @return  "true" if the rollback was performed successfully; "false" otherwise.
     */
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

    /**
     *  Creates a new entity (table) into the database.
     *
     *  In general, if the entity already exists, it will not be replaced -- but the method stills will return "true".
     *
     * @param   dataEntity The [EntityClass] corresponding to the entity to be created.
     * @return  "true" if the operation was performed successfully; "false" otherwise.
     */
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

    /**
     *  Inserts a new record into the entity (table) in the database.
     *
     *  The values of the record must be set using the method [EntityClass.set]; unsetted values are ignored.
     *
     * @param   dataEntity The [EntityClass] containing the information to be inserted.
     * @return  "true" if the operation was performed successfully; "false" otherwise.
     */
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

    /**
     *  Updates an existing record in the entity (table), in the database.
     *
     *  The values of the record must be set using the [EntityClass.set] method, and the filters must be set using the
     * [EntityClass.setFilter] method; unsetted values are ignored.
     *
     * @param   dataEntity The [EntityClass] containing the information to be updated.
     * @return  "true" if the operation was performed successfully; "false" otherwise.
     */
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

    /**
     *  Deletes  an existing record in the entity (table), in the database.
     *
     *  The filters for the record must be set using the [EntityClass.setFilter] method; unsetted values are ignored.
     *
     * @param   dataEntity The [EntityClass] containing the information to be deleted.
     * @return  "true" if the operation was performed successfully; "false" otherwise.
     */
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

    /**
     *  Selects a set of existing record from the entity (table), in the database.
     *
     *  The filters for the records must be set using the [EntityClass.setFilter] method; unsetted values are ignored.
     *
     * @param   dataEntity The [EntityClass] containing the information to be deleted.
     * @return  A set of [EntityClass] with the records values set in the [EntityClass.entityRecord] field.
     */
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

    /**
     *  Performs a select join of existing record from one or more entities (tables) of the database.
     *
     *  The filters for the records must be set using the [EntityClass.setFilter] method; unsetted values are ignored.
     *
     * @param   dataEntity The [EntityClass] containing the information to be deleted.
     * @return  A set of [EntityClass] with the records values set in the [EntityClass.entityRecord] and in the [EntityClass.entityRelations] field.
     */
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