package ar.edu.utn.frba.dds.Atencion_Medica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/heladera")
public class DatosController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/uso")
    public String obtenerUsoHeladera() {
        // Obtener los datos de hoy (Si no cambiar fecha)
        String key = "usoHeladera:" + LocalDate.now();
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .orElse("No data available");
    }
}

