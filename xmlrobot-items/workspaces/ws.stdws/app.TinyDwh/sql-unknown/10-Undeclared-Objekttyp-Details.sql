select                                      
 *         
from 
  obj
where not exists (
  select obj_typ 
  from t_o
  where obj.obj_typ = t_o.obj_typ
)
order by obj_id;


  