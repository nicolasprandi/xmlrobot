-- Copyright © 2015-2018 Nicolas Prandstaetter. All rights reserved.
-- drop views
DROP VIEW IF EXISTS import.kopf;

-- box
DROP TABLE IF EXISTS import.box;
DROP SEQUENCE IF EXISTS import.box_seq;
CREATE SEQUENCE import.box_seq;
CREATE TABLE import.box (
                box_id INTEGER NOT NULL DEFAULT nextval('import.box_seq'),
                zeit_import TIMESTAMP,
                zeit_qk TIMESTAMP,
                zeit_export TIMESTAMP,
                qualitaet smallint,
                CONSTRAINT box_pk PRIMARY KEY (box_id)
);

-- attr
DROP TABLE IF EXISTS import.attr CASCADE;
DROP SEQUENCE IF EXISTS import.attr_seq;
CREATE SEQUENCE import.attr_seq;
CREATE TABLE import.attr (
                attr_id INTEGER NOT NULL DEFAULT nextval('import.attr_seq'),
                obj_id INTEGER NOT NULL,
                attr_typ smallint NOT NULL,
                box_id INTEGER NOT NULL,     
                top_aktuell smallint DEFAULT 0,                           
                CONSTRAINT attr_pk PRIMARY KEY (attr_id)
);


-- wert
DROP TABLE IF EXISTS import.wert CASCADE;
DROP SEQUENCE IF EXISTS import.wert_seq;
CREATE SEQUENCE import.wert_seq;
CREATE TABLE import.wert (
                wert_id INTEGER NOT NULL DEFAULT nextval('import.wert_seq'),
                attr_id INTEGER NOT NULL,
                syntax_art smallint,
                inhalt CHAR(60) NOT NULL,
                inhalt2 CHAR(60),
                von TIMESTAMP,
                bis TIMESTAMP,
                wann TIMESTAMP DEFAULT now() NOT NULL,
                ist_top smallint DEFAULT 0 NOT NULL,
                hat_mehr smallint DEFAULT 0 NOT NULL,
                box_id INTEGER NOT NULL,                   
                CONSTRAINT wert_pk PRIMARY KEY (wert_id)
);

-- schlecht
DROP TABLE IF EXISTS import.schlecht CASCADE;
DROP SEQUENCE IF EXISTS import.schlecht_seq;
CREATE SEQUENCE import.schlecht_seq;
CREATE TABLE import.schlecht (
                schlecht_id INTEGER NOT NULL DEFAULT nextval('import.schlecht_seq'),
                wert_id INTEGER NOT NULL,
                datum TIMESTAMP DEFAULT now() NOT NULL,
                grund varchar(200),
                box_id INTEGER NOT NULL,                   
                CONSTRAINT schlecht_pk PRIMARY KEY (schlecht_id)
);

-- mehr
DROP TABLE IF EXISTS import.mehr CASCADE;
CREATE TABLE import.mehr (
                wert_id INTEGER NOT NULL,
                inhalt VARCHAR,
                box_id INTEGER NOT NULL,                  
                CONSTRAINT mehr_pk PRIMARY KEY (wert_id)
);

-- obj
DROP TABLE IF EXISTS import.obj CASCADE;
DROP SEQUENCE IF EXISTS import.obj_seq;
CREATE SEQUENCE import.obj_seq;
CREATE TABLE import.obj (
                obj_id INTEGER NOT NULL DEFAULT nextval('import.obj_seq'),
                obj_typ smallint NOT NULL,               
                box_id INTEGER NOT NULL,               
                CONSTRAINT obj_pk PRIMARY KEY (obj_id)
);


-- bez
DROP TABLE IF EXISTS import.bez CASCADE;
DROP SEQUENCE IF EXISTS import.bez_seq;
CREATE SEQUENCE import.bez_seq;
CREATE TABLE import.bez (
                bez_id INTEGER NOT NULL DEFAULT nextval('import.bez_seq'),
                bez_typ smallint NOT NULL,
                obj_id_a INTEGER NOT NULL,
                obj_id_b INTEGER NOT NULL,
                box_id INTEGER NOT NULL,                
                CONSTRAINT bez_pk PRIMARY KEY (bez_id)
);

-- qk-attr-wert
DROP TABLE IF EXISTS import.qk_attr_wert CASCADE;
DROP SEQUENCE IF EXISTS import.qk_attr_wert_seq;
CREATE SEQUENCE import.qk_attr_wert_seq;
CREATE TABLE import.qk_attr_wert (
                id INTEGER NOT NULL DEFAULT nextval('import.qk_attr_wert_seq'),
                attr_typ smallint NOT NULL,
                syntax_art smallint,                
                inhalt VARCHAR,
                von TIMESTAMP,
                bis TIMESTAMP,
                wann TIMESTAMP NOT NULL DEFAULT now(),
                box_id INTEGER NOT NULL, 
                obj_id INTEGER NOT NULL,                                                               
                attr_id INTEGER,
                wert_id INTEGER,
                done smallint NOT NULL DEFAULT 0,                 
                CONSTRAINT qk_attr_wert_pk PRIMARY KEY (id)
);

-- tip
-- DROP TABLE IF EXISTS import.tip CASCADE;
-- DROP SEQUENCE IF EXISTS import.tip_seq;
-- CREATE SEQUENCE import.tip_seq;
-- CREATE TABLE import.tip (
--                 id INTEGER NOT NULL DEFAULT nextval('import.tip_seq'),
--                 sys_erstellt TIMESTAMP NOT NULL DEFAULT now(),
--                 sys_verarbeitet TIMESTAMP, 
--                 sys_aktiv INTEGER NOT NULL DEFAULT 1,                 
--                 obj_id INTEGER,
--                 attr_typ smallint,
--                 attr_tech_name VARCHAR(100),              
--                 inhalt VARCHAR,
--                 von TIMESTAMP,
--                 bis TIMESTAMP,
--                 wann TIMESTAMP,
--                 tip_kategory INTEGER,
--                 tip_kategory2 INTEGER,
--                 tip_person VARCHAR(60),
--                 tip_orga VARCHAR(60),
--                 tip_programm VARCHAR(60),
--                 tip_thema VARCHAR(60),
--                 tip_aktion VARCHAR(60),
--                 tip_datum TIMESTAMP,
--                 tip_doku VARCHAR(200),                                
--                 CONSTRAINT tip_pk PRIMARY KEY (id)
-- );

-- frish
DROP TABLE IF EXISTS import.frisch CASCADE;
DROP SEQUENCE IF EXISTS import.frisch_seq;
CREATE SEQUENCE import.frisch_seq;
CREATE TABLE import.frisch (
                id INTEGER NOT NULL DEFAULT nextval('import.frisch_seq'),
                obj_id INTEGER NOT NULL,
                box_id INTEGER NOT NULL,                           
                CONSTRAINT frisch_pk PRIMARY KEY (id)
);

-- mig
--DROP TABLE IF EXISTS import.mig;
--CREATE TABLE import.mig (
--                mig_id INTEGER NOT NULL,
--                zeit_angefordert TIMESTAMP,
--                zeit_migriert TIMESTAMP,
--                zeit_abgeschlossen TIMESTAMP,
--                CONSTRAINT mig_pk PRIMARY KEY (mig_id)
--);

-- mig_obj
DROP TABLE IF EXISTS import.mig_obj;
DROP SEQUENCE IF EXISTS import.mig_obj_seq;
CREATE SEQUENCE import.mig_obj_seq;
CREATE TABLE import.mig_obj (               
                mig_obj_id INTEGER NOT NULL DEFAULT nextval('import.mig_obj_seq'),
                datum TIMESTAMP NOT NULL DEFAULT now(),
                uk VARCHAR NOT NULL,
                uk_attr_usg_id INTEGER NOT NULL,
                uk_inhalt VARCHAR NOT NULL,
                master_obj_id INTEGER NOT NULL,
                slave_obj_id INTEGER NOT NULL,
                CONSTRAINT mig_obj_pk PRIMARY KEY (mig_obj_id)
);

-- mig_obj_hist
DROP TABLE IF EXISTS import.mig_obj_hist;
CREATE TABLE import.mig_obj_hist (               
                mig_obj_id INTEGER,
                datum TIMESTAMP,
                uk VARCHAR NOT NULL,
                uk_attr_usg_id INTEGER NOT NULL,
                uk_inhalt VARCHAR NOT NULL,
                master_obj_id INTEGER NOT NULL,
                slave_obj_id INTEGER NOT NULL,
                CONSTRAINT mig_obj_hist_pk PRIMARY KEY (mig_obj_id)
);

-- attr
DROP INDEX IF EXISTS import_idx_attr_obj_id;
CREATE INDEX import_idx_attr_obj_id ON import.attr (obj_id);

DROP INDEX IF EXISTS import_idx_attr_attr_typ;
CREATE INDEX import_idx_attr_attr_typ ON import.attr (attr_typ);

DROP INDEX IF EXISTS import_idx_attr_top_aktuell;
CREATE INDEX import_idx_attr_top_aktuell ON import.attr (top_aktuell);

-- wert
DROP INDEX IF EXISTS import_idx_wert_attr_id;
CREATE INDEX import_idx_wert_attr_id ON import.wert (attr_id);

DROP INDEX IF EXISTS import_idx_wert_wann;
CREATE INDEX import_idx_wert_wann ON import.wert (wann);

DROP INDEX IF EXISTS import_idx_wert_heal_double_werte;
CREATE INDEX import_idx_wert_heal_double_werte ON import.wert (attr_id, inhalt, von, bis);

DROP INDEX IF EXISTS import_idx_wert_top;
CREATE INDEX import_idx_wert_top ON import.wert (attr_id, wann desc);

-- obj


-- qk_attr_wert
DROP INDEX IF EXISTS import_idx_qk_attr_wert_done;
CREATE INDEX  import_idx_qk_attr_wert_done ON  import.qk_attr_wert (done);

-- index auf box_id
-- box: obj
DROP INDEX IF EXISTS import_idx_obj_box_id;
CREATE INDEX import_idx_obj_box_id ON import.obj (box_id);

-- box: bez
DROP INDEX IF EXISTS import_idx_bez_box_id;
CREATE INDEX import_idx_bez_box_id ON import.bez (box_id);

-- box: attr
DROP INDEX IF EXISTS import_idx_attr_box_id;
CREATE INDEX import_idx_attr_box_id ON import.attr (box_id);

-- box: wert
DROP INDEX IF EXISTS import_idx_wert_box_id;
CREATE INDEX import_idx_wert_box_id ON import.wert (box_id, attr_id);

-- box: mehr
DROP INDEX IF EXISTS import_idx_mehr_box_id;
CREATE INDEX import_idx_mehr_box_id ON import.mehr (box_id);

-- box: frisch
DROP INDEX IF EXISTS import_idx_frisch_box_id;
CREATE INDEX import_idx_frisch_box_id ON import.frisch (box_id);

CREATE VIEW import.kopf AS
 SELECT n1.obj_id,
    n1.obj_typ,
    n1.typ_tech_name,
    n1.typ_name,
    n2.kopf
   FROM ( SELECT obj.obj_id,
            obj.obj_typ,
            ot.tech_name AS typ_tech_name,
            ot.name AS typ_name,
            ot.kopf_attr_typ
           FROM import.obj obj,
            vok."to" ot
          WHERE obj.obj_typ = ot.obj_typ) n1
     LEFT JOIN ( SELECT attr.obj_id,
            attr.attr_typ,
            wert.inhalt AS kopf
           FROM import.attr attr,
            import.wert wert
          WHERE attr.attr_id = wert.attr_id AND wert.ist_top = 1) n2 ON n1.obj_id = n2.obj_id AND n1.kopf_attr_typ = n2.attr_typ;

