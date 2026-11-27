select
 count(*)
from 
  int.mig_obj as mig_obj,
  int.mig as mig,
  int.obj as obj,
  int.attr as attr
WHERE 
  mig.mig_id = mig_obj.mig_id and
  mig.zeit_migriert is null and
  mig_obj.slave_obj_id = obj.obj_id and
  attr.info_id = obj.info_id;

select
 attr.attr_id as del_attr_id
from 
  int.mig_obj as mig_obj,
  int.mig as mig,
  int.obj as obj,
  int.attr as attr
WHERE 
  mig.mig_id = mig_obj.mig_id and
  mig.zeit_migriert is null and
  mig_obj.slave_obj_id = obj.obj_id and
  attr.info_id = obj.info_id
ORDER BY
  attr.attr_id
LIMIT 10000;

select
 count(*)
from 
  int.mig_obj as mig_obj,
  int.mig as mig,
  int.obj as obj
WHERE 
  mig.mig_id = mig_obj.mig_id and
  mig.zeit_migriert is null and
  mig_obj.slave_obj_id = obj.obj_id;

select
 obj.obj_id as del_obj_id
from 
  int.mig_obj as mig_obj,
  int.mig as mig,
  int.obj as obj
WHERE 
  mig.mig_id = mig_obj.mig_id and
  mig.zeit_migriert is null and
  mig_obj.slave_obj_id = obj.obj_id
LIMIT 20;

select
 count(*)
from 
  int.mig_obj as mig_obj,
  int.mig as mig,
  int.obj as obj
WHERE 
  mig.mig_id = mig_obj.mig_id and
  mig.zeit_migriert is null and
  mig_obj.slave_obj_id = obj.obj_id;

select
 obj.info_id as del_info_id
from 
  int.mig_obj as mig_obj,
  int.mig as mig,
  int.obj as obj
WHERE 
  mig.mig_id = mig_obj.mig_id and
  mig.zeit_migriert is null and
  mig_obj.slave_obj_id = obj.obj_id
LIMIT 20;