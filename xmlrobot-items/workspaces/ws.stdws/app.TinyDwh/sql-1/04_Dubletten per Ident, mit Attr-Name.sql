select
usg.attr_usg_id,
max(ta.tech_name) as attr_typ,
wert.inhalt,
max(obj.obj_id) as head_obj_id,
count(*) as cnt
from 
  int.obj as obj,
  int.attr as attr,
  int.wert as wert,
  vok.attr_usg as usg,
  vok.ta as ta
where
  obj.info_id = attr.info_id and
  attr.attr_id = wert.attr_id and
  attr.attr_typ = usg.attr_typ and
  usg.obj_typ is not null and
  usg.obj_typ = obj.obj_typ and
  usg.varianz = 1 and
  ta.attr_typ = attr.attr_typ
group by usg.attr_usg_id, wert.inhalt
limit 100000;