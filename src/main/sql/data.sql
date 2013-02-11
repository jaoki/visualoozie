-- MySQL dump 10.13  Distrib 5.5.14, for Win64 (x86)
--
-- Host: localhost    Database: lpbw
-- ------------------------------------------------------
-- Server version	5.5.14

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `event_plan`
--

LOCK TABLES `event_plan` WRITE;
/*!40000 ALTER TABLE `event_plan` DISABLE KEYS */;
INSERT INTO `event_plan` VALUES (1,'123456','ett','test', FALSE,'2011-09-28 22:09:29')
,(2,'123456','event name2','test', FALSE,'2011-09-28 22:09:29')
,(3,'11111111','event name3','test', FALSE,'2011-09-28 22:09:29')
,(4,'123456','event name4','test', FALSE,'2011-09-28 22:09:29')
,(5,'123456','event name5','test', FALSE,'2011-09-28 22:09:29')
,(6,'123456','event name6','test', FALSE,'2011-09-28 22:09:29')
,(7,'123456','event name7','test', FALSE,'2011-10-09 13:53:50')
,(11,'123456','event name8+','test8++', FALSE,'2011-10-08 02:29:17')
,(12,'123456','aaa','bbb', FALSE,'2011-10-06 21:38:30')
,(14,'123456','event 13','event 13 desc', FALSE,'2011-10-06 21:43:55')
,(15,'123456','event 15','event 15 a', FALSE,'2011-10-06 21:52:24')
,(16,'123456','event 16','event 16', FALSE,'2011-10-06 22:26:22')
,(17,'123456','event 17b','event17a', FALSE,'2011-10-06 23:13:25')
,(18,'123456','event 18aaa','event 18bbb', FALSE,'2011-10-06 23:22:09')
,(19,'123456','event 19+1','event 19 d+1', FALSE,'2011-10-07 00:04:51')
,(20,'123456','event20+','envet20 d++', FALSE,'2011-10-09 17:19:21')
,(21,'123456','bbq for chisato','some bbq party', FALSE,'2011-10-08 19:34:02')
,(22,'123456','new event','new event desc', FALSE,'2011-10-09 17:21:34');
/*!40000 ALTER TABLE `event_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_plan_date`
--

LOCK TABLES `event_plan_date` WRITE;
/*!40000 ALTER TABLE `event_plan_date` DISABLE KEYS */;
INSERT INTO `event_plan_date` VALUES (1,1,'08/15/2011'),(2,1,'2011-12-03'),(3,1,'09/23/2011'),(19,12,'08/15/2011'),(20,12,'2011-12-03'),(21,12,'2011-12-05'),(22,14,'08/15/2011'),(23,14,'2011-12-03'),(24,14,'2011-12-05'),(25,15,'08/15/2011'),(26,15,'2011-12-03'),(27,15,'2011-12-05'),(28,16,'08/15/2011'),(29,16,'2011-12-03'),(30,16,'2011-12-05'),(31,17,'08/15/2011'),(32,17,'2011-12-03'),(33,17,'2011-12-05'),(34,18,'08/15/2011'),(35,18,'2011-12-03'),(36,18,'2011-12-05'),(37,19,'08/01/2011'),(38,19,'10/01/2011'),(56,11,'02/01/2011'),(57,11,'02/02/2011'),(58,11,'02/03/2011'),(61,21,'08/05/2011'),(62,21,'10/01/2011'),(79,7,'01/01/2011'),(80,7,'01/02/2011'),(81,7,'10/04/2011'),(88,20,'10/31/2011'),(89,20,'10/30/2011'),(90,22,'11/09/2011');
/*!40000 ALTER TABLE `event_plan_date` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_plan_place`
--

LOCK TABLES `event_plan_place` WRITE;
/*!40000 ALTER TABLE `event_plan_place` DISABLE KEYS */;
INSERT INTO `event_plan_place` VALUES (1,1,'Dutch Goose'),(2,1,'BJ\'s'),(3,1,'Orench'),(6,12,'Dutch Goose'),(7,12,'B'),(8,12,'O'),(9,14,'Dutch Goose'),(10,14,'BJ\'s'),(11,14,'Orench'),(12,15,'Dutch Goose'),(13,15,'BJ\'s'),(14,15,'Orench'),(15,16,'Dutch Goose'),(16,16,'BJ\'s'),(17,16,'Orench'),(18,17,'Dutch Goose'),(19,17,'BJ\'s'),(20,17,'Orench'),(21,18,'Dutch Goose'),(22,18,'BJ\'s'),(23,18,'Orench'),(24,19,'1'),(25,19,'2'),(26,19,'3'),(27,19,'4'),(28,19,'5'),(29,19,'6'),(30,19,'7'),(31,19,'8'),(32,19,'9'),(33,19,'0'),(34,19,'BJ\'s12'),(35,19,'Orench12'),(53,11,'place c'),(54,11,'place d'),(58,21,'my place'),(59,21,'bar'),(60,21,'gmail'),(74,20,'a+++'),(75,20,'b++++'),(76,20,'dddd+'),(77,20,'aaa'),(78,22,'basho');
/*!40000 ALTER TABLE `event_plan_place` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `event_plan_planner`
--

LOCK TABLES `event_plan_planner` WRITE;
/*!40000 ALTER TABLE `event_plan_planner` DISABLE KEYS */;
INSERT INTO `event_plan_planner` VALUES (1,1,'100002681070637'),(3,12,'100002681070637'),(4,14,'100002681070637'),(5,15,'100002681070637'),(6,16,'100002681070637'),(7,17,'100002681070637'),(8,18,'100002681070637'),(9,19,'213322'),(10,19,'412459'),(23,11,'708757'),(24,11,'3700053'),(26,21,'3220353'),(28,7,'213322'),(35,20,'213322'),(36,20,'412459'),(37,22,'16800526');
/*!40000 ALTER TABLE `event_plan_planner` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2011-10-09 21:57:27
