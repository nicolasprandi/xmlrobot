select                                      
 min(obj_typ) as obj_typ,
 count(*) as times         
from 
  obj
where not exists (
  select obj_typ 
  from t_o
  where obj.obj_typ = t_o.obj_typ
)
group by obj_typ;


  