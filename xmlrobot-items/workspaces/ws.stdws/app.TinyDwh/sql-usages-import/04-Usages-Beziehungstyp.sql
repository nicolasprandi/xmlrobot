select                                                
 min(bez.bez_typ) as bez_typ ,
 count(*) as times,
 concat ('bez(',min(bez.bez_typ),') = ',cast(count(*) as varchar(10)),' times') as explained
from 
  i_bez as bez
group by
  bez.bez_typ
order by
  bez.bez_typ;