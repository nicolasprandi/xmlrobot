select
 count(*) as cnt,
 min(obj.obj_typ) as obj_typ,
 min(typo.name) as name
from 
  int.obj as obj,
  vok.to as typo
where
  typo.obj_typ = obj.obj_typ
group by
  obj.obj_typ;