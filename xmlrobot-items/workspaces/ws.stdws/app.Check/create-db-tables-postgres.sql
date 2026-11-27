CREATE SCHEMA ##SCHEMA##
  AUTHORIZATION postgres;

DROP TABLE IF EXISTS ##SCHEMA##."prvDbUsers" ;
DROP TABLE IF EXISTS ##SCHEMA##."prvDbColls" ;

CREATE TABLE  ##SCHEMA##."prvDbUsers" (  "user" VARCHAR(20) NOT NULL,  "pwd" varchar(1000) NOT NULL, "size" bigint ,  CONSTRAINT pk_dbuser PRIMARY KEY ("user") ) ;
CREATE TABLE  ##SCHEMA##."prvDbColls" (  "user" VARCHAR(20) NOT NULL,  "pwd" varchar(1000) NOT NULL, "size" bigint , CONSTRAINT pk_dbcoll PRIMARY KEY ("user") ) ;