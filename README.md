# Documentación de la API
## Servicio 3: Atención Médica

La ONG a cargo del sistema tiene un programa que se dedica a brindar atención médica a personas en situación de calle. Diariamente sale un grupo de médicos voluntarios a buscar personas que necesiten atención. Sin embargo, en algunos casos ocurre que les es difícil saber en qué barrio pueden generar el mayor impacto.
Es por eso que se nos solicitó los datos de las personas que tenemos registradas para realizar sus circuitos. Diariamente, se ejecutará este servicio y se espera que el mismo retorne una lista de las localidades con la cantidad de personas que tenemos registradas como en situación de calle que solicitaron al menos una vianda en ese barrio. Además, los médicos nos solicitaron saber también el nombre de aquellas personas.

### Endpoints

El código proporcionado no especifica ningún endpoint de la API. Sin embargo, basándonos en la funcionalidad del servicio, podemos sugerir el siguiente endpoint:

#### GET localhost:8080/heladera/uso

Este endpoint devuelve una lista de localidades con el número de personas en situación de calle que solicitaron al menos una vianda en ese barrio, junto con sus nombres.

##### Respuesta

Un array JSON de objetos, cada uno conteniendo:

- `localidad`: La localidad.
- `cantidad_personas`: El número de personas en situación de calle que solicitaron al menos una vianda en ese barrio.
- `nombres_personas`: Los nombres de esas personas separados por comas (,).

Ejemplo:

```json
[
    {
        "localidad": "Buenos Aires",
        "cantidad_personas": 2,
        "nombres_personas": "Gonzalo Carrizo, Lautaro Petronacci"
    },
    {
        "localidad": "Cordoba",
        "cantidad_personas": 3,
        "nombres_personas": "Sicher Matias, Federico Bietti, Martin Ibarra"
    }
]
```