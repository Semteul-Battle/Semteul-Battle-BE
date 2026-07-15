DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM users
        GROUP BY login_id
        HAVING COUNT(*) > 1
    ) THEN
        RAISE EXCEPTION 'Duplicate users.login_id values exist. Resolve duplicate login_id data before applying unique constraint.';
    END IF;

    IF EXISTS (
        SELECT 1
        FROM users
        GROUP BY email
        HAVING COUNT(*) > 1
    ) THEN
        RAISE EXCEPTION 'Duplicate users.email values exist. Resolve duplicate email data before applying unique constraint.';
    END IF;
END $$;

ALTER TABLE users
    ADD CONSTRAINT uk_users_login_id UNIQUE (login_id);

ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);
