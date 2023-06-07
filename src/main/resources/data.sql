INSERT INTO users (username,password,enabled) VALUES (
                                                         'admin', '{bcrypt}$2a$10$AQxo2/IzK0oBg6VhSxJbeuB2H7Bt4LBRrJUtY2lcaKFRPpcCsCrJi', 1
                                                     );
INSERT INTO authorities (username,authority) VALUES ( 'admin', 'ROLE_ADMIN' );