package main.kotlin.com.carzuiliam.gatekeeper.core.driver.handler

import main.kotlin.com.carzuiliam.gatekeeper.core.driver.command.SqlCommand
import main.kotlin.com.carzuiliam.gatekeeper.core.driver.command.SqliteCommand
import main.kotlin.com.carzuiliam.gatekeeper.core.driver.database.SqlDatabase
import main.kotlin.com.carzuiliam.gatekeeper.core.driver.database.SqliteDatabase
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityClass
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityField
import main.kotlin.com.carzuiliam.gatekeeper.core.entity.EntityRelation
import main.kotlin.com.carzuiliam.gatekeeper.core.enumerable.EntityAttributeType
import main.kotlin.com.carzuiliam.gatekeeper.core.enumerable.EntityDatabaseType
import java.sql.ResultSet

class DatabaseHandler(entityDatabaseType: EntityDatabaseType, connectionString: String) {
    private var sqlCommand: SqlCommand
    private var sqlDatabase: SqlDatabase

    init {
        when (entityDatabaseType) {
            EntityDatabaseType.SQLITE -> {
                sqlCommand = SqliteCommand()
                sqlDatabase = SqliteDatabase(connectionString)
            }
        }
    }

    fun transaction(): Boolean {
        return sqlDatabase.beginTransaction()
    }

    fun commit(): Boolean {
        return sqlDatabase.commitTransaction()
    }

    fun rollback(): Boolean {
        return sqlDatabase.rollbackTransaction()
    }

    fun create(dataEntity: EntityClass): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.create(dataEntity))
    }

    fun insert(dataEntity: EntityClass): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.insert(dataEntity))
    }

    fun update(dataEntity: EntityClass): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.update(dataEntity))
    }

    fun delete(dataEntity: EntityClass): Boolean {
        return sqlDatabase.performSQLCommand(sqlCommand.delete(dataEntity))
    }

    fun select(dataEntity: EntityClass): MutableList<EntityClass> {
        val resultSet = sqlDatabase.performSQLQuery(sqlCommand.select(dataEntity))
        return resultSet?.let { dumpSelectData(dataEntity, it) } ?: mutableListOf()
    }

    fun join(dataEntity: EntityClass): MutableList<EntityClass> {
        val resultSet = sqlDatabase.performSQLQuery(sqlCommand.join(dataEntity))
        return resultSet?.let { dumpJoinData(dataEntity, it) } ?: mutableListOf()
    }

    private fun dumpSelectData(dataEntity: EntityClass, resultSet: ResultSet): MutableList<EntityClass> {
        val entityClasses = mutableListOf<EntityClass>()

        while (resultSet.next()) {
            val entityClass = EntityClass(dataEntity.name)

            dataEntity.entityAttributes.forEachIndexed { index, attr ->
                val value = when (attr.type) {
                    EntityAttributeType.INTEGER ->  resultSet.getInt(index + 1)
                    EntityAttributeType.DOUBLE -> resultSet.getDouble(index + 1)
                    EntityAttributeType.STRING -> resultSet.getString(index + 1)
                    EntityAttributeType.DATETIME -> resultSet.getDate(index + 1)
                }

                entityClass.entityRecord.entityFields.add(EntityField(attr, value))
            }

            entityClasses.add(entityClass)
        }

        return entityClasses
    }

    private fun dumpJoinData(dataEntity: EntityClass, resultSet: ResultSet): MutableList<EntityClass> {
        val entityClasses = mutableListOf<EntityClass>()

        while (resultSet.next()) {
            val entityClass = EntityClass(dataEntity.name)
            var index = 1

            while (index <= dataEntity.entityAttributes.size) {
                dataEntity.entityAttributes.forEach { attr ->
                    val value = when (attr.type) {
                        EntityAttributeType.INTEGER ->  resultSet.getInt(index)
                        EntityAttributeType.DOUBLE -> resultSet.getDouble(index)
                        EntityAttributeType.STRING -> resultSet.getString(index)
                        EntityAttributeType.DATETIME -> resultSet.getDate(index)
                    }

                    entityClass.entityRecord.entityFields.add(EntityField(attr, value))
                    index++
                }
            }

            dataEntity.entityRelations.forEach { rlt ->
                val relatedEntity = EntityClass(rlt.entityClass.name)
                val entityRelation = dataEntity.entityRelations.first { rtp -> rtp.entityClass.name == rlt.entityClass.name }

                rlt.entityClass.entityAttributes.forEach { attr ->
                    val value = when (attr.type) {
                        EntityAttributeType.INTEGER ->  resultSet.getInt(index)
                        EntityAttributeType.DOUBLE -> resultSet.getDouble(index)
                        EntityAttributeType.STRING -> resultSet.getString(index)
                        EntityAttributeType.DATETIME -> resultSet.getDate(index)
                    }

                    relatedEntity.entityRecord.entityFields.add(EntityField(attr, value))
                    index++
                }

                entityClass.entityRelations.add(EntityRelation(relatedEntity, entityRelation.entityRelationType))
            }

            entityClasses.add(entityClass)
        }

        return entityClasses
    }
}