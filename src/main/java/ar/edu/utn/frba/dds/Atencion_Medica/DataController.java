package ar.edu.utn.frba.dds.Atencion_Medica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/heladera")
public class DataController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DataSyncRepositorio dataSyncRepositorio;

    @GetMapping("/uso")
    public String obtenerUsoHeladera() {
        // Obtener los datos de hoy (Si no cambiar fecha)
        String key = "usoHeladera:" + LocalDate.now();
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .orElse("No data available");
    }


    @PostMapping("/integracion")
    public ResponseEntity<String> procesarDatos(@RequestBody Map<String, Object> payload) {
        try {
            List<Map<String, Object>> personas = (List<Map<String, Object>>) payload.get("personas");
            List<Map<String, Object>> heladeras = (List<Map<String, Object>>) payload.get("heladeras");
            List<Map<String, Object>> ubicaciones = (List<Map<String, Object>>) payload.get("ubicaciones");
            List<Map<String, Object>> usosHeladeras = (List<Map<String, Object>>) payload.get("usosHeladeras");

            // Persistir en base de datos (usando tu repositorio personalizado)
            for (Map<String, Object> persona : personas) {
                if (!dataSyncRepositorio.existePersona(persona)) {
                    dataSyncRepositorio.insertPersonaVulnerable(persona);
                }
            }

            for (Map<String, Object> ubicacion : ubicaciones) {
                if (!dataSyncRepositorio.existeUbicacion(ubicacion)) {
                    dataSyncRepositorio.insertUbicacion(ubicacion);
                }
            }

            for (Map<String, Object> heladera : heladeras) {
                if (!dataSyncRepositorio.existeHeladera(heladera)) {
                    dataSyncRepositorio.insertHeladera(heladera);
                }
            }

            for (Map<String, Object> usoHeladera : usosHeladeras) {
                /*
                if (!dataSyncRepositorio.existeUsoHeladera(usoHeladera)) {
                }
                 */
                dataSyncRepositorio.insertUsoHeladera(usoHeladera);

            }

            return ResponseEntity.ok("Datos procesados y almacenados con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al procesar los datos: " + e.getMessage());
        }
    }


    @PostMapping("/personas")
    public ResponseEntity<String> insertarPersonaVulnerable(@RequestBody List<Map<String, Object>> personas) {
        try {
            System.out.println("Request Body: " + personas);

            for (Map<String, Object> persona : personas) {
                dataSyncRepositorio.insertPersonaVulnerable(persona);
            }
            return ResponseEntity.ok("Personas vulnerables insertadas con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al insertar personas vulnerables: " + e.getMessage());
        }
    }

    @PostMapping("/ubicaciones")
    public ResponseEntity<String> insertarUbicaciones(@RequestBody List<Map<String, Object>> ubicaciones) {
        try {
            for (Map<String, Object> ubicacion : ubicaciones) {
                dataSyncRepositorio.insertUbicacion(ubicacion);
            }
            return ResponseEntity.ok("Ubicaciones insertadas con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al insertar ubicaciones: " + e.getMessage());
        }
    }

    @PostMapping("/heladeras")
    public ResponseEntity<String> insertarHeladeras(@RequestBody List<Map<String, Object>> heladeras) {
        try {
            for (Map<String, Object> heladera : heladeras) {
                dataSyncRepositorio.insertHeladera(heladera);
            }
            return ResponseEntity.ok("Heladeras insertadas con éxito.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al insertar heladeras: " + e.getMessage());
        }
    }
}