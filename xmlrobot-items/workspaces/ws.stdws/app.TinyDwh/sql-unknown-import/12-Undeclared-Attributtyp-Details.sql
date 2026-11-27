select                                      
 *        
from 
  i_attr as attr
where not exists (
  select attr_typ 
  from t_a
  where attr.attr_typ = t_a.attr_typ
)
order by attr_id;
  