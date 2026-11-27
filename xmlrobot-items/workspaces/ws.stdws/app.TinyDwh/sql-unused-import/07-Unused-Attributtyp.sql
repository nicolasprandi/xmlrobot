select                                      
 attr_typ         
from 
  t_a
where not exists (
  select attr_typ 
  from i_attr as attr
  where attr.attr_typ = t_a.attr_typ
);
  