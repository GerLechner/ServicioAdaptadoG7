-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.4.2 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.8.0.6908
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para dds_master
CREATE DATABASE IF NOT EXISTS `dds_master` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `dds_master`;

-- Volcando estructura para tabla dds_master.heladera
CREATE TABLE IF NOT EXISTS `heladera` (
  `id` int NOT NULL AUTO_INCREMENT,
  `ubicacion_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ubicacion_id` (`ubicacion_id`),
  CONSTRAINT `heladera_ibfk_1` FOREIGN KEY (`ubicacion_id`) REFERENCES `ubicacion` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla dds_master.heladera: ~4 rows (aproximadamente)
INSERT INTO `heladera` (`id`, `ubicacion_id`) VALUES
	(1, 1),
	(2, 2),
	(3, 3),
	(4, 4);

-- Volcando estructura para tabla dds_master.personavulnerable
CREATE TABLE IF NOT EXISTS `personavulnerable` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `apellido` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla dds_master.personavulnerable: ~9 rows (aproximadamente)
INSERT INTO `personavulnerable` (`id`, `nombre`, `apellido`) VALUES
	(1, 'Juan', 'Pérez'),
	(2, 'María', 'Gómez'),
	(3, 'Carlos', 'Díaz'),
	(4, 'Ana', 'López'),
	(5, 'Juan', 'Pérez'),
	(6, 'Ana', 'González'),
	(7, 'María', 'López'),
	(8, 'Carlos', 'Martínez'),
	(9, 'Lucía', 'Rodríguez');

-- Volcando estructura para tabla dds_master.ubicacion
CREATE TABLE IF NOT EXISTS `ubicacion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `localidad` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla dds_master.ubicacion: ~4 rows (aproximadamente)
INSERT INTO `ubicacion` (`id`, `localidad`) VALUES
	(1, 'Buenos Aires'),
	(2, 'Córdoba'),
	(3, 'Rosario'),
	(4, 'Mendoza');

-- Volcando estructura para tabla dds_master.usoheladera
CREATE TABLE IF NOT EXISTS `usoheladera` (
  `id` int NOT NULL AUTO_INCREMENT,
  `heladera_id` int DEFAULT NULL,
  `fechaHora` datetime DEFAULT NULL,
  `tarjeta` varchar(255) DEFAULT NULL,
  `personaVulnerable_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `heladera_id` (`heladera_id`),
  KEY `personaVulnerable_id` (`personaVulnerable_id`),
  CONSTRAINT `usoheladera_ibfk_1` FOREIGN KEY (`heladera_id`) REFERENCES `heladera` (`id`),
  CONSTRAINT `usoheladera_ibfk_2` FOREIGN KEY (`personaVulnerable_id`) REFERENCES `personavulnerable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla dds_master.usoheladera: ~14 rows (aproximadamente)
INSERT INTO `usoheladera` (`id`, `heladera_id`, `fechaHora`, `tarjeta`, `personaVulnerable_id`) VALUES
	(1, 1, '2024-09-01 12:30:00', '1234-5678-9101', 1),
	(2, 2, '2024-09-02 14:45:00', '2345-6789-1012', 2),
	(3, 3, '2024-09-03 16:20:00', '3456-7890-1123', 3),
	(4, 4, '2024-09-04 11:10:00', '4567-8901-1234', 4),
	(5, 1, '2024-09-05 13:50:00', '5678-9012-1345', 1),
	(6, 2, '2024-09-06 15:30:00', '6789-0123-1456', 2),
	(7, 1, '2024-09-01 12:00:00', 'Tarjeta1', 1),
	(8, 1, '2024-09-01 13:00:00', 'Tarjeta2', 2),
	(9, 1, '2024-09-02 09:30:00', 'Tarjeta3', 3),
	(10, 2, '2024-09-01 14:00:00', 'Tarjeta4', 4),
	(11, 2, '2024-09-03 11:15:00', 'Tarjeta5', 5),
	(12, 3, '2024-09-01 10:00:00', 'Tarjeta6', 1),
	(13, 4, '2024-09-02 16:00:00', 'Tarjeta7', 2),
	(14, 4, '2024-09-02 17:30:00', 'Tarjeta8', 3);


-- Volcando estructura de base de datos para dds_slave
CREATE DATABASE IF NOT EXISTS `dds_slave` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `dds_slave`;

-- Volcando estructura para tabla dds_slave.uso_heladera_resumen
CREATE TABLE IF NOT EXISTS `uso_heladera_resumen` (
  `localidad` varchar(255) DEFAULT NULL,
  `cantidad_personas` int DEFAULT NULL,
  `nombres_personas` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla dds_slave.uso_heladera_resumen: ~4 rows (aproximadamente)
INSERT INTO `uso_heladera_resumen` (`localidad`, `cantidad_personas`, `nombres_personas`) VALUES
	('Buenos Aires', 3, 'Carlos Díaz, Juan Pérez, María Gómez'),
	('Córdoba', 3, 'Ana López, Juan Pérez, María Gómez'),
	('Mendoza', 3, 'Ana López, Carlos Díaz, María Gómez'),
	('Rosario', 2, 'Carlos Díaz, Juan Pérez');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
