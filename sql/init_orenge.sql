USE gongda_circle;

INSERT INTO user (id, username, password, nickname, status, create_time)
VALUES (1, 'orenge', '$2a$10$...[BCrypt加密后的jinyancheng]', 'Orange', 1, NOW());