package ar.edu.utn.frba.dds.Atencion_Medica;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

        // Configuración de la base de datos Master
        @Bean(name = "masterDataSource")
        @Primary
        public DataSource masterDataSource() {
                HikariDataSource dataSource = new HikariDataSource();
                dataSource.setJdbcUrl("jdbc:mysql://dds-mysql.cfqgkykgwkz2.sa-east-1.rds.amazonaws.com:3306/dds_master?serverTimeZone=America/Argentina/Buenos_Aires");
                dataSource.setUsername("admin");
                dataSource.setPassword("ElPistolero23");
                dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                return dataSource;
        }

        @Bean(name = "jdbcTemplateMaster")
        @Primary
        public JdbcTemplate jdbcTemplateMaster(@Qualifier("masterDataSource") DataSource masterDataSource) {
                return new JdbcTemplate(masterDataSource);
        }

        // Configuración de la base de datos Slave
        @Bean(name = "slaveDataSource")
        public DataSource slaveDataSource() {
                HikariDataSource dataSource = new HikariDataSource();
                dataSource.setJdbcUrl("jdbc:mysql://dds-mysql.cfqgkykgwkz2.sa-east-1.rds.amazonaws.com:3306/dds_slave?serverTimeZone=America/Argentina/Buenos_Aires");
                dataSource.setUsername("admin");
                dataSource.setPassword("ElPistolero23");
                dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                return dataSource;
        }

        @Bean(name = "jdbcTemplateSlave")
        public JdbcTemplate jdbcTemplateSlave(@Qualifier("slaveDataSource") DataSource slaveDataSource) {
                return new JdbcTemplate(slaveDataSource);
        }

        // Configuración de Redis
        @Bean(name= "redisTemplate")
        public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
                return new StringRedisTemplate(redisConnectionFactory);
        }
}
