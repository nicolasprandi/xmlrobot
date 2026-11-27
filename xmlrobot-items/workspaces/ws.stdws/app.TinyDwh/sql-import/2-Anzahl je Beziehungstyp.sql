select
 count(*) as anzahl_pro_bez_typ,
 min(typb.tech_name) as bez_typ_tech_name,
 min(bez.bez_typ) as bez_typ 
from 
  import.bez as bez,
  vok.tb as typb
where
  typb.bez_typ = bez.bez_typ
group by
  bez.bez_typ
order by
  bez.bez_typ;