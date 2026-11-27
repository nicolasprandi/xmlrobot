select                                                
 min(bez.bez_typ) as bez_typ, 
 min(obj_a.obj_typ) as obj_typ_a,
 min(obj_b.obj_typ) as obj_typ_b,
 count(*) as times,
 concat ('bez(',min(bez.bez_typ) ,'): objA(',min(obj_a.obj_typ),') x objB(',min(obj_b.obj_typ),') = ',cast(count(*) as varchar(10)),' times') as explained
from 
  i_bez as bez,
  i_obj as obj_a,
  i_obj as obj_b
where
  bez.obj_id_a = obj_a.obj_id and
  bez.obj_id_b = obj_b.obj_id
group by
  bez.bez_typ,
  obj_a.obj_typ,
  obj_b.obj_typ
order by
  bez.bez_typ,
  obj_a.obj_typ,
  obj_b.obj_typ;
  