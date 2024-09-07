package ar.edu.utn.frba.dds.Atencion_Medica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DataSyncRepositorio {

    @Autowired
    @Qualifier("jdbcTemplateMaster")
    private JdbcTemplate jdbcTemplateMaster;

    @Autowired
    @Qualifier("jdbcTemplateSlave")
    private JdbcTemplate jdbcTemplateSlave;

    // Método para obtener datos de la BD del proyecto
    public List<Map<String, Object>> getDataMaster() {

        //TODO: VERIFICAR LA QUERY, DEBERÍA TRAER LOS DATOS NECESARIOS QUE VAMOS A MOSTRAR
        String sql = "SELECT t1.campo1 AS columna1, t2.campo2 AS columna2, t3.campo3 AS columna3 " +
                "FROM tabla1 t1 " +
                "JOIN tabla2 t2 ON t1.id = t2.id " +
                "JOIN tabla3 t3 ON t2.id = t3.id";

        return jdbcTemplateMaster.queryForList(sql);
    }

    // Método para insertar datos en la BD del servicio
    public void insertDataToSlave(List<Map<String, Object>> data) {

        //TODO: VERIFICAR LA QUERY, DEBERÍA METER LOS DATOS TRAIDOS EN UNA SOLA TABLA PARA CONSULTARLA
        String sql = "INSERT INTO subordinated_table (columna1, columna2, columna3) VALUES (?, ?, ?)";
        for (Map<String, Object> row : data) {
            jdbcTemplateSlave.update(sql, row.get("columna1"), row.get("columna2"), row.get("columna3"));
        }
    }

    // Método para limpiar la tabla subordinada antes de sincronizar
    public void clearSlaveTable() {
        //TODO: VERIFICAR LA QUERY, DEBERÍA VACIAR LA TABLA DE LA SLAVE, SE PUEDE HACER UN DROP DEL ESQUEMA SI NO.
        String sql = "DELETE FROM subordinated_table";
        jdbcTemplateSlave.update(sql);
    }
}