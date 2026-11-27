select                                      
 bez_typ         
from 
  t_b
where not exists (
  select bez_typ 
  from bez
  where bez.bez_typ = t_b.bez_typ
);
  