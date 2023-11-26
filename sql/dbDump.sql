-- MySQL dump 10.13  Distrib 8.1.0, for Linux (x86_64)
--
-- Host: localhost    Database: j2ee
-- ------------------------------------------------------
-- Server version       8.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `post_comment_fk` (`post_id`),
  KEY `user_comment_fk` (`user_id`),
  CONSTRAINT `post_comment_fk` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `user_comment_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'Day la noi dung',1,1),(2,'Day la binh luan t2',1,2),(3,'Day la binh luan t3',1,3),(4,'Day la binh luan t4',1,2),(5,'chao nha',1,1),(6,'chao nha',1,1),(7,'aaaa',1,1),(8,'ngu',1,1),(9,'a',5,1),(10,'abc',5,1),(11,'a',5,1),(12,'cd main',4,1),(13,'cd',4,1),(14,'',3,1),(15,'aaaaaa',3,1),(16,'binh dinh',4,1),(17,'a',4,1),(18,'a',4,1),(19,'a',4,1),(20,'biinh',4,1),(21,'hai',4,1),(22,'binhluan',6,1),(23,'binhluan',6,1),(24,'a',6,1),(25,'public',7,1),(26,'public',7,1),(27,'public',7,1),(28,'public',7,1),(29,'public',7,1),(30,'public',7,1),(31,'public',7,1),(32,'public',7,1),(33,'public',7,1),(34,'public',7,1),(35,'public',7,1),(36,'public',7,1),(37,'public',7,1),(38,'public',7,1),(39,'public',7,1),(40,'public',7,1),(41,'public',7,1),(42,'public',7,1),(43,'iphone',2,1),(44,'binh dinh',2,1),(45,'hai ne',2,1),(46,'anh nhon',3,1),(47,'anh nhon',3,1),(48,'vietnam',3,1),(49,'comment',3,1);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emote`
--

DROP TABLE IF EXISTS `emote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emote` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `post_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `post_like_fk` (`post_id`),
  KEY `user_like_fk` (`user_id`),
  CONSTRAINT `post_like_fk` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),
  CONSTRAINT `user_like_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emote`
--

LOCK TABLES `emote` WRITE;
/*!40000 ALTER TABLE `emote` DISABLE KEYS */;
/*!40000 ALTER TABLE `emote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `friend`
--

DROP TABLE IF EXISTS `friend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friend` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `status` tinyint DEFAULT NULL,
  `user_from_id` bigint NOT NULL,
  `user_to_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_from_fk` (`user_from_id`),
  KEY `user_to_fk` (`user_to_id`),
  CONSTRAINT `user_from_fk` FOREIGN KEY (`user_from_id`) REFERENCES `user` (`id`),
  CONSTRAINT `user_to_fk` FOREIGN KEY (`user_to_id`) REFERENCES `user` (`id`),
  CONSTRAINT `friend_chk_1` CHECK ((`status` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friend`
--

LOCK TABLES `friend` WRITE;
/*!40000 ALTER TABLE `friend` DISABLE KEYS */;
/*!40000 ALTER TABLE `friend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `message` varchar(255) DEFAULT NULL,
  `time_send` datetime(6) DEFAULT NULL,
  `room_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKl1kg5a2471cv6pkew0gdgjrmo` (`room_id`),
  KEY `FKb3y6etti1cfougkdr0qiiemgv` (`user_id`),
  CONSTRAINT `FKb3y6etti1cfougkdr0qiiemgv` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKl1kg5a2471cv6pkew0gdgjrmo` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `visible` tinyint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_post_fk` (`user_id`),
  CONSTRAINT `user_post_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `post_chk_1` CHECK ((`visible` between 0 and 2))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'Vua pha ki luat','2023-11-04 13:51:00.868000','/uploads/tienhaile488@gmail.com/kyluat85.png','2023-11-04 13:51:00.868000',0,1),(2,'chao tat ca moi nguoi nha','2023-11-04 13:59:58.640000','/uploads/tienhaile488@gmail.com/Screenshot 2022-10-15 142342.png','2023-11-04 13:59:58.640000',0,1),(3,'chao tat ca moi nguoi nha','2023-11-04 13:59:58.640000','/uploads/tienhaile488@gmail.com/Screenshot 2022-09-26 215825.png','2023-11-04 13:59:58.640000',0,1),(4,'tai sao khong load len ','2023-11-04 13:59:58.640000','/uploads/tienhaile488@gmail.com/Screenshot 2022-11-12 132553.png','2023-11-04 13:59:58.640000',0,1),(5,'ke dung dau','2023-11-04 13:59:58.640000','/uploads/tienhaile488@gmail.com/Screenshot 2022-09-22 083531.png','2023-11-04 13:59:58.640000',0,1),(6,'kiêm tra friend\r\n','2023-11-04 20:41:40.224000',NULL,'2023-11-04 20:41:40.224000',1,1),(7,'Kiem tra public','2023-11-04 20:41:40.224000',NULL,'2023-11-04 20:41:40.224000',2,1);
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_image`
--

DROP TABLE IF EXISTS `post_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post_image` (
  `id` int NOT NULL AUTO_INCREMENT,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_image`
--

LOCK TABLES `post_image` WRITE;
/*!40000 ALTER TABLE `post_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `post_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `last_updated` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `room_type` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `room_chk_1` CHECK ((`room_type` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_messages`
--

DROP TABLE IF EXISTS `room_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_messages` (
  `room_id` bigint NOT NULL,
  `messages_id` bigint NOT NULL,
  UNIQUE KEY `UK_1m0nkocht3lmljiqlmaf6vluv` (`messages_id`),
  KEY `FKh61esxflkh1ocufq5yhxqwjpa` (`room_id`),
  CONSTRAINT `FK3ampv1lcdbap8nln00joji1xo` FOREIGN KEY (`messages_id`) REFERENCES `message` (`id`),
  CONSTRAINT `FKh61esxflkh1ocufq5yhxqwjpa` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_messages`
--

LOCK TABLES `room_messages` WRITE;
/*!40000 ALTER TABLE `room_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `room_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room_participants`
--

DROP TABLE IF EXISTS `room_participants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_participants` (
  `room_id` bigint NOT NULL,
  `participants_id` bigint NOT NULL,
  KEY `FKqa34b914mobvghtpaco0vm55d` (`participants_id`),
  KEY `FKd93x26glimf3l3f05ukaoi6ry` (`room_id`),
  CONSTRAINT `FKd93x26glimf3l3f05ukaoi6ry` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
  CONSTRAINT `FKqa34b914mobvghtpaco0vm55d` FOREIGN KEY (`participants_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_participants`
--

LOCK TABLES `room_participants` WRITE;
/*!40000 ALTER TABLE `room_participants` DISABLE KEYS */;
/*!40000 ALTER TABLE `room_participants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `background` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'/uploads/tienhaile488@gmail.com/Screenshot (9).png','https://c4.wallpaperflare.com/wallpaper/321/512/923/tom-and-jerry-heroes-cartoons-desktop-hd-wallpaper-for-mobile-phones-tablet-and-pc-1920%C3%971200-wallpaper-thumb.jpg','2023-11-04 13:51:00.868000','tienhaile488@gmail.com','Le','Hai','$2a$10$p6efORwXPlEqAQnuHYMT8eDS4jig7nR5NqApD.5H5.j2wT0RELCTa','0987982144','User','2023-11-04 13:51:00.868000'),(2,'https://cdn.alongwalk.info/vn/wp-content/uploads/2022/10/14054104/image-100-y-tuong-avatar-cute-doc-dao-an-tuong-nhat-cho-ban-166567566414594.jpg','https://c4.wallpaperflare.com/wallpaper/321/512/923/tom-and-jerry-heroes-cartoons-desktop-hd-wallpaper-for-mobile-phones-tablet-and-pc-1920%C3%971200-wallpaper-thumb.jpg','2023-11-04 22:20:19.985000','binhdinh@gmail.com','Binh','Dinh','$2a$10$xQovOOLwjhJZLMcEShAfreALHj13Iwq.dNZZvfD.zZ6DqFX4kbydy','0999999999','User','2023-11-04 22:20:19.985000'),(3,'https://cdn.alongwalk.info/vn/wp-content/uploads/2022/10/14054104/image-100-y-tuong-avatar-cute-doc-dao-an-tuong-nhat-cho-ban-166567566414594.jpg','https://c4.wallpaperflare.com/wallpaper/321/512/923/tom-and-jerry-heroes-cartoons-desktop-hd-wallpaper-for-mobile-phones-tablet-and-pc-1920%C3%971200-wallpaper-thumb.jpg','2023-11-04 22:20:19.985000','minhlam@gmail.com','minh','lam','$2a$10$iSzqIqMVDp2CQnxYMhTh/eZ3lIYS3H1ZJwRbbVlRYc0dy4ZqikGaq','0987654321','User','2023-11-04 22:20:19.985000'),(4,'https://cdn.alongwalk.info/vn/wp-content/uploads/2022/10/14054104/image-100-y-tuong-avatar-cute-doc-dao-an-tuong-nhat-cho-ban-166567566414594.jpg','https://c4.wallpaperflare.com/wallpaper/321/512/923/tom-and-jerry-heroes-cartoons-desktop-hd-wallpaper-for-mobile-phones-tablet-and-pc-1920%C3%971200-wallpaper-thumb.jpg','2023-11-04 22:20:19.985000','chilong@gmail.com','chi','long','$2a$10$a5tPRY05cxZFcs7yAe.9AujTmZz1gkApaYw0cYG1mrfhRc8vtWceS','0999999999','User','2023-11-04 22:20:19.985000'),(5,'https://cdn.alongwalk.info/vn/wp-content/uploads/2022/10/14054104/image-100-y-tuong-avatar-cute-doc-dao-an-tuong-nhat-cho-ban-166567566414594.jpg','https://c4.wallpaperflare.com/wallpaper/321/512/923/tom-and-jerry-heroes-cartoons-desktop-hd-wallpaper-for-mobile-phones-tablet-and-pc-1920%C3%971200-wallpaper-thumb.jpg','2023-11-04 22:20:19.985000','annhon@gmail.com','an','nhon','$2a$10$u9Z/0rKaXOGCvWJkQhJBl.8mSTNw/L/i6RSkKvxWBfTUqm.cCSPYy','0987654321','User','2023-11-04 22:20:19.985000'),(6,'https://cdn.alongwalk.info/vn/wp-content/uploads/2022/10/14054104/image-100-y-tuong-avatar-cute-doc-dao-an-tuong-nhat-cho-ban-166567566414594.jpg','https://c4.wallpaperflare.com/wallpaper/321/512/923/tom-and-jerry-heroes-cartoons-desktop-hd-wallpaper-for-mobile-phones-tablet-and-pc-1920%C3%971200-wallpaper-thumb.jpg','2023-11-04 22:20:19.985000','tienhai9a2@gmail.com','tien','hai488','$2a$10$Ktws8u9tpsWI1ORfAffM3.kbt1y9h04TQa7xgzAqetde9IXZ5qT0K','0987654321','User','2023-11-04 22:20:19.985000');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_message_last_seen`
--

DROP TABLE IF EXISTS `user_message_last_seen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_message_last_seen` (
  `last_seen` datetime(6) DEFAULT NULL,
  `room_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`room_id`,`user_id`),
  KEY `FK4dh7prjakl51g1wncx2p7ei0g` (`user_id`),
  CONSTRAINT `FK4dh7prjakl51g1wncx2p7ei0g` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKo7aixakwop6fqiiphd8tfu2y4` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_message_last_seen`
--

LOCK TABLES `user_message_last_seen` WRITE;
/*!40000 ALTER TABLE `user_message_last_seen` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_message_last_seen` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-25  7:37:00