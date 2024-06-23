-- src/main/resources/db/migration/V1__Create_activite_table.sql
CREATE TABLE activite (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date VARCHAR(50) NOT NULL,
    commentaire VARCHAR(255) NOT NULL,
    statut VARCHAR(50) NOT NULL
);
