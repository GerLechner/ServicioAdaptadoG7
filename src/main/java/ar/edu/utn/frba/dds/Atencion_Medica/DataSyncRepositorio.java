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

    public List<Map<String, Object>> getDataMaster() {
        String sql = "SELECT u.fechaHora AS fecha, pv.nombre AS nombre, pv.apellido AS apellido, ub.localidad AS localidad " +
                "FROM usoheladera u " +
                "JOIN heladera h ON u.heladera_id = h.id " +
                "JOIN ubicacion ub ON h.ubicacion_id = ub.id " +
                "JOIN personavulnerable pv ON u.personaVulnerable_id = pv.id";

        return jdbcTemplateMaster.queryForList(sql);
    }

    public void insertDataToSlave(List<Map<String, Object>> data) {
        String sql = "INSERT INTO uso_heladera_resumen (localidad, persona_vulnerable, cantidad_usos) VALUES (?, ?, ?)";
        for (Map<String, Object> row : data) {
            jdbcTemplateSlave.update(sql, row.get("localidad"), row.get("persona_vulnerable"), row.get("cantidad_usos"));
        }
    }

    public void clearSlaveTable() {
        String sql = "DELETE FROM uso_heladera_resumen";
        jdbcTemplateSlave.update(sql);
    }
}
