select 
mig_obj.master_obj_id, 
mig_obj.slave_obj_id, 
count(*),
min(mig_obj_id) as keep_mig_obj_id,
min(mig.mig_id) as mig_id
from 
  int.mig_obj as mig_obj,
  int.mig as mig
WHERE 
  mig.mig_id = mig_obj.mig_id and
  mig.zeit_migriert is null
group by mig_obj.master_obj_id, mig_obj.slave_obj_id
HAVING count(*) > 1
; 