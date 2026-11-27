select
 count(*) as anzahl_pro_objekt_typ,
 min(typo.name) as objekt_typ,
 min(obj.obj_typ) as obj_typ 
from 
  import.obj as obj,
  vok.to as typo
where
  typo.obj_typ = obj.obj_typ
group by
  obj.obj_typ
order by
  obj.obj_typ;