select
 mig_obj.mig_obj_id as mig_id_m,
 mig_obj.master_obj_id as obj_id_m,
 mig_obj.master_info_id as info_id_m,
 attr.attr_typ as attr_typ_m, 
 attr.attr_id as attr_id_m,
 obj.obj_typ as obj_typ_m
from 
  int.mig_obj as mig_obj,
  int.mig as mig,
  int.attr as attr,
  int.obj as obj
WHERE 
  mig.mig_id = mig_obj.mig_id and
  mig.zeit_migriert is null and
  attr.info_id = mig_obj.master_info_id and
  obj.obj_id = mig_obj.master_obj_id
ORDER BY
  mig_obj.mig_obj_id,
  attr.attr_typ;