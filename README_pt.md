# Gatekeeper

Este projeto (em construção) corresponde a um _framework_ feito em Kotlin, visando otimizar operações com banco de dados, economizando tempo de desenvolvimento.

## Motivação

Há um evento canônico na vida de todo programador: a criação da camada de acesso a dados, através da implementação de operações CRUD em um projeto. Geralmente é a primeira coisa que fazemos ao iniciar o desenvolvimento do _backend_ de um novo projeto; Mas nem sempre gostamos de gastar tempo de desenvolvimento nesse tipo de coisa, e só então começar a desenvolver a lógica do sistema como um todo. 

E a comunidade sabe disso: há no mundo de desenvolvimento alguns _frameworks_ com o objetivo de economizar esse tempo (e.g., _Entity Framework_ em C# ou _Ktorm_ em Kotlin), que são bastante utilizados em projetos de médio e grande porte, e fazem muito bem o seu papel. Mas às vezes, em projetos menores, só queremos criar a camada de dados da maneira mais simples -- e rápida -- possível. Foi assim que surgiu a ideia do **_GateKeeper_**: criar um _framework_ capaz de entregar somente o básico, já pronto.

## Como Funciona

Após importar o **_GateKeeper_** no seu projeto, você já pode começar a utilizá-lo. A terminologia utilizada neste projeto baseia-se nos [**conceitos utilizados pela IBM**](https://www.ibm.com/docs/en/imdm/12.0?topic=concepts-key-entity-attribute-entity-type) para definição de tipos de dados. Neste caso, utilizaremos as seguintes definições:

 - **Entidade**: é a representação de um único objeto do mundo real.
 - **Atrbuto**: é uma característica que descreve algo de uma entidade.

Pra facilitar, entenda uma entidade como se fosse uma tabela, enquanto o atributo é como uma coluna desta tabela.

### Definindo uma Entidade (Tabela)

Para definir uma nova entidade, é necessário criar uma classe no projeto, extendendo o tipo _EntityClass_. Como exemplo, considere o seguinte código em SQL ANSI abaixo, que cria uma tabela:

```SQL
CREATE TABLE PERSON (
    PRS_ID INT,
    PRS_NAME VARCHAR(255),
    PRS_AGE INT
)
```

Esta tabela pode ser mapeada dentro do código como a entidade a seguir:

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

A partir daí, você já pode realizar manipular dados usando as operações do **_GateKeeper_**. 

Na verdade, se você não quiser criar a tabela direto no banco (rodando o SQL anterior), após definir a entidade acima, basta executar o seguinte código:

```Kotlin
    val entityDB = DatabaseEntity(...)
    val prsEntity = PersonEntity()

    // "true" se ok, "false" caso contrário
    val success = entityDB.create(prsEntity)
```

Dessa forma, o próprio **_GateKeeper_** irá criar a tabela que apresentamos inicialmente. Legal!

## Manipulando Dados

### Valores, Filtros e Relações

Antes de demonstrar como as operações CRUD funcionam, é necessário explicar os três tipos de **_set()_** que uma _EntityClass_ aceita nesse projeto:

- **_setValue()_**: que seta um valor para um atributo da entidade. Usado em operações do tipo INSERT e UPDATE; 
- **_setFilter()_**: que seta um filtro para um atributo da entidade. Usado em operações do tipo SELECT, UPDATE e DELETE; 
- **_setRelation()_**: que seta uma relação entre duas entidades. Usado em operações do tipo JOIN (INNER e LEFT).

Parece confuso? Talvez com exemplos fique melhor.

#### INSERT

Para inserir (INSERT) um novo registro no banco, você pode usar o seguinte código:

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

    // "true" se ok, "false" caso contrário
    val result = entityDB.insert(prsEntity)
```

Os passos são bem simples -- instanciamos uma entidade, atribuimos valores a seus campos, e executamos a inserção. Fácil! 

#### DELETE

Já para excluir (DELETE) um registro existente, você pode usar o código a seguir: 

```Kotlin
    val entityDB = DatabaseEntity(...)
    val prsEntity = PersonEntity()

    /** 
     * DELETE FROM PERSON
     * WHERE
     *  PRS_ID = 1
    **/
    prsEntity.setFilter("PRS_ID", 1)

    // "true" se ok, "false" caso contrário
    val result = entityDB.delete(prsEntity)
```

Perceba que, no caso da inserção, usamos um _setValue()_ (com a notação em colchetes), pois estávamos inserindo valores. Já no caso da exclusão, usamos um _setFilter()_, pois estamos realizando a exclusão de um registro específico.

#### UPDATE

No caso da atualização de um registro (UPDATE), você pode combinar _setFilter()_ e _setValue()_, de modo a atualizar apenas registros específicos. Por exemplo:

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

    // "true" se ok, "false" caso contrário
    val result = entityDB.update(prsEntity)
```

O código acima irá atualizar apenas os registros compatíveis com o filtro setado. Uau!

#### SELECT

A busca de registros (SELECT) é um caso especial. Por exemplo:

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

Neste caso, em vez de um _Boolean_, a variável _results_ irá conter uma lista de entidades que atendem ao filtro. Daí, você pode acessar os valores em uma entidade via _getValue()_ (ou pela notação com colchetes).

#### Outros Recursos

Alguns outros recursos estão disponíveis como, por exemplo:

- Utilização de parâmetros em um atributo (e.g., definir um atributo de uma entidade como _somente leitura_ e _não-nulo_);
- Utilização de chaves estrangeiras e execução de JOIN's (INNER e LEFT);
- Utilização de transações (TRANSACTION, COMMIT e ROLLBACK).

Nestes casos, a fim de não tornar este texto extenso demais, você pode encontrar exemplos de tais operações no arquivo [GateKeeperTests.kt](gatekeeper-test/src/main/kotlin/com/carzuiliam/gatekeeper/test/test/GateKeeperTests.kt). Dê uma olhada!

## Informações Adicionais

Ah, este projeto encontra-se eternamente em construção. Não se preocupe se as coisas mudarem muito nele. 😅

## Licença de Uso

Os códigos disponibilizados estão sob a licença Apache, versão 2.0 (veja o arquivo `LICENSE` em anexo para mais detalhes). Dúvidas sobre este projeto podem ser enviadas para o meu e-mail: carloswilldecarvalho@outlook.com.