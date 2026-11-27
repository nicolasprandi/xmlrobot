select 
count(*) as anzahl,
'[zustand 1 -Ready for Integr]' as titel,
min(box_id) as min_box_id,
max(box_id) as max_box_id
from i_box
where zustand=1
UNION
select 
count(*) as anzahl,
'[zustand 2 -Integrated]' as titel,
min(box_id) as min_box_id,
max(box_id) as max_box_id
from i_box
where zustand=2
UNION
select 
count(*) as anzahl,
'[zustand 0 -Importing]' as titel,
min(box_id) as min_box_id,
max(box_id) as max_box_id
from i_box
where zustand=0
;