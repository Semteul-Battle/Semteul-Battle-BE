ALTER TABLE users
    ADD CONSTRAINT uk_users_login_id UNIQUE (login_id);

ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);
