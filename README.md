


# Documentación de la API

## Resumen
Esta es una adaptacion del servicio creado por el grupo 12 de DDS, para el correcto funcionamiento con nuestro sistema.

Repositorio Original: https://github.com/Sichermatias/2024-tpa-mi-no-grupo-12-api-rest

## Servicio 3: Atención Médica

La ONG a cargo del sistema tiene un programa que se dedica a brindar atención médica a personas en situación de calle. Diariamente sale un grupo de médicos voluntarios a buscar personas que necesiten atención. Sin embargo, en algunos casos ocurre que les es difícil saber en qué barrio pueden generar el mayor impacto.
Es por eso que se nos solicitó los datos de las personas que tenemos registradas para realizar sus circuitos. Diariamente, se ejecutará este servicio y se espera que el mismo retorne una lista de las localidades con la cantidad de personas que tenemos registradas como en situación de calle que solicitaron al menos una vianda en ese barrio. Además, los médicos nos solicitaron saber también el nombre de aquellas personas.

### Configuracion
- Instalar Redis https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/install-redis-on-windows/ para correcto funcionamiento de la cache 
- Recomendamos utilizar el archivo .sql para la creacion de las bases de datos necesarias.
- Una vez creadas las bases de datos, se deben modificar los datos del archivo "application.properties" y "DataSourceConfig" con la informacion necesaria para conectarse (puertos, usuario, contraseña).
- Como ultimo paso, recomendamos modificar el tiempo de ejecucion de los CronJobs para que se ejecuten las funciones inicialmente (DataService.java, DataSyncService.java), inicialmente pueden ejecutarla cada 5 segundos ("*/5 * * * * ?") para comprobar el funcionamiento.

### Endpoints

El código proporcionado no especifica ningún endpoint de la API. Sin embargo, basándonos en la funcionalidad del servicio, podemos sugerir el siguiente endpoint:

#### GET localhost:8080/heladera/uso

Este endpoint devuelve una lista de localidades con el número de personas en situación de calle que solicitaron al menos una vianda en ese barrio, junto con sus nombres.

##### Respuesta

Un array JSON de objetos, cada uno conteniendo:

- `localidad`: La localidad.
- `personas`: Los nombres de esas personas separados por comas (,).
- `cantidad_personas`: El número de personas en situación de calle que solicitaron al menos una vianda en ese barrio.

Ejemplo:

```json
[
    {
        "localidad": "Buenos Aires",
        "personas": "Gonzalo Carrizo, Lautaro Petronacci",
        "cantidad_personas": 2
    },
    {
        "localidad": "Cordoba"
        "personas": "Sicher Matias, Federico Bietti, Martin Ibarra",
        "cantidad_personas": 3,
    }
]
```

Agradecimientos por las pruebas que nos sirvieron para corregir:
@camilabarrosalbarracin.