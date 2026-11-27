select                                      
 min(bez_typ) as bez_typ,
 count(*) as times         
from 
  i_bez as bez
where not exists (
  select bez_typ 
  from t_b
  where bez.bez_typ = t_b.bez_typ
)
group by bez_typ;
  