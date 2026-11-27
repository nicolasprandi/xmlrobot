-- phpMyAdmin SQL Dump
-- version 4.0.8
-- http://www.phpmyadmin.net
--
-- Host: onhp-db1.iron.bsa.oriented.ch
-- Generation Time: Aug 07, 2019 at 09:49 PM
-- Server version: 10.0.38-MariaDB-0+deb8u1
-- PHP Version: 5.6.40-7+0~20190503101815.14+stretch~1.gbp1a44f9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `formixat001`
--

-- --------------------------------------------------------

--
-- Table structure for table `prvDbColls`
--

CREATE TABLE IF NOT EXISTS `prvDbColls` (
  `user` varchar(20) NOT NULL,
  `pwd` varchar(1000) NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `prvDbUsers`
--

CREATE TABLE IF NOT EXISTS `prvDbUsers` (
  `user` varchar(20) NOT NULL,
  `pwd` varchar(1000) NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `prvDbUsers`
--

INSERT INTO `prvDbUsers` (`user`, `pwd`) VALUES
('nix', 'bmERqCh4GSVR7qvMWTMfew==');

-- --------------------------------------------------------

--
-- Table structure for table `prvFormixRezeptblock`
--

CREATE TABLE IF NOT EXISTS `prvFormixRezeptblock` (
  `user` varchar(20) NOT NULL,
  `pwd` varchar(1000) NOT NULL,
  `role` varchar(10) DEFAULT NULL,
  `privs` varchar(255) DEFAULT NULL,
  `privsRoles` varchar(255) DEFAULT NULL,
  `appPrivs` varchar(255) DEFAULT NULL,
  `appPrivsRoles` varchar(255) DEFAULT NULL,
  `privParas` varchar(255) DEFAULT NULL,
  `privParasRoles` varchar(255) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `prvFormixRezeptblock`
--

INSERT INTO `prvFormixRezeptblock` (`user`, `pwd`, `role`, `privs`, `privsRoles`, `appPrivs`, `appPrivsRoles`, `privParas`, `privParasRoles`, `description`) VALUES
('nix', 'bmERqCh4GSVR7qvMWTMfew==', 'admin', 'drop', 'all libro', NULL, 'all', NULL, NULL, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
