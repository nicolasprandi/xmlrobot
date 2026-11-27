select
A.box_id,
B.zustand,
A.anzahl_der_objekte,
B.titel,
B.kurzname,
B.zeit_import,
B.zeit_export,
B.beschreibung
from
( select 
max(box_id) as box_id,
count(*) as anzahl_der_objekte 
from i_obj
group by box_id ) AS A
LEFT JOIN (
select 
box_id,
titel,
kurzname,
zustand,
beschreibung,
zeit_import,
zeit_export
from 
i_box ) AS B
ON
A.box_id = B.box_id;