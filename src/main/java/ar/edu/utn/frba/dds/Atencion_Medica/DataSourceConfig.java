package ar.edu.utn.frba.dds.Atencion_Medica;
import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class DataSourceConfig {
        // DataSource para la BD del proyecto
        @Bean(name = "masterDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.master")
        public DataSource masterDataSource() {
            return DataSourceBuilder.create().build();
        }

        // JdbcTemplate para la BD del proyecto
        @Bean(name = "jdbcTemplateMaster")
        public JdbcTemplate jdbcTemplateMaster(@Qualifier("masterDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
        }


        // DataSource para la BD del servicio
        @Bean(name = "slaveDataSource")
        @ConfigurationProperties(prefix = "spring.datasource.slave")
        public DataSource slaveDataSource() {
            return DataSourceBuilder.create().build();
        }

        // JdbcTemplate para la BD del servicio
        @Bean(name = "jdbcTemplateSlave")
        public JdbcTemplate jdbcTemplateSlave(@Qualifier("slaveDataSource") DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
}
