package ar.edu.utn.frba.dds.Atencion_Medica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@ComponentScan(basePackages = "ar.edu.utn.frba.dds.Atencion_Medica")
public class AtencionMedicaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AtencionMedicaApplication.class, args);
	}
}