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
     wert.wert_id
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
     attr.attr_id;