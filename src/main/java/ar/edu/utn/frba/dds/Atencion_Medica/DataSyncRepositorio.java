package ar.edu.utn.frba.dds.Atencion_Medica;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DataSyncRepositorio {

    private final JdbcTemplate jdbcTemplateMaster;
    private final JdbcTemplate jdbcTemplateSlave;

    public DataSyncRepositorio(
            @Qualifier("jdbcTemplateMaster") JdbcTemplate jdbcTemplateMaster,
            @Qualifier("jdbcTemplateSlave") JdbcTemplate jdbcTemplateSlave) {
        this.jdbcTemplateMaster = jdbcTemplateMaster;
        this.jdbcTemplateSlave = jdbcTemplateSlave;
    }

    // Crea la tabla si no existe
    public void ensureTableExists() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS uso_heladera_resumen (" +
                "localidad VARCHAR(255), " +
                "cantidad_personas INT, " +
                "nombres_personas TEXT)";
        jdbcTemplateSlave.update(createTableSql);
    }

    // Obtiene datos de la base de datos principal
    public List<Map<String, Object>> getDataMaster() {
        String sql = "SELECT " +
                "ub.localidad AS localidad, " +
                "COUNT(DISTINCT pv.id) AS cantidad_personas, " +
                "GROUP_CONCAT(DISTINCT CONCAT(pv.nombre, ' ', pv.apellido) SEPARATOR ', ') AS nombres_personas " +
                "FROM usoheladera u " +
                "JOIN heladera h ON u.heladera_id = h.id " +
                "JOIN ubicacion ub ON h.ubicacion_id = ub.id " +
                "JOIN personavulnerable pv ON u.personaVulnerable_id = pv.id " +
                "GROUP BY ub.localidad " +
                "ORDER BY cantidad_personas DESC";
        return jdbcTemplateMaster.queryForList(sql);
    }

    // Inserta datos en la base de datos secundaria
    public void insertDataToSlave(List<Map<String, Object>> data) {
        ensureTableExists(); // Asegúrate de que la tabla existe antes de insertar
        String sql = "INSERT INTO uso_heladera_resumen (localidad, cantidad_personas, nombres_personas) VALUES (?, ?, ?)";
        for (Map<String, Object> row : data) {
            jdbcTemplateSlave.update(sql, row.get("localidad"), row.get("cantidad_personas"), row.get("nombres_personas"));
        }
    }

    // Borra los datos de la tabla secundaria
    public void clearSlaveTable() {
        String sql = "DROP TABLE IF EXISTS uso_heladera_resumen";
        jdbcTemplateSlave.update(sql);
    }

    // Métodos para insertar datos en las tablas maestras
    public void insertPersonaVulnerable(Map<String, Object> persona) {
        String sql = "INSERT INTO personaVulnerable (id, nombre, apellido) VALUES (?, ?, ?)";
        jdbcTemplateMaster.update(sql, persona.get("id"), persona.get("nombre"), persona.get("apellido"));
    }

    public void insertUbicacion(Map<String, Object> ubicacion) {
        String sql = "INSERT INTO ubicacion (id, localidad) VALUES (?, ?)";
        jdbcTemplateMaster.update(sql, ubicacion.get("ubicacion_id"), ubicacion.get("localidad"));
    }

    public void insertHeladera(Map<String, Object> heladera) {
        String sql = "INSERT INTO heladera (id, ubicacion_id) VALUES (?, ?)";
        jdbcTemplateMaster.update(sql, heladera.get("id"), heladera.get("ubicacion_id"));
    }

    public void insertUsoHeladera(Map<String, Object> uso) {
        String sql = "INSERT INTO usoHeladera (heladera_id, fechaHora, tarjeta, personaVulnerable_id) VALUES (?, ?, ?, ?)";
        jdbcTemplateMaster.update(sql, uso.get("heladera_id"), uso.get("fechaHora"), uso.get("tarjeta_id"), uso.get("personaVulnerable_id"));
    }

    public boolean existePersona(Map<String, Object> persona) {
        String nombre = (String) persona.get("nombre");
        String apellido = (String) persona.get("apellido");

        if (nombre == null || apellido == null) {
            System.err.println("Datos incompletos: nombre=" + nombre + ", apellido=" + apellido);
            return false;
        }

        System.out.println("Verificando persona: " + nombre + " " + apellido);

        String sql = "SELECT COUNT(*) FROM personaVulnerable WHERE nombre = ? AND apellido = ? AND id = ?";
        try {
            Integer count = jdbcTemplateMaster.queryForObject(sql, Integer.class, nombre, apellido, persona.get("id"));
            return count != null && count > 0;
        } catch (Exception e) {
            System.err.println("Error ejecutando consulta SQL: " + e.getMessage());
            throw e; // O maneja el error según tu lógica
        }
    }

    public boolean existeUbicacion(Map<String, Object> ubicacion) {
        String sql = "SELECT COUNT(*) FROM ubicacion WHERE id = ? AND localidad = ?";
        Integer count = jdbcTemplateMaster.queryForObject(sql, Integer.class, ubicacion.get("id"), ubicacion.get("localidad"));
        return count != null && count > 0;
    }

    public boolean existeHeladera(Map<String, Object> heladera) {
        String sql = "SELECT COUNT(*) FROM heladera WHERE id = ? AND ubicacion_id = ?";
        Integer count = jdbcTemplateMaster.queryForObject(sql, Integer.class,
                heladera.get("id"), heladera.get("ubicacion_id"));
        return count != null && count > 0;
    }

    /*
    public boolean existeUsoHeladera(Map<String, Object> usoHeladera) {
        String sql = "SELECT COUNT(*) FROM usoheladera WHERE heladera_id = ? AND persona_id = ?";
        Integer count = jdbcTemplateMaster.queryForObject(sql, Integer.class,
                usoHeladera.get("heladera_id"), usoHeladera.get("persona_id"));
        return count != null && count > 0;
    }
     */
}
