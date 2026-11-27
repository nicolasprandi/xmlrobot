select 
wert.inhalt,
schlecht.grund,
ta.tech_name,
ta.format,
schlecht.schlecht_id,
schlecht.wert_id,
schlecht.box_id,
attr.attr_id,
ta.attr_typ,
ta.final_typ
from 
import.schlecht as schlecht,
import.wert as wert,
import.attr as attr,
vok.ta as ta
where
 wert.wert_id = schlecht.wert_id and
 wert.attr_id = attr.attr_id and
 attr.attr_typ = ta.attr_typ;