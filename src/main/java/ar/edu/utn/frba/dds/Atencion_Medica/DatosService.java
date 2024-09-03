package ar.edu.utn.frba.dds.Atencion_Medica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DatosService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;

    //TODO:PROBAR LA QUERY CUANDO ESTÉ LA PERSISTENCIA
    @Scheduled(cron = "0 0 0 * * ?") //Cron configurado para todos los dias a las 00:00hs

    public void obtenerUsoHeladera() {
        String sql = "SELECT h.localidad, p.nombre, COUNT(u.id) AS cantidad_uses " +
                "FROM UsoDeHeladera u " +
                "JOIN Heladera h ON u.heladera_id = h.id " +
                "JOIN PersonaVulnerable p ON u.persona_vulnerable_id = p.id " +
                "GROUP BY h.localidad, p.nombre " +
                "ORDER BY cantidad_uses DESC";

        List<String> resultados = jdbcTemplate.query(sql, (rs, rowNum) ->
                String.format("Localidad: %s, Persona: %s, Usos: %d",
                        rs.getString("localidad"),
                        rs.getString("nombre"),
                        rs.getInt("cantidad_usos"))
        );
        redisTemplate.opsForValue().set("usoHeladera:" + LocalDateTime.now().toLocalDate(), String.join(",", resultados));
    }
}