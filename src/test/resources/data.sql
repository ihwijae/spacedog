insert into member (name, email, nick_name, role)
values ('testUser', 'test@email.com', 'testNickName', 'ROLE_USER');

INSERT INTO category (category_id, category_name, depth, parent_id) VALUES (1, '사료', 0, null);
INSERT INTO category (category_id, category_name, depth, parent_id) VALUES (2, '간식', 0, null);
INSERT INTO category (category_id, category_name, depth, parent_id) VALUES (3, '용품', 0, null);
INSERT INTO category (category_id, category_name, depth, parent_id) VALUES (4, '습식사료', 1, 1);
INSERT INTO category (category_id, category_name, depth, parent_id) VALUES (5, '에어/동결사료', 1, 1);
INSERT INTO category (category_id, category_name, depth, parent_id) VALUES (6, '건식사료', 1, 1);
INSERT INTO category (category_id, category_name, depth, parent_id) VALUES (8, '장/소화', 2, 6);
INSERT INTO category (category_id, category_name, depth, parent_id) VALUES (9, '의류/악세사리', 1, 3);



