-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.22 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping data for table scar.drivers: ~0 rows (approximately)
/*!40000 ALTER TABLE `drivers` DISABLE KEYS */;
INSERT INTO `drivers` (`driverId`, `loginId`, `ownerId`, `fullName`, `phoneNumber`, `carNumber`, `keyNo`, `drivingPermission`, `imageId`) VALUES
	(1, 2, 1, 'P97+5A+SDdIMV5pQ7zOSSw==\n', '9bPn/SuSI7+rjKBU8ZJ+Wg==\n', '1Uqp0wGjpoXhqIh69GePag==\n', 'a1ewRxLhCfXp849kjn5jrzM0uOhZ/KhRiBqOBleeIPjaqI4eWwRMQl3jhixvK3G1\n', '03/02/2021', 2131230835);
/*!40000 ALTER TABLE `drivers` ENABLE KEYS */;

-- Dumping data for table scar.login: ~2 rows (approximately)
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
INSERT INTO `login` (`loginId`, `email`, `password`, `isOwner`) VALUES
	(1, '7q5VAoyZkHhiszJ2V1GOcQ==\n', '7q5VAoyZkHhiszJ2V1GOcQ==\n', 1),
	(2, '5hgCkeuckDg3qb9kJuisRw==\n', '5hgCkeuckDg3qb9kJuisRw==\n', 0);
/*!40000 ALTER TABLE `login` ENABLE KEYS */;

-- Dumping data for table scar.owners: ~0 rows (approximately)
/*!40000 ALTER TABLE `owners` DISABLE KEYS */;
INSERT INTO `owners` (`ownerId`, `loginId`, `fullName`, `phoneNumber`, `carNumber`, `keyNo`, `imageId`) VALUES
	(1, 1, 'YaVIn18rDRccKwCtrTjvBQ==\n', 'La4v6nLwZZi99DShpbX1HA==\n', '1Uqp0wGjpoXhqIh69GePag==\n', 'a1ewRxLhCfXp849kjn5jrzM0uOhZ/KhRiBqOBleeIPjaqI4eWwRMQl3jhixvK3G1\n', 2131230817);
/*!40000 ALTER TABLE `owners` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
