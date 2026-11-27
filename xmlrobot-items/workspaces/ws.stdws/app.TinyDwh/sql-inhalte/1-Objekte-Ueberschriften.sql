select
 obj.obj_id,
 obj.obj_typ,
 obj.box_id,
 ot.tech_name,
 ot.name
from
int.obj,
vok.to as ot
where
obj.obj_typ = ot.obj_typ
order by obj.obj_id desc;