DO $$
BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'userrole') THEN
create type userrole  AS ENUM ('ADMIN', 'STUDENT', 'TEACHER');
END IF;
END
$$;