select                                      
 min(attr_typ) as attr_typ,
 count(*) as times        
from 
  attr
where not exists (
  select attr_typ 
  from t_a
  where attr.attr_typ = t_a.attr_typ
)
group by attr_typ;
  