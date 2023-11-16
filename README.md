# Gatekeeper

This project (under construction) corresponds to a _framework_ made in Kotlin, aiming to optimize database operations, saving development time.

## Motivation

There is a canonical event in the life of every programmer: the creation of the data access layer, through the implementation of CRUD operations in a project. It's usually the first thing we do when starting development of the _backend_ of a new project; But we don't always like to spend development time on this kind of thing, and only then start developing the logic of the system as a whole.

And the community knows this: there are some _frameworks_ in the development world with the aim of saving this time (e.g., _Entity Framework_ in C# or _Ktorm_ in Kotlin), which are widely used in medium and large projects, and do very well your role. But sometimes, on smaller projects, we just want to create the data layer as simply -- and quickly -- as possible. This is how the idea for **_GateKeeper_** came about: creating a _framework_ capable of delivering only the basics, ready-to-go.

## How it Works

After importing **_GateKeeper_** into your project, you can now using it. The terminology used in this project is based on [**concepts used by IBM**](https://www.ibm.com/docs/en/imdm/12.0?topic=concepts-key-entity-attribute-entity-type) to define data types. In this case, we will use the following definitions:

 - **Entity**: represents a single object in the real world.
 - **Attribute**: is a characteristic that describes something about an entity.

To make it easier, consider an entity as if it were a table, while the attribute is like a column in this table.

### Defining an Entity (Table)

To define a new entity, it is necessary to create a class in the project, extending the _EntityClass_ type. As an example, consider the following ANSI SQL code below, which creates a table:

```SQL
CREATE TABLE PERSON (
    PRS_ID INT,
    PRS_NAME VARCHAR(255),
    PRS_AGE INT
)
```

This table can be mapped within the code as the following entity:

```Kotlin
...
class PersonEntity: EntityClass("PERSON") {
    init {
        setAttributes {
            listOf(
                EntityAttribute("PRS_ID", EntityAttributeType.INTEGER),
                EntityAttribute("PRS_NAME", EntityAttributeType.STRING),
                EntityAttribute("PRS_AGE", EntityAttributeType.INTEGER)
            )
        }
    }
}
...
```

From there, you can now manipulate data using **_GateKeeper_** operations.

In fact, if you don't want to create the table directly in the database (running the previous SQL), after defining the entity above, just run the following code:

```Kotlin
    val entityDB = DatabaseEntity(...)
    val prsEntity = PersonEntity()

    // "true" if ok, "false" otherwise
    val success = entityDB.create(prsEntity)
```

This way, **_GateKeeper_** itself will create the table we initially presented. Cool!

## Manipulating Data

### Values, Filters and Relationships

Before demonstrating how CRUD operations work, it is necessary to explain the three types of **_set()_** that an _EntityClass_ accepts in this project:

- **_setValue()_**: which sets a value for an entity attribute. Used in INSERT and UPDATE operations;
- **_setFilter()_**: which sets a filter for an entity attribute. Used in SELECT, UPDATE and DELETE operations;
- **_setRelation()_**: which sets a relationship between two entities. Used in JOIN operations (INNER and LEFT).

Sound confusing? Maybe with examples, it will be better.

#### INSERT

To insert (INSERT) a new record in the database, you can use the following code:

```Kotlin
    val entityDB = DatabaseEntity(...)
    val prsEntity = PersonEntity()

    /** 
     * INSERT INTO PERSON 
     *  (PRS_ID, PRS_NAME, PRS_AGE)
     * VALUES
     *  (1, "Carlos Carvalho", 31)
    **/
    prsEntity["PRS_ID"] = 1
    prsEntity["PRS_NAME"] = "Carlos Carvalho"
    prsEntity["PRS_AGE"] = 31

    // "true" if ok, "false" otherwise
    val result = entityDB.insert(prsEntity)
```

The steps are very simple -- we instantiate an entity, assign values to its fields, and perform the insertion. Easy!

#### DELETE

Now, to delete (DELETE) an existing record, you can use the following code:

```Kotlin
    val entityDB = DatabaseEntity(...)
    val prsEntity = PersonEntity()

    /** 
     * DELETE FROM PERSON
     * WHERE
     *  PRS_ID = 1
    **/
    prsEntity.setFilter("PRS_ID", 1)

    // "true" if ok, "false" otherwise
    val result = entityDB.delete(prsEntity)
```

Note that, in the case of insertion, we used a _setValue()_ (with bracket notation), as we were inserting values. In the case of deletion, we use a _setFilter()_, as we are deleting a specific record.

#### UPDATE

In the case of updating a record (UPDATE), you may combine _setFilter()_ and _setValue()_, in order to update only specific records. For example:

```Kotlin
    val entityDB = DatabaseEntity(...)
    val prsEntity = PersonEntity()

    /** 
     * UPDATE PERSON SET 
     *  PRS_AGE = 31
     * WHERE
     *  PRS_ID = 1
    **/
    prsEntity["PRS_AGE"] = 31
    prsEntity.setFilter("PRS_ID", 1)

    // "true" if ok, "false" otherwise
    val result = entityDB.update(prsEntity)
```

The code above will only update records compatible with the set filter. Wow!

#### SELECT

Searching for records (SELECT) is a special case. For example:

```Kotlin
    val entityDB = DatabaseEntity(...)
    val prsEntity = PersonEntity()

    /** 
     * SELECT * FROM PERSON
     * WHERE
     *  PRS_ID = 1
    **/
    prsEntity.setFilter("PRS_ID", 1)

    // List<EntityClass> com os resultados
    val results = entityDB.select(prsEntity)
```

In this case, instead of a _Boolean_, the _results_ variable will contain a list of entities that meet the given filter(s). Then, you can access the values in an entity via _getValue()_ (or bracket notation). Magnificent!

#### Other Resources

Some other features are available, such as:

- Use of parameters in an attribute (e.g., defining an attribute of an entity as _read-only_ and _non-nullable_);
- Use of foreign keys and execution of JOIN's (INNER and LEFT);
- Use of transactions (TRANSACTION, COMMIT, and ROLLBACK).

In these cases, in order to not make this text too long, you can find examples of such operations in the file [GateKeeperTests.kt](gatekeeper-test/src/main/kotlin/com/carzuiliam/gatekeeper/test/test/GateKeeperTests.kt). Take a look!

## Additional Information

Ah, this project is forever under construction. Don't worry if things change a lot in it. 😅

## License

The available source codes here are under the Apache License, version 2.0 (see the attached `LICENSE` file for more details). Any questions can be submitted to my email: carloswdecarvalho@outlook.com.