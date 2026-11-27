select 
  min(attr_typ) as attr_typ,
  count(*) as times,
 concat ('attr(',min(attr_typ),') = ',cast(count(*) as varchar(10)),' times') as explained
from i_attr as attr
group by attr_typ
order by attr_typ;
