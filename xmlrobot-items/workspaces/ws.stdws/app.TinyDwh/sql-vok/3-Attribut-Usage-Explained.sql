select 
usg.attr_usg_id,
'<' || tob.tech_name || '> has <'  ||  ta.tech_name|| '>' as explained,
usg.obj_typ,
usg.attr_typ,
tob.tech_name as obj_tech_name,
ta.tech_name as attr_tech_name,
tob.name as obj_name,
ta.name as attr_name,
usg.varianz,
usg.varianz_text,
usg.ident,
usg.ident_text,
usg.ident_mehr,
usg.ident_club 
from 
t_u as usg, 
t_o as tob,
t_a as ta
where
  usg.obj_typ = tob.obj_typ and
  usg.attr_typ = ta.attr_typ;