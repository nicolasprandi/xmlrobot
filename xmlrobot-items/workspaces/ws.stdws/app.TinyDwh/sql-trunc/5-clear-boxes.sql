UPDATE import.box 
SET zeit_export = NULL 
WHERE 
box.zeit_export is not null ;