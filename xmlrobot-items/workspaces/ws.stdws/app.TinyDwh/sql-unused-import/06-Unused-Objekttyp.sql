select                                      
 obj_typ         
from 
  t_o
where not exists (
  select obj_typ 
  from i_obj as obj
  where obj.obj_typ = t_o.obj_typ
);
  