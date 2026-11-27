select      
	 n1.obj_id,
     n1.attr_id,
     n1.obj_typ_tech,
     n1.obj_typ_name,
     n1.attr_typ_tech,
     n1.inhalt,
     n1.von,
     n1.bis,
     n1.wann,
     n1.attr_typ_name,
     n1.obj_typ,
     n1.attr_typ,
     n2.attr_usg_id,
     n2.varianz,
     n2.varianz_text
from (
SELECT
     obj.obj_id,
     attr.attr_id,
     tobj.tech_name as obj_typ_tech,
     tobj.name as obj_typ_name,
     tattr.tech_name as attr_typ_tech,
     wert.inhalt,
     wert.von,
     wert.bis,
     wert.wann,
     tattr.name as attr_typ_name,
     obj.obj_typ,
    attr.attr_typ
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
ORDER BY
     obj.obj_id,
     attr.attr_id ) n1
left outer join (
    select * from vok.attr_usg
	) n2
on
(n1.obj_typ = n2.obj_typ and
 n1.attr_typ = n2.attr_typ);