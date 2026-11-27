DROP TABLE  `prvDbUsers` ;
DROP TABLE  `prvDbColls` ;

CREATE TABLE  `prvDbUsers` (  `user` varchar(20) NOT NULL,  `pwd` varchar(1000) NOT NULL, `size` bigint , PRIMARY KEY (`user`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;
CREATE TABLE  `prvDbColls` (  `user` varchar(20) NOT NULL,  `pwd` varchar(1000) NOT NULL, `size` bigint ,  PRIMARY KEY (`user`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;