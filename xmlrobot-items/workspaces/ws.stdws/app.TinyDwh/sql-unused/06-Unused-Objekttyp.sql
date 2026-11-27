select                                      
 obj_typ         
from 
  t_o
where not exists (
  select obj_typ 
  from obj
  where obj.obj_typ = t_o.obj_typ
);
  