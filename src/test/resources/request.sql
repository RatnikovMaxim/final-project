INSERT INTO users(login, password, role)
VALUES ('IvanovA.A', 'secret', 'storekeeper');

INSERT INTO users(login, password, role)
VALUES ('PetrovA.A', 'secret', 'storekeeper');

INSERT INTO users(login, password, role)
VALUES ('admin', 'secret', 'admin');

SELECT id, login
FROM users
ORDER BY id
LIMIT 3 OFFSET 0;

SELECT id, login, EXTRACT(EPOCH FROM created AT TIME ZONE 'Europe/Moscow')
FROM users
ORDER BY id
LIMIT 3 OFFSET 3;

SELECT id, login FROM users
WHERE removed = FALSE
ORDER BY id
LIMIT 3 OFFSET 0;

SELECT id, login FROM users
WHERE id = 2;

UPDATE users SET password = 'top-secret'
WHERE id = 1  AND removed = FALSE
RETURNING id, login, role;

UPDATE users SET removed = FALSE -- restore
WHERE id = 3
RETURNING id, login, role;

UPDATE users SET removed = TRUE -- delete
WHERE id = 3
RETURNING id, login, role;

INSERT INTO spare_parts(owner_id, name, description, quantity, image)
VALUES (1, 'Bearing R2280-2RS', 'radial steel', 5, 'Bearing.png');



SELECT h.id, h.part_id, h.owner_id, sp.name
FROM history h
JOIN spare_parts sp ON h.part_id = sp.id;

SELECT h.id, u.login FROM history h
JOIN users u on h.owner_id = u.id;

SELECT h.id, sp.name, sp.quantity, sp.created, u.login FROM history h
JOIN spare_parts sp ON h.part_id = sp.id
JOIN users u on h.owner_id = u.id
