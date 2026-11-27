

GRANT ALL PRIVILEGES ON *.* TO 'username'@'localhost' IDENTIFIED BY 'password';


GRANT ALL PRIVILEGES ON *.* TO 'formixat'@'localhost' IDENTIFIED BY '23!-AbEgIdA?78';

CREATE USER 'jeffrey'@'localhost' IDENTIFIED BY 'password';

CREATE USER 'formixat'@'localhost' IDENTIFIED BY '23!-AbEgIdA?78';

GRANT ALL ON formixat001.* TO 'formixat'@'localhost';
GRANT SELECT ON db2.invoice TO 'jeffrey'@'localhost';
ALTER USER 'jeffrey'@'localhost' WITH MAX_QUERIES_PER_HOUR 90;

drop upser 'formixat'@'localhost';

CREATE USER 'formixat' IDENTIFIED BY '23!-AbEgIdA?78';