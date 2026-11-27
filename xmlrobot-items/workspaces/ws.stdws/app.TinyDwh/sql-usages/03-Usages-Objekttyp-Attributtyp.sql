select    
     n1.attr_typ , 
     n1.obj_typ ,
     n1.times,
     concat ('attr(',n1.attr_typ,') x obj(',n1.obj_typ,') = ',n1.times,' times') as explained
from (
SELECT
     count(*) as times,  
     obj.obj_typ as obj_typ,
     attr.attr_typ as attr_typ
FROM
     obj as obj,
     attr as attr,
     wert as wert,
     t_o as tobj,
     t_a as tattr         
WHERE
     obj.obj_id = attr.obj_id and
     wert.attr_id = attr.attr_id and
     tobj.obj_typ = obj.obj_typ and
     tattr.attr_typ = attr.attr_typ
group BY
     obj.obj_typ,
     attr.attr_typ
order by
     attr.attr_typ,
     obj.obj_typ
      ) n1
left outer join (
    select obj_typ, attr_typ from t_u
	) n2  
  on
  (n1.obj_typ = n2.obj_typ and
   n1.attr_typ = n2.attr_typ)
order by
n1.attr_typ,
n1.obj_typ
;