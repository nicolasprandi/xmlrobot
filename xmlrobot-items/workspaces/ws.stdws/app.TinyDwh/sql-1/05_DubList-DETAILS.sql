select
 usg.attr_usg_id || '|' ||wert.inhalt as uk,
 obj.obj_id,
 obj.obj_typ,
 attr.attr_id,
 attr.attr_typ,
 wert.wert_id,
 usg.attr_usg_id as uk_attr_usg_id, 
 wert.inhalt as uk_inhalt
from 
  int.obj as obj,
  int.attr as attr,
  int.wert as wert,
  vok.attr_usg as usg
where
  obj.info_id = attr.info_id and
  attr.attr_id = wert.attr_id and
  attr.attr_typ = usg.attr_typ and
  usg.obj_typ is not null and
  usg.obj_typ = obj.obj_typ and
  usg.varianz = 1
order by
  usg.attr_usg_id,
  wert.inhalt;