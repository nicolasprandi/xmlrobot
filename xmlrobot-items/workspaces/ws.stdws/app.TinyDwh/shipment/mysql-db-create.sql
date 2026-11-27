-- Copyright © 2018-2021 Nicolas Prandstaetter. All rights reserved.
-- beziehungstypen mysql

CREATE TABLE t_b (
                bez_typ smallint NOT NULL
);

-- objekttypen
CREATE TABLE t_o (
                obj_typ smallint NOT NULL,
                kopf_attr_typ smallint
);

-- attributtypen
CREATE TABLE t_a (
                attr_typ smallint NOT NULL,  
                final_typ smallint  NOT NULL       
);

-- attribute usage    object x attribute
CREATE TABLE t_u (
                attr_usg_id INTEGER NOT NULL AUTO_INCREMENT,
                attr_typ smallint NOT NULL,
                obj_typ smallint NOT NULL,
                ident smallint  NOT NULL,
                PRIMARY KEY (attr_usg_id)
);

-- objekt report
CREATE TABLE r_o (
                obj_typ smallint NOT NULL,
                anzahl INTEGER NOT NULL
);


-- Copyright © 2018-2021 Nicolas Prandstaetter. All rights reserved.
-- box
CREATE TABLE box (
                box_id INTEGER NOT NULL AUTO_INCREMENT,
                zustand smallint NOT NULL DEFAULT 1, 
                zeit_import TIMESTAMP,
                zeit_qk TIMESTAMP,
                zeit_export TIMESTAMP,
                qualitaet smallint,
                uri VARCHAR(500),
                beschreibung VARCHAR(500),
                PRIMARY KEY (box_id)
);

-- attr
CREATE TABLE attr (
                attr_id INTEGER NOT NULL AUTO_INCREMENT,
                obj_id INTEGER NOT NULL,
                attr_typ smallint NOT NULL,
                box_id INTEGER NOT NULL,               
                PRIMARY KEY (attr_id)
);


-- wert
CREATE TABLE wert (
                wert_id INTEGER NOT NULL AUTO_INCREMENT,
                attr_id INTEGER NOT NULL,
                syntax_art smallint,
                inhalt TEXT(60) NOT NULL,
                inhalt2 TEXT(60),
                von TIMESTAMP,
                bis TIMESTAMP,
                wann TIMESTAMP DEFAULT now() NOT NULL,
                ist_top smallint DEFAULT 0 NOT NULL,
                hat_mehr smallint DEFAULT 0 NOT NULL,
                box_id INTEGER NOT NULL,                   
                PRIMARY KEY (wert_id)
);

-- schlecht
CREATE TABLE schlecht (
                schlecht_id INTEGER NOT NULL AUTO_INCREMENT,
                wert_id INTEGER NOT NULL,
                datum TIMESTAMP DEFAULT now() NOT NULL,
                grund varchar(200),
                box_id INTEGER NOT NULL,                   
                PRIMARY KEY (schlecht_id)
);

-- mehr
CREATE TABLE mehr (
                wert_id INTEGER NOT NULL,
                inhalt TEXT,
                box_id INTEGER NOT NULL,                  
                PRIMARY KEY (wert_id)
);

-- obj
CREATE TABLE obj (
                obj_id INTEGER NOT NULL AUTO_INCREMENT,
                obj_typ smallint NOT NULL,
                pub_ist_aktuell smallint DEFAULT 0,                
                box_id INTEGER NOT NULL,               
                PRIMARY KEY (obj_id)
);


-- bez
CREATE TABLE bez (
                bez_id INTEGER NOT NULL AUTO_INCREMENT,
                bez_typ smallint NOT NULL,
                obj_id_a INTEGER NOT NULL,
                obj_id_b INTEGER NOT NULL,
                box_id INTEGER NOT NULL,                
                PRIMARY KEY (bez_id)
);

-- qk-attr-wert
CREATE TABLE qk_attr_wert (
                id INTEGER NOT NULL AUTO_INCREMENT,
                attr_typ smallint NOT NULL,
                syntax_art smallint,                
                inhalt VARCHAR(5000),
                von TIMESTAMP,
                bis TIMESTAMP,
                wann TIMESTAMP NOT NULL DEFAULT now(),
                box_id INTEGER NOT NULL, 
                obj_id INTEGER NOT NULL,                                                               
                attr_id INTEGER,
                wert_id INTEGER,
                done smallint NOT NULL DEFAULT 0,                 
                PRIMARY KEY (id)
);

-- frisch
CREATE TABLE frisch (
                id INTEGER NOT NULL AUTO_INCREMENT,
                obj_id INTEGER NOT NULL,
                box_id INTEGER NOT NULL,                           
                PRIMARY KEY (id)
);

-- mig_obj
CREATE TABLE mig_obj (               
                mig_obj_id INTEGER NOT NULL AUTO_INCREMENT,
                datum TIMESTAMP NOT NULL DEFAULT now(),
                uk TEXT(80) NOT NULL,
                uk_attr_usg_id INTEGER NOT NULL,
                uk_inhalt TEXT(80) NOT NULL,
                master_obj_id INTEGER NOT NULL,
                slave_obj_id INTEGER NOT NULL,
                PRIMARY KEY (mig_obj_id)
);

-- mig_obj_hist
CREATE TABLE mig_obj_hist (               
                mig_obj_id INTEGER,
                datum TIMESTAMP,
                uk TEXT(80) NOT NULL,
                uk_attr_usg_id INTEGER NOT NULL,
                uk_inhalt TEXT(80) NOT NULL,
                master_obj_id INTEGER NOT NULL,
                slave_obj_id INTEGER NOT NULL,
                PRIMARY KEY (mig_obj_id)
);

-- attr
CREATE INDEX idx_attr_obj_id ON attr (obj_id);

CREATE INDEX idx_attr_attr_typ ON attr (attr_typ);

-- wert
CREATE INDEX idx_wert_attr_id ON wert (attr_id);

CREATE INDEX idx_wert_wann ON wert (wann);

CREATE INDEX idx_wert_heal_double_werte ON wert (attr_id, inhalt(60), von, bis);

CREATE INDEX idx_wert_top ON wert (attr_id, wann desc);

-- obj
CREATE INDEX idx_obj_pub_ist_aktuell ON obj (pub_ist_aktuell);

-- qk_attr_wert
-- CREATE INDEX  idx_qk_attr_wert_done ON  qk_attr_wert (done);
                                                         
CREATE INDEX  idx_qk_attr_wert_sort ON qk_attr_wert (done,obj_id, attr_typ);

-- index auf box_id
-- box: obj
CREATE INDEX idx_obj_box_id ON obj (box_id);

-- box: bez
CREATE INDEX idx_bez_box_id ON bez (box_id);

-- box: attr;
CREATE INDEX idx_attr_box_id ON attr (box_id);

-- box: wert;
CREATE INDEX idx_wert_box_id ON wert (box_id);

-- box: mehr
CREATE INDEX idx_mehr_box_id ON mehr (box_id);

-- box: frisch;
CREATE INDEX idx_frisch_box_id ON frisch (box_id);

CREATE VIEW kopf AS
 SELECT n1.obj_id,
    n1.obj_typ,
    n2.kopf
   FROM ( SELECT obj.obj_id,
            obj.obj_typ,
            ot.kopf_attr_typ
           FROM obj obj,
            t_o ot
          WHERE obj.obj_typ = ot.obj_typ) n1
     LEFT JOIN ( SELECT attr.obj_id,
            attr.attr_typ,
            wert.inhalt AS kopf
           FROM attr attr,
            wert wert
          WHERE attr.attr_id = wert.attr_id AND wert.ist_top = 1) n2 ON n1.obj_id = n2.obj_id AND n1.kopf_attr_typ = n2.attr_typ;
  
-- Copyright © 2018-2021 Nicolas Prandstaetter. All rights reserved.
-- box
CREATE TABLE i_box (
                box_id INTEGER NOT NULL AUTO_INCREMENT,
                zustand smallint NOT NULL DEFAULT 1, 
                zeit_import TIMESTAMP,
                zeit_qk TIMESTAMP,
                zeit_export TIMESTAMP,
                qualitaet smallint,
                uri VARCHAR(500),
                beschreibung VARCHAR(500),
                PRIMARY KEY (box_id)
);

-- attr
CREATE TABLE i_attr (
                attr_id INTEGER NOT NULL AUTO_INCREMENT,
                obj_id INTEGER NOT NULL,
                attr_typ smallint NOT NULL,
                box_id INTEGER NOT NULL,               
                PRIMARY KEY (attr_id)
);


-- wert
CREATE TABLE i_wert (
                wert_id INTEGER NOT NULL AUTO_INCREMENT,
                attr_id INTEGER NOT NULL,
                syntax_art smallint,
                inhalt TEXT(60) NOT NULL,
                inhalt2 TEXT(60),
                von TIMESTAMP,
                bis TIMESTAMP,
                wann TIMESTAMP DEFAULT now() NOT NULL,
                ist_top smallint DEFAULT 0 NOT NULL,
                hat_mehr smallint DEFAULT 0 NOT NULL,
                box_id INTEGER NOT NULL,                   
                PRIMARY KEY (wert_id)
);

-- schlecht
CREATE TABLE i_schlecht (
                schlecht_id INTEGER NOT NULL AUTO_INCREMENT,
                wert_id INTEGER NOT NULL,
                datum TIMESTAMP DEFAULT now() NOT NULL,
                grund varchar(200),
                box_id INTEGER NOT NULL,                   
                PRIMARY KEY (schlecht_id)
);

-- mehr
CREATE TABLE i_mehr (
                wert_id INTEGER NOT NULL,
                inhalt TEXT,
                box_id INTEGER NOT NULL,                  
                PRIMARY KEY (wert_id)
);

-- obj
CREATE TABLE i_obj (
                obj_id INTEGER NOT NULL AUTO_INCREMENT,
                obj_typ smallint NOT NULL,
                pub_ist_aktuell smallint DEFAULT 0,                
                box_id INTEGER NOT NULL,               
                PRIMARY KEY (obj_id)
);


-- bez
CREATE TABLE i_bez (
                bez_id INTEGER NOT NULL AUTO_INCREMENT,
                bez_typ smallint NOT NULL,
                obj_id_a INTEGER NOT NULL,
                obj_id_b INTEGER NOT NULL,
                box_id INTEGER NOT NULL,                
                PRIMARY KEY (bez_id)
);

-- qk-attr-wert
CREATE TABLE i_qk_attr_wert (
                id INTEGER NOT NULL AUTO_INCREMENT,
                attr_typ smallint NOT NULL,
                syntax_art smallint,                
                inhalt VARCHAR(5000),
                von TIMESTAMP,
                bis TIMESTAMP,
                wann TIMESTAMP NOT NULL DEFAULT now(),
                box_id INTEGER NOT NULL, 
                obj_id INTEGER NOT NULL,                                                               
                attr_id INTEGER,
                wert_id INTEGER,
                done smallint NOT NULL DEFAULT 0,                 
                PRIMARY KEY (id)
);

-- frisch
CREATE TABLE i_frisch (
                id INTEGER NOT NULL AUTO_INCREMENT,
                obj_id INTEGER NOT NULL,
                box_id INTEGER NOT NULL,                           
                PRIMARY KEY (id)
);

-- mig_obj
CREATE TABLE i_mig_obj (               
                mig_obj_id INTEGER NOT NULL AUTO_INCREMENT,
                datum TIMESTAMP NOT NULL DEFAULT now(),
                uk TEXT(80) NOT NULL,
                uk_attr_usg_id INTEGER NOT NULL,
                uk_inhalt TEXT(80) NOT NULL,
                master_obj_id INTEGER NOT NULL,
                slave_obj_id INTEGER NOT NULL,
                PRIMARY KEY (mig_obj_id)
);

-- mig_obj_hist
CREATE TABLE i_mig_obj_hist (               
                mig_obj_id INTEGER,
                datum TIMESTAMP,
                uk TEXT(80) NOT NULL,
                uk_attr_usg_id INTEGER NOT NULL,
                uk_inhalt TEXT(80) NOT NULL,
                master_obj_id INTEGER NOT NULL,
                slave_obj_id INTEGER NOT NULL,
                PRIMARY KEY (mig_obj_id)
);

-- attr
CREATE INDEX i_idx_attr_obj_id ON i_attr (obj_id);

CREATE INDEX i_idx_attr_attr_typ ON i_attr (attr_typ);

-- wert
CREATE INDEX i_idx_wert_attr_id ON i_wert (attr_id);

CREATE INDEX i_idx_wert_wann ON i_wert (wann);

CREATE INDEX i_idx_wert_heal_double_werte ON i_wert (attr_id, inhalt(60), von, bis);

CREATE INDEX i_idx_wert_top ON i_wert (attr_id, wann desc);

-- obj
CREATE INDEX i_idx_obj_pub_ist_aktuell ON i_obj (pub_ist_aktuell);

-- qk_attr_wert
-- CREATE INDEX  i_idx_qk_attr_wert_done ON  i_qk_attr_wert (done);
                                                         
CREATE INDEX  i_idx_qk_attr_wert_sort ON i_qk_attr_wert (done,obj_id, attr_typ);

-- index auf box_id
-- box: obj
CREATE INDEX i_idx_obj_box_id ON i_obj (box_id);

-- box: bez
CREATE INDEX i_idx_bez_box_id ON i_bez (box_id);

-- box: attr;
CREATE INDEX i_idx_attr_box_id ON i_attr (box_id);

-- box: wert;
CREATE INDEX i_idx_wert_box_id ON i_wert (box_id);

-- box: mehr
CREATE INDEX i_idx_mehr_box_id ON i_mehr (box_id);

-- box: frisch;
CREATE INDEX i_idx_frisch_box_id ON i_frisch (box_id);

CREATE VIEW i_kopf AS
 SELECT n1.obj_id,
    n1.obj_typ,
    n2.kopf
   FROM ( SELECT obj.obj_id,
            obj.obj_typ,
            ot.kopf_attr_typ
           FROM i_obj obj,
            t_o ot
          WHERE obj.obj_typ = ot.obj_typ) n1
     LEFT JOIN ( SELECT attr.obj_id,
            attr.attr_typ,
            wert.inhalt AS kopf
           FROM i_attr attr,
            i_wert wert
          WHERE attr.attr_id = wert.attr_id AND wert.ist_top = 1) n2 ON n1.obj_id = n2.obj_id AND n1.kopf_attr_typ = n2.attr_typ;

