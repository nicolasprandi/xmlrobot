select
 mig_obj.mig_obj_id as mig_id_s,
 mig_obj.slave_obj_id as obj_id_s,
 mig_obj.slave_info_id as info_id_s, 
 attr.attr_typ as attr_typ_s, 
 attr.attr_id as attr_id_s
from 
  int.mig_obj as mig_obj,
  int.mig as mig,
  int.attr as attr
WHERE 
  mig.mig_id = mig_obj.mig_id and
  mig.zeit_migriert is null and
  attr.info_id = mig_obj.slave_info_id
ORDER BY
  mig_obj.mig_obj_id,
  attr.attr_typ;