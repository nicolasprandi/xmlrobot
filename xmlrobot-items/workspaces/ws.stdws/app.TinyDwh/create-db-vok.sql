-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved.
-- beziehungstypen
DROP TABLE IF EXISTS vok.tb;
CREATE TABLE vok.tb (
                bez_typ smallint NOT NULL,
                tech_name char(60),
                name char(100),
                doku varchar(2000),
                name_plural char(100),
                text_ab char(200),
                text_ba char(200),
                doku_a char(200),
                doku_b char(200),
                satz_ab char(200),
                satz_ba char(200)
);

-- objekttypen
DROP TABLE IF EXISTS t_o;
CREATE TABLE t_o (
                obj_typ smallint NOT NULL,
                tech_name char(60),
                name char(100),
                doku varchar(2000),
                name_plural char(100),
                kopf_attr_typ smallint
);

-- attributtypen
DROP TABLE IF EXISTS vok.ta;
CREATE TABLE vok.ta (
                attr_typ smallint NOT NULL,
                tech_name char(60),
                format char(100),
                name char(100),
                doku varchar(2000),  
                flag_klasse smallint,   
                final_typ smallint, 
                final_p1 smallint           
);

-- attribute usage    object x attribute
DROP TABLE IF EXISTS vok.attr_usg;
DROP SEQUENCE IF EXISTS vok.usg_seq;
CREATE SEQUENCE vok.usg_seq;
CREATE TABLE vok.attr_usg (
                attr_usg_id INTEGER NOT NULL DEFAULT nextval('vok.usg_seq'),
                attr_typ smallint NOT NULL,
                obj_typ smallint NOT NULL,
                varianz smallint,
                varianz_text char(20),
                ident smallint,
                ident_text char(20),
                ident_mehr smallint,
                ident_club char(20)
);
