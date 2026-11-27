select
 usg.attr_usg_id || '|' || wert.inhalt as uk,
 min (obj.obj_id) as master_obj_id
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
group by
  usg.attr_usg_id,
  wert.inhalt
order by
  uk;