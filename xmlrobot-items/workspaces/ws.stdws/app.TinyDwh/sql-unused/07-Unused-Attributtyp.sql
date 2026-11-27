select                                      
 attr_typ         
from 
  t_a
where not exists (
  select attr_typ 
  from attr
  where attr.attr_typ = t_a.attr_typ
);
  