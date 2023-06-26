package main.kotlin.com.carzuiliam.gatekeeper.test.test

import com.carzuiliam.gatekeeper.core.controller.DatabaseEntity
import com.carzuiliam.gatekeeper.core.enumerable.RelationType
import main.kotlin.com.carzuiliam.gatekeeper.test.config.AppConfig
import main.kotlin.com.carzuiliam.gatekeeper.test.entities.CarEntity
import main.kotlin.com.carzuiliam.gatekeeper.test.entities.HouseEntity
import main.kotlin.com.carzuiliam.gatekeeper.test.entities.PersonEntity
import org.junit.Assert.assertEquals
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GateKeeperTests {
    @Test
    fun `(01) Creates entities into database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val prsEntity = PersonEntity()
        val houEntity = HouseEntity()
        val carEntity = CarEntity()

        val hSuccess = entityDB.create(houEntity)
        val cSuccess = entityDB.create(carEntity)
        val pSuccess = entityDB.create(prsEntity)

        assertEquals(true, hSuccess && cSuccess && pSuccess)
    }

    @Test
    fun `(02) Inserts a single entity into database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val prsEntity = PersonEntity()

        prsEntity.setFilter(PersonEntity.PRS_ID, 1)

        if (entityDB.select(prsEntity).isEmpty()) {
            prsEntity.clear()

            prsEntity[PersonEntity.PRS_ID] = 1
            prsEntity[PersonEntity.PRS_NAME] = "Carlos Carvalho"
            prsEntity[PersonEntity.PRS_AGE] = 31

            val pSuccess = entityDB.insert(prsEntity)
            prsEntity.clear()

            prsEntity.setFilter(PersonEntity.PRS_ID, 1)
            prsEntity.setFilter(PersonEntity.PRS_NAME, "Carlos Carvalho")
            prsEntity.setFilter(PersonEntity.PRS_AGE, 31)

            assertEquals(true, pSuccess && entityDB.select(prsEntity).size == 1)

        } else {
            assertEquals(true, true)
        }
    }

    @Test
    fun `(03) Inserts a single binded entity value into database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val prsEntity = PersonEntity()

        prsEntity.setFilter(PersonEntity.PRS_ID, 2)

        if (entityDB.select(prsEntity).isEmpty()) {
            prsEntity.clear()

            val houEntity = HouseEntity()

            houEntity[HouseEntity.HOU_ID] = 1
            houEntity[HouseEntity.HOU_ADDRESS] = "Rua A"
            houEntity[HouseEntity.HOU_NUMBER] = 100

            val hSuccess = entityDB.insert(houEntity)
            houEntity.clear()

            prsEntity[PersonEntity.PRS_ID] = 2
            prsEntity[PersonEntity.PRS_NAME] = "Carlos Carvalho (Single)"
            prsEntity[PersonEntity.PRS_AGE] = 31
            prsEntity[HouseEntity.HOU_ID] = 1

            val pSuccess = entityDB.insert(prsEntity)
            prsEntity.clear()

            prsEntity.setFilter(PersonEntity.PRS_ID, 2)
            prsEntity.setFilter(PersonEntity.PRS_NAME, "Carlos Carvalho (Single)")
            prsEntity.setFilter(PersonEntity.PRS_AGE, 31)
            prsEntity.setFilter(HouseEntity.HOU_ID, 1)

            assertEquals(true, hSuccess && pSuccess && entityDB.select(prsEntity).size == 1)

        } else {
            assertEquals(true, true)
        }
    }

    @Test
    fun `(04) Inserts a full binded entity value into database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val prsEntity = PersonEntity()

        prsEntity.setFilter(PersonEntity.PRS_ID, 3)

        if (entityDB.select(prsEntity).isEmpty()) {
            prsEntity.clear()

            val houEntity = HouseEntity()

            houEntity[HouseEntity.HOU_ID] = 2
            houEntity[HouseEntity.HOU_ADDRESS] = "Rua B"
            houEntity[HouseEntity.HOU_NUMBER] = 150

            val hSuccess = entityDB.insert(houEntity)
            houEntity.clear()

            val carEntity = CarEntity()

            carEntity[CarEntity.CAR_ID] = 1
            carEntity[CarEntity.CAR_MODEL] = "Monza"
            carEntity[CarEntity.CAR_YEAR] = 1989
            carEntity[CarEntity.CAR_PRICE] = 5000

            val cSuccess = entityDB.insert(carEntity)
            houEntity.clear()

            prsEntity[PersonEntity.PRS_ID] = 3
            prsEntity[PersonEntity.PRS_NAME] = "Carlos Carvalho (Full)"
            prsEntity[PersonEntity.PRS_AGE] = 31
            prsEntity[HouseEntity.HOU_ID] = 2
            prsEntity[CarEntity.CAR_ID] = 1

            val pSuccess = entityDB.insert(prsEntity)
            prsEntity.clear()

            prsEntity.setFilter(PersonEntity.PRS_ID, 3)
            prsEntity.setFilter(PersonEntity.PRS_NAME, "Carlos Carvalho (Full)")
            prsEntity.setFilter(PersonEntity.PRS_AGE, 31)
            prsEntity.setFilter(HouseEntity.HOU_ID, 2)
            prsEntity.setFilter(CarEntity.CAR_ID, 1)

            assertEquals(true, hSuccess && cSuccess && pSuccess && entityDB.select(prsEntity).size == 1)

        } else {
            assertEquals(true, true)
        }
    }

    @Test
    fun `(05) Updates and entity in database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val prsEntity = PersonEntity()

        prsEntity.setFilter(PersonEntity.PRS_ID, 1)
        prsEntity[PersonEntity.PRS_AGE] = 31

        assertEquals(true, entityDB.update(prsEntity))
    }

    @Test
    fun `(06) Selects all entities from the database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val prsEntity = PersonEntity()

        val pSize = entityDB.select(prsEntity).size

        assertEquals(3, pSize)
    }

    @Test
    fun `(07) Selects a single entity from database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val prsEntity = PersonEntity()

        prsEntity.setFilter(PersonEntity.PRS_ID, 1)

        val pSize = entityDB.select(prsEntity).size

        assertEquals(1, pSize)
    }

    @Test
    fun `(08) Performing a inner join on a single entity in database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val carEntity = CarEntity()
        val houEntity = HouseEntity()
        val prsEntity = PersonEntity()

        prsEntity.setRelation(houEntity, RelationType.INNER)
        prsEntity.setRelation(carEntity, RelationType.INNER)

        val pSize = entityDB.join(prsEntity).size

        assertEquals(1, pSize)
    }

    @Test
    fun `(09) Performing a left join on all entities in database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val carEntity = CarEntity()
        val houEntity = HouseEntity()
        val prsEntity = PersonEntity()

        prsEntity.setRelation(houEntity, RelationType.LEFT)
        prsEntity.setRelation(carEntity, RelationType.LEFT)

        val pSize = entityDB.join(prsEntity).size

        assertEquals(3, pSize)
    }

    @Test
    fun `(10) Deletes a single entity from database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val prsEntity = PersonEntity()

        prsEntity.setFilter(PersonEntity.PRS_ID, 1)

        val pSuccess = entityDB.delete(prsEntity)
        prsEntity.clear()

        prsEntity.setFilter(PersonEntity.PRS_ID, 1)

        assertEquals(true, pSuccess && entityDB.select(prsEntity).isEmpty())
    }

    @Test
    fun `(11) Deletes all entities from database`() {
        val entityDB = DatabaseEntity(AppConfig.DATABASE_TYPE, AppConfig.CONNECTION_STRING)
        val carEntity = CarEntity()
        val houEntity = HouseEntity()
        val prsEntity = PersonEntity()

        val cSuccess = entityDB.delete(carEntity)
        carEntity.clear()

        val hSuccess = entityDB.delete(houEntity)
        houEntity.clear()

        val pSuccess = entityDB.delete(prsEntity)
        prsEntity.clear()

        assertEquals(true,
            hSuccess && cSuccess && pSuccess &&
            entityDB.select(carEntity).isEmpty() &&
            entityDB.select(houEntity).isEmpty() &&
            entityDB.select(prsEntity).isEmpty())
    }
}