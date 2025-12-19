CREATE TABLE users (
   id UUID PRIMARY KEY,
   name VARCHAR(255),
   email VARCHAR(255) UNIQUE NOT NULL,
   password VARCHAR(255) NOT NULL,
   created TIMESTAMP,
   modified TIMESTAMP,
   last_login TIMESTAMP,
   token VARCHAR(255),
   isactive BOOLEAN
);

CREATE TABLE phone (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   number VARCHAR(20),
   citycode VARCHAR(10),
   contrycode VARCHAR(10),
   user_id UUID,
   FOREIGN KEY (user_id) REFERENCES users(id)
);