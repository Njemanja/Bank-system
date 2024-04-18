CREATE DATABASE  IF NOT EXISTS `banka_backup` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `banka_backup`;
-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: banka_backup
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.21-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `filijala`
--

DROP TABLE IF EXISTS `filijala`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filijala` (
  `IDFil` int(11) NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `IDMes` int(11) DEFAULT NULL,
  PRIMARY KEY (`IDFil`),
  KEY `FK_IDMes_idx` (`IDMes`),
  CONSTRAINT `FK_IDMes_Fil` FOREIGN KEY (`IDMes`) REFERENCES `mesto` (`IDMes`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filijala`
--

LOCK TABLES `filijala` WRITE;
/*!40000 ALTER TABLE `filijala` DISABLE KEYS */;
/*INSERT INTO `filijala` VALUES (1,'Bulevar','Bulevar kralja Aleksandra 75',1),(2,'Banka22','Resavska 14',5),(3,'Zmaj','Dunavska 12',4),(4,'Bankica','Kamenicka 3',2),(5,'Perica','Njegoseva 30',2);
/*!40000 ALTER TABLE `filijala` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `komitent`
--

DROP TABLE IF EXISTS `komitent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `komitent` (
  `IDKom` int(11) NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `IDMes` int(11) DEFAULT NULL,
  PRIMARY KEY (`IDKom`),
  KEY `FK_IDMes_idx` (`IDMes`),
  CONSTRAINT `FK_IDMes` FOREIGN KEY (`IDMes`) REFERENCES `mesto` (`IDMes`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `komitent`
--

LOCK TABLES `komitent` WRITE;
/*!40000 ALTER TABLE `komitent` DISABLE KEYS */;
/*INSERT INTO `komitent` VALUES (1,'Nevena','Nemanjina 77',5),(2,'Ivan','Vojvode Stepe 122',1),(3,'Nikola','Zmaj Jovina 23',4),(4,'Lazar','Kicevska 3',1);
/*!40000 ALTER TABLE `komitent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `komitent2`
--

/**DROP TABLE IF EXISTS `komitent2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*/*!50503 SET character_set_client = utf8mb4 */;
/*CREATE TABLE `komitent2` (
  `KomitentId` int(11) NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `Adresa` varchar(45) NOT NULL,
  `MestoId` varchar(45) NOT NULL,
  PRIMARY KEY (`KomitentId`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `komitent2`
--



--
-- Table structure for table `mesto`
--

DROP TABLE IF EXISTS `mesto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mesto` (
  `IDMes` int(11) NOT NULL AUTO_INCREMENT,
  `Naziv` varchar(45) NOT NULL,
  `PostanskiBroj` int(11) NOT NULL,
  PRIMARY KEY (`IDMes`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mesto`
--

LOCK TABLES `mesto` WRITE;
/*!40000 ALTER TABLE `mesto` DISABLE KEYS */;
/*INSERT INTO `mesto` VALUES (1,'Beograd',11000),(2,'Kragujevac',34000),(3,'Nis',18000),(4,'Novi Sad',21000),(5,'Pozarevac',12000),(6,'Sabac',15000),(30,'Valjevo',14000);
/*!40000 ALTER TABLE `mesto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prenos`
--

DROP TABLE IF EXISTS `prenos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prenos` (
  `IDTra` int(11) NOT NULL,
  `IDRacNA` int(11) NOT NULL,
  PRIMARY KEY (`IDTra`),
  KEY `FK_IDRacNA_idx` (`IDRacNA`),
  CONSTRAINT `FK_IDRacNA` FOREIGN KEY (`IDRacNA`) REFERENCES `racun` (`IDRac`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_IDTra` FOREIGN KEY (`IDTra`) REFERENCES `transakcija` (`IDTra`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prenos`
--

LOCK TABLES `prenos` WRITE;
/*!40000 ALTER TABLE `prenos` DISABLE KEYS */;
/*!40000 ALTER TABLE `prenos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `racun`
--

DROP TABLE IF EXISTS `racun`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `racun` (
  `IDRac` int(11) NOT NULL AUTO_INCREMENT,
  `BrojTransakcija` int(11) NOT NULL DEFAULT 0,
  `Status` int(1) NOT NULL DEFAULT 1,
  `DozvoljenMinus` double NOT NULL,
  `Stanje` double NOT NULL DEFAULT 0,
  `IDMes` int(11) NOT NULL,
  `IDKom` int(11) NOT NULL,
  `Datum` datetime DEFAULT NULL,
  PRIMARY KEY (`IDRac`),
  KEY `FK_IDKom_idx` (`KomitentId`),
  CONSTRAINT `FK_IDKom_idx` FOREIGN KEY (`IDKom`) REFERENCES `komitent` (`IDKom`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `FK_IDMes_idx` FOREIGN KEY (`IDMes`) REFERENCES `mesto` (`IDMes`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `racun`
--

LOCK TABLES `racun` WRITE;
/*!40000 ALTER TABLE `racun` DISABLE KEYS */;
/*!40000 ALTER TABLE `racun` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transakcija`
--

DROP TABLE IF EXISTS `transakcija`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transakcija` (
  `IDTra` int(11) NOT NULL AUTO_INCREMENT,
  `Datum_Vreme` datetime NOT NULL,
  `Iznos` double NOT NULL,
  `RedniBroj` int(11) NOT NULL,
  `Svrha` char(1) DEFAULT NULL,
  `IDRac` int(11) NOT NULL,
  PRIMARY KEY (`IDTra`),
  KEY `FK_IDRac_idx` (`IDRac`),
  CONSTRAINT `FK_IDRac` FOREIGN KEY (`IDRac`) REFERENCES `racun` (`IDRac`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transakcija`
--

LOCK TABLES `transakcija` WRITE;
/*!40000 ALTER TABLE `transakcija` DISABLE KEYS */;
/*!40000 ALTER TABLE `transakcija` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uplata_isplata`
--

DROP TABLE IF EXISTS `uplata_isplata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uplata_isplata` (
  `IDTra` int(11) NOT NULL,
  `IDFil` int(11) NOT NULL,
  `Tip` char(1) NOT NULL,
  PRIMARY KEY (`IDTra`),
  CONSTRAINT `FK_IDTra2` FOREIGN KEY (`IDTra`) REFERENCES `transakcija` (`IDTra`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uplata_isplata`
--

LOCK TABLES `uplata_isplata` WRITE;
/*!40000 ALTER TABLE `uplata_isplata` DISABLE KEYS */;
/*!40000 ALTER TABLE `uplata_isplata` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-02-03  5:50:34
