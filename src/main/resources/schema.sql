DROP TABLE IF EXISTS "sessions";
DROP TABLE IF EXISTS "users";


CREATE TABLE users(
id BIGSERIAL PRIMARY KEY,
username VARCHAR(20) UNIQUE NOT NULL,
email VARCHAR(255) UNIQUE,
belt_rank VARCHAR(7) DEFAULT 'N/A',
stripe_count INTEGER DEFAULT 0,
join_date DATE DEFAULT NOW(),
password_hash VARCHAR(255) NOT NULL,
CHECK (stripe_count <= 4 AND stripe_count >= 0)
);

CREATE TABLE sessions(
id BIGSERIAL PRIMARY KEY,
user_id BIGINT NOT NULL,
class_date DATE NOT NULL,
class_type VARCHAR(5),
duration INTEGER,
notes VARCHAR(4000),
CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
