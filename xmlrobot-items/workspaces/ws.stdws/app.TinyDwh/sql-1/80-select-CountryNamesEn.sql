select
*
from 
 int.attr as attr,
 int.wert as wert
where
 attr.attr_typ = 2 and
 wert.attr_id = attr.attr_id;