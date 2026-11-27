select      
     n1.cnt_obj_attr,
     n1.obj_typ_tech_name,
     n1.attr_typ_tech_name, 
     n2.varianz_text,     
     n1.obj_typ,
     n1.attr_typ,
     n1.obj_typ_name,
     n1.attr_typ_name,
     n2.attr_usg_id,
     n2.varianz
from (
SELECT
     count(*) as cnt_obj_attr,
     max( tobj.tech_name) as obj_typ_tech_name,
     max (tattr.tech_name) as attr_typ_tech_name,     
     obj.obj_typ as obj_typ,
     attr.attr_typ as attr_typ,
     max (tobj.name) as obj_typ_name,
     max (tattr.name) as attr_typ_name
FROM
     int.obj as obj,
     int.attr as attr,
     int.wert as wert,
     vok.to as tobj,
     vok.ta as tattr         
WHERE
     obj.info_id = attr.info_id and
     wert.attr_id = attr.attr_id and
     tobj.obj_typ = obj.obj_typ and
     tattr.attr_typ = attr.attr_typ
group BY
     obj.obj_typ,
     attr.attr_typ
order by
     obj.obj_typ,
     attr.attr_typ ) n1
left outer join (
    select * from vok.attr_usg
	) n2
on
(n1.obj_typ = n2.obj_typ and
 n1.attr_typ = n2.attr_typ);