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
}
