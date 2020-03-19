-- init tables

CREATE TABLE users (
	id VARCHAR(256) NOT NULL PRIMARY KEY,
    username VARCHAR(256) NOT NULL,
    password VARCHAR(256) NOT NULL,
    name VARCHAR(256) NOT NULL,
    surname VARCHAR(256) NOT NULL,
    is_account_non_expired INT,
    is_account_non_locked INT,
    is_credentials_non_expired INT,
    is_enabled INT,
    email VARCHAR(256)
);

CREATE TABLE permissions(
	id VARCHAR(256) NOT NULL PRIMARY KEY,
    permission VARCHAR(256) NOT NULL
);

CREATE TABLE roles(
	id VARCHAR(256) NOT NULL PRIMARY KEY,
    role_name VARCHAR(256) NOT NULL
);

CREATE TABLE user_roles(
    user_id VARCHAR(255) NOT NULL,
    role_id VARCHAR(255) NOT NULL,
    PRIMARY KEY(user_id, role_id)
);

CREATE TABLE role_permissions(
    role_id VARCHAR(255) NOT NULL,
    permission_id VARCHAR(255) NOT NULL,
    PRIMARY KEY(role_id, permission_id)
);


-- add relationships between users/roles

ALTER TABLE user_roles
ADD CONSTRAINT FK_user_role
FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE user_roles
ADD CONSTRAINT FK_role_user
FOREIGN KEY (role_id) REFERENCES roles(id);


-- add relationships between roles/permissions

ALTER TABLE role_permissions
ADD CONSTRAINT FK_role_permission
FOREIGN KEY (role_id) REFERENCES roles(id);

ALTER TABLE role_permissions
ADD CONSTRAINT FK_permission_role
FOREIGN KEY (permission_id) REFERENCES permissions(id);
