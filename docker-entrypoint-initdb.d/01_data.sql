INSERT INTO users(login, password, role)
VALUES ('IvanovA.A', '$2a$10$hTjQR2cJMe6JCawD1dY27.p2goeY4vW.R8peaMk0ZQniB1f17566W', 'STOREKEEPER');

INSERT INTO users(login, password, role)
VALUES ('PetrovA.A', '$2a$10$hTjQR2cJMe6JCawD1dY27.p2goeY4vW.R8peaMk0ZQniB1f17566W', 'STOREKEEPER');

INSERT INTO users(login, password, role)
VALUES ('admin', '$2a$10$hTjQR2cJMe6JCawD1dY27.p2goeY4vW.R8peaMk0ZQniB1f17566W', 'ADMIN');

INSERT INTO spare_parts(owner_id, name, description, quantity, image)
VALUES (1, 'Bearing R2280-2RS', 'radial steel', 5, 'Bearing.png');

INSERT INTO spare_parts(owner_id, name, description, quantity, image)
VALUES (2, 'Compression spring STAMO', 'stainless steel', 10, 'Compression_spring.png');

INSERT INTO spare_parts(owner_id, name, description, quantity, image)
VALUES (3, 'Connecting coupling TECHNIX', 'aluminum', 15, 'Connecting_coupling.png');

INSERT INTO spare_parts(owner_id, name, description, quantity, image)
VALUES (3, 'Sealant silicone Krass', 'colorless 300ml', 20, 'Sealant_silicone.png');

INSERT INTO history(owner_id, part_id, type, part_name, part_qty)
VALUES (1, 1, 'NEW', 'Bearing R2280-2RS', 5),
       (2, 2, 'NEW', 'Compression spring STAMO', 10),
       (3, 3, 'NEW', 'Connecting coupling TECHNIX', 15),
       (3, 4, 'NEW', 'Sealant silicone Krass', 20);