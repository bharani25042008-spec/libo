CREATE DATABASE IF NOT EXISTS librotrack;

CREATE USER IF NOT EXISTS 'librotrack'@'localhost' IDENTIFIED BY 'librotrack';

GRANT ALL PRIVILEGES ON librotrack.* TO 'librotrack'@'localhost';
FLUSH PRIVILEGES;
