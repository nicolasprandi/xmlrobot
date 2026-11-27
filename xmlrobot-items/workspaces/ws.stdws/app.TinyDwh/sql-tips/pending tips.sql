select 
inhalt,
attr_tech_name,
obj_id,
tip_doku,
id
from import.tip where sys_verarbeitet is null  order by wann desc;