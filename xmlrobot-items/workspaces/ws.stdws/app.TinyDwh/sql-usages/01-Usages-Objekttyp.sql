select     
 min(obj.obj_typ) as obj_typ ,
 count(*) as times,
 concat ('obj(',min(obj.obj_typ),') = ',cast(count(*) as varchar(10)),' times') as explained
from 
  obj as obj
group by
  obj.obj_typ
order by
  obj.obj_typ;