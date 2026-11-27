
CREATE TABLE IF NOT EXISTS `prvDbColls` (
  `user` varchar(20) NOT NULL,
  `pwd` varchar(1000) NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE IF NOT EXISTS `prvDbUsers` (
  `user` varchar(20) NOT NULL,
  `pwd` varchar(1000) NOT NULL,
  PRIMARY KEY (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `prvBackup` (
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

CREATE TABLE IF NOT EXISTS `prvZoran` (
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

CREATE TABLE IF NOT EXISTS `prvRezepte` (
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

CREATE TABLE IF NOT EXISTS `prvBackup` (
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

CREATE TABLE IF NOT EXISTS `prvCheck` (
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

CREATE TABLE IF NOT EXISTS `prvXslt` (
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

-- INSERT INTO `prvFormixRezeptblock` (`user`, `pwd`, `role`, `privs`, `privsRoles`, `appPrivs`, `appPrivsRoles`, `privParas`, `privParasRoles`, `description`) VALUES
-- ('nix', 'bmERqCh4GSVR7qvMWTMfew==', 'admin', 'drop', 'all libro', NULL, 'all', NULL, NULL, NULL);


