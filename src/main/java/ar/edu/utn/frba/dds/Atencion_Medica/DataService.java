package ar.edu.utn.frba.dds.Atencion_Medica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Duration;
import java.util.List;

import java.time.LocalDate;
import java.util.Optional;



@Service
public class DataService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private int reintentosFallidos = 0; // Contador de reintentos

    @Scheduled(cron = "50 6 0 * * ?") // Cron para ejecutar a las 00:00 todos los días
    public void obtenerUsoHeladera() {
        ejecutarConsultaUsoHeladera();
    }

    private void ejecutarConsultaUsoHeladera() {
        String sql = "SELECT ub.localidad, pv.nombre, COUNT(uh.id) AS cantidad_usos " +
                "FROM usoheladera uh " +
                "JOIN heladera h ON uh.heladera_id = h.id " +
                "JOIN ubicacion ub ON h.ubicacion_id = ub.id " +
                "JOIN personavulnerable pv ON uh.personaVulnerable_id = pv.id " +
                "GROUP BY ub.localidad, pv.nombre " +
                "ORDER BY cantidad_usos DESC";
        try {
            // Ejecuto la consulta
            List<String> resultados = jdbcTemplate.query(sql, (rs, rowNum) ->
                    String.format("Localidad: %s, Persona: %s, Usos: %d",
                            rs.getString("localidad"),
                            rs.getString("nombre"),
                            rs.getInt("cantidad_usos"))
            );

            // Si hay resultados, los guardamos en Redis con expiración de 26 horas
            if (!resultados.isEmpty()) {
                redisTemplate.opsForValue().set("usoHeladera:" + LocalDate.now(), String.join(",", resultados), Duration.ofHours(26));
                reintentosFallidos = 0;  // Reseteo el contador de reintentos si es exitoso
            } else {
                // Si no hay resultados, almacenar los datos del día anterior
                System.out.println("No se encontraron datos, intentando usar datos del día anterior.");
                usarDatosDeAyer();
            }
        } catch (Exception e) {
            // Si ocurre un error, intento nuevamente o uso los datos de ayer tras 3 fallos
            System.out.println("Error al obtener los datos: " + e.getMessage());
            manejarErrorConsulta();
        }
    }

    private void manejarErrorConsulta() {
        if (reintentosFallidos < 2) {
            reintentosFallidos++;
            System.out.println("Reintento fallido #" + reintentosFallidos + ". Volviendo a intentar en 5 minutos.");
        } else {
            System.out.println("Fallo tras 3 intentos. Usando los datos del día anterior.");
            usarDatosDeAyer();
            reintentosFallidos = 0;  // Resetear el contador después de fallar 3 veces
        }
    }

    private void usarDatosDeAyer() {
        String keyAyer = "usoHeladera:" + LocalDate.now().minusDays(1);
        Optional<String> datosAyer = Optional.ofNullable(redisTemplate.opsForValue().get(keyAyer));

        if (datosAyer.isPresent()) {
            // Guardar los datos de ayer con la fecha de hoy
            redisTemplate.opsForValue().set("usoHeladera:" + LocalDate.now(), datosAyer.get(), Duration.ofHours(26));
            System.out.println("Datos del día anterior almacenados para hoy.");
        } else {
            System.out.println("No se encontraron datos del día anterior.");
        }
    }

    @Scheduled(cron = "0 */5 * * * ?")  // Reintentar cada 5 minutos si no hay datos
    public void reintentarObtenerUsoHeladera() {
        String keyHoy = "usoHeladera:" + LocalDate.now();
        String cachedData = redisTemplate.opsForValue().get(keyHoy);

        // Si no hay datos almacenados hoy, intento obtener los datos nuevamente
        if (cachedData == null || cachedData.isEmpty()) {
            System.out.println("No hay datos para hoy. Reintentando obtener datos.");
            obtenerUsoHeladera();
        }
    }
}