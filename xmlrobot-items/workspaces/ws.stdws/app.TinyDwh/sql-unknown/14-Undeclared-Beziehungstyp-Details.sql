select                                      
 *         
from 
  bez
where not exists (
  select bez_typ 
  from t_b
  where bez.bez_typ = t_b.bez_typ
)
order by bez_id;
  