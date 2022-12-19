-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: ivr
-- ------------------------------------------------------
-- Server version	5.5.29

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
-- Dumping data for table `bean`
--

LOCK TABLES `bean` WRITE;
/*!40000 ALTER TABLE `bean` DISABLE KEYS */;
INSERT INTO `bean` VALUES ('parentCallOrigTask','','virtual'),('mcf','org.asteriskjava.manager.ManagerConnectionFactory',''),('ws50010','com.inov8.ivr.task.call.CallOriginatorTask',''),('parentLangSelTask','','virtual'),('ws500101','com.inov8.ivr.task.input.MultiCharInputTask','default');
/*!40000 ALTER TABLE `bean` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `bean_property`
--

LOCK TABLES `bean_property` WRITE;
/*!40000 ALTER TABLE `bean_property` DISABLE KEYS */;
INSERT INTO `bean_property` VALUES ('mcf',1,'java.lang.String','ManagerConnectionFactory','asteriskserverip',1),('mcf',1,'int','ManagerConnectionFactory','5038',2),('mcf',1,'java.lang.String','ManagerConnectionFactory','manager',3),('mcf',1,'java.lang.String','ManagerConnectionFactory','1234',4),('parentCallOrigTask',1,'org.asteriskjava.manager.ManagerConnectionFactory','setManagerConnectionFactory','mcf',1),('parentCallOrigTask',2,'String','setChannel','Local/@phones',1),('parentCallOrigTask',3,'String','setCallerId','0428354900 <0428354900>',1),('parentCallOrigTask',4,'long','setTimeout','30000',1),('parentCallOrigTask',5,'boolean','setIsAsync','false',1),('parentCallOrigTask',6,'String','setAgiUrl','agi://172.29.12.16/javaivr.agi',1),('ws50010',1,'String','setChannelMap','nextTaskId',1),('ws50010',1,'String','setChannelMap','ws500101',2),('ws50010',2,'String','virtualInheritProperty','parentCallOrigTask',1),('parentLangSelTask',1,'int','setTotalAttempts','3',1),('parentLangSelTask',2,'int','setPromptSoundFiles','0',1),('parentLangSelTask',2,'String','setPromptSoundFiles','*common/branchless_banking/lang_selection',2),('parentLangSelTask',3,'String','setAcceptablePattern','12',1),('ws500101',2,'char','setExitOnFirst','1',1),('ws500101',2,'String','setExitOnFirst','ws5001014',2),('ws500101',3,'char','setExitOnFirst','2',1),('ws500101',3,'String','setExitOnFirst','ws5001014',2),('ws500101',4,'String','virtualInheritProperty','parentLangSelTask',1),('parentLangSelTask',4,'String','setDefaultNextTask','24',1);
/*!40000 ALTER TABLE `bean_property` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-09-19 16:23:26
