select
*
from
int.obj,
int.kopf
where
obj.obj_id = kopf.obj_id
order by obj.obj_id desc;