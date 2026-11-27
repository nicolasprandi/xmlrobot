select 
( select max(box_id) from i_box) as  max_box_id,
( select max(obj_id) from i_obj) as  max_obj_id,
( select max(attr_id) from i_attr) as  max_attr_id,
( select max(wert_id) from i_wert) as  max_wert_id,
( select max(box_id) from i_box) as  max_box_id_int,
( select max(obj_id) from i_obj) as  max_obj_id_int,
( select max(attr_id) from i_attr) as  max_attr_id_int,
( select max(wert_id) from i_wert) as  max_wert_id_int;