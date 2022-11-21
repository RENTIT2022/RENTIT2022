insert into role_permission (role_id, permission_id) values (1, 2);
insert into role_permission (role_id, permission_id) values (2, 1);
insert into role_permission (role_id, permission_id) values (2, 2);
insert into role_permission (role_id, permission_id) values (3, 1);
insert into role_permission (role_id, permission_id) values (3, 2);
insert into role_permission (role_id, permission_id) values (3, 3);

-- Admin
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('user_seq');
select nextval ('passport_data_seq');
select nextval ('registered_address_seq');
select nextval ('residence_address_seq');

insert into "image" (public_id, url, id) values (' ', ' ', 1);
insert into "image_user" (image_id, order_number, id) values (1, 1, 1);
insert into "image" (public_id, url, id) values (' ', ' ', 2);
insert into "image_user" (image_id, order_number, id) values (2, 2, 2);
insert into "image" (public_id, url, id) values (' ', ' ', 3);
insert into "image_user" (image_id, order_number, id) values (3, 3, 3);
insert into "image" (public_id, url, id) values (' ', ' ', 4);
insert into "image_user" (image_id, order_number, id) values (4, 4, 4);

insert into "passport_data" (authority, date_of_issue, tin, id) values (' ', null, ' ', 1);
insert into "registered_address" (apartment_number, city_or_village, district, house_number, region, street, id) values (0, ' ', ' ', 0, ' ', ' ', 1);
insert into "residence_address" (apartment_number, city_or_village, district, house_number, region, street, id) values (0, ' ', ' ', 0, ' ', ' ', 1);
insert into "user" (code_expiration_date, date_of_birth, email, first_name, is_registration_complete, is_verified_by_tech_support, last_name, middle_name, passport_data_id, password, phone_number, provider, provider_id, registered_address_id, reset_password_code, residence_address_id, role_id, status, id)
values (null, null, 'rentit.kg@gmail.com', 'RENTIT', true, true, ' ', ' ', 1, '$2a$10$bEQJAukLr38Kkv2eFCcfh.qWFW2iF7nKaN4xoT039bM6ZSdFxDeBC', ' ', 'local', ' ', 1, null, 1, 3, 'ACTIVE', 1);

update "image_user" set image_user_id=1 where id=1;
update "image_user" set image_user_id=1 where id=2;
update "image_user" set image_user_id=1 where id=3;
update "image_user" set image_user_id=1 where id=4;

-- Tech Support
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('user_seq');
select nextval ('passport_data_seq');
select nextval ('registered_address_seq');
select nextval ('residence_address_seq');

insert into "image" (public_id, url, id) values (' ', ' ', 5);
insert into "image_user" (image_id, order_number, id) values (5, 1, 5);
insert into "image" (public_id, url, id) values (' ', ' ', 6);
insert into "image_user" (image_id, order_number, id) values (6, 2, 6);
insert into "image" (public_id, url, id) values (' ', ' ', 7);
insert into "image_user" (image_id, order_number, id) values (7, 3, 7);
insert into "image" (public_id, url, id) values (' ', ' ', 8);
insert into "image_user" (image_id, order_number, id) values (8, 4, 8);

insert into "passport_data" (authority, date_of_issue, tin, id) values (' ', null, ' ', 2);
insert into "registered_address" (apartment_number, city_or_village, district, house_number, region, street, id) values (0, ' ', ' ', 0, ' ', ' ', 2);
insert into "residence_address" (apartment_number, city_or_village, district, house_number, region, street, id) values (0, ' ', ' ', 0, ' ', ' ', 2);
insert into "user" (code_expiration_date, date_of_birth, email, first_name, is_registration_complete, is_verified_by_tech_support, last_name, middle_name, passport_data_id, password, phone_number, provider, provider_id, registered_address_id, reset_password_code, residence_address_id, role_id, status, id)
values (null, null, 'rentit1.neo@gmail.com', 'Timur', true, true, 'Pratov', ' ', 2, '$2a$10$bEQJAukLr38Kkv2eFCcfh.qWFW2iF7nKaN4xoT039bM6ZSdFxDeBC', ' ', 'local', ' ', 2, null, 2, 2, 'ACTIVE', 2);

update "image_user" set image_user_id=2 where id=5;
update "image_user" set image_user_id=2 where id=6;
update "image_user" set image_user_id=2 where id=7;
update "image_user" set image_user_id=2 where id=8;

-- Tilek
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('user_seq');
select nextval ('passport_data_seq');
select nextval ('registered_address_seq');
select nextval ('residence_address_seq');

insert into "image" (public_id, url, id) values (' ', ' ', 9);
insert into "image_user" (image_id, order_number, id) values (9, 1, 9);
insert into "image" (public_id, url, id) values (' ', ' ', 10);
insert into "image_user" (image_id, order_number, id) values (10, 2, 10);
insert into "image" (public_id, url, id) values (' ', ' ', 11);
insert into "image_user" (image_id, order_number, id) values (11, 3, 11);
insert into "image" (public_id, url, id) values (' ', ' ', 12);
insert into "image_user" (image_id, order_number, id) values (12, 4, 12);

insert into "passport_data" (authority, date_of_issue, tin, id) values (' ', null, ' ', 3);
insert into "registered_address" (apartment_number, city_or_village, district, house_number, region, street, id) values (0, ' ', ' ', 0, ' ', ' ', 3);
insert into "residence_address" (apartment_number, city_or_village, district, house_number, region, street, id) values (0, ' ', ' ', 0, ' ', ' ', 3);
insert into "user" (code_expiration_date, date_of_birth, email, first_name, is_registration_complete, is_verified_by_tech_support, last_name, middle_name, passport_data_id, password, phone_number, provider, provider_id, registered_address_id, reset_password_code, residence_address_id, role_id, status, id)
values (null, null, 'tilekju@gmail.com', 'Tika', false, false, 'A', ' ', 3, '$2a$12$KvEg83ia5Q9ALBZ5TzfU2uBgnWyjpQJOSY2Xe0ImV2dy63CTEwSN.', ' ', 'local', ' ', 3, null, 3, 1, 'ACTIVE', 3);

update "image_user" set image_user_id=3 where id=9;
update "image_user" set image_user_id=3 where id=10;
update "image_user" set image_user_id=3 where id=11;
update "image_user" set image_user_id=3 where id=12;

-- Aikanysh
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('user_seq');
select nextval ('passport_data_seq');
select nextval ('registered_address_seq');
select nextval ('residence_address_seq');

insert into "image" (public_id, url, id) values (' ', ' ', 13);
insert into "image_user" (image_id, order_number, id) values (13, 1, 13);
insert into "image" (public_id, url, id) values (' ', ' ', 14);
insert into "image_user" (image_id, order_number, id) values (14, 2, 14);
insert into "image" (public_id, url, id) values (' ', ' ', 15);
insert into "image_user" (image_id, order_number, id) values (15, 3, 15);
insert into "image" (public_id, url, id) values (' ', ' ', 16);
insert into "image_user" (image_id, order_number, id) values (16, 4, 16);

insert into "passport_data" (authority, date_of_issue, tin, id) values (' ', null, ' ', 4);
insert into "registered_address" (apartment_number, city_or_village, district, house_number, region, street, id) values (0, ' ', ' ', 0, ' ', ' ', 4);
insert into "residence_address" (apartment_number, city_or_village, district, house_number, region, street, id) values (0, ' ', ' ', 0, ' ', ' ', 4);
insert into "user" (code_expiration_date, date_of_birth, email, first_name, is_registration_complete, is_verified_by_tech_support, last_name, middle_name, passport_data_id, password, phone_number, provider, provider_id, registered_address_id, reset_password_code, residence_address_id, role_id, status, id)
values (null, null, 'saikanysh@gmail.com', 'Айканыш', false, false, 'A', ' ', 4, '$2a$10$bEQJAukLr38Kkv2eFCcfh.qWFW2iF7nKaN4xoT039bM6ZSdFxDeBC', ' ', 'local', ' ', 4, null, 4, 1, 'ACTIVE', 4);

update "image_user" set image_user_id=4 where id=13;
update "image_user" set image_user_id=4 where id=14;
update "image_user" set image_user_id=4 where id=15;
update "image_user" set image_user_id=4 where id=16;


-- Azamat
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('image_seq');
select nextval ('image_user_seq');
select nextval ('user_seq');
select nextval ('passport_data_seq');
select nextval ('registered_address_seq');
select nextval ('residence_address_seq');

insert into "image" (public_id, url, id) values (' ', ' ', 17);
insert into "image_user" (image_id, order_number, id) values (17, 1, 17);
insert into "image" (public_id, url, id) values (' ', ' ', 18);
insert into "image_user" (image_id, order_number, id) values (18, 2, 18);
insert into "image" (public_id, url, id) values (' ', ' ', 19);
insert into "image_user" (image_id, order_number, id) values (19, 3, 19);
insert into "image" (public_id, url, id) values (' ', ' ', 20);
insert into "image_user" (image_id, order_number, id) values (20, 4, 20);

insert into "passport_data" (authority, date_of_issue, tin, id) values (' ', null, ' ', 5);
insert into "registered_address" (apartment_number, city_or_village, district, house_number, region, street, id) values (0, ' ', ' ', 0, ' ', ' ', 5);
insert into "residence_address" (apartment_number, city_or_village, district, house_number, region, street, id) values (0, ' ', ' ', 0, ' ', ' ', 5);
insert into "user" (code_expiration_date, date_of_birth, email, first_name, is_registration_complete, is_verified_by_tech_support, last_name, middle_name, passport_data_id, password, phone_number, provider, provider_id, registered_address_id, reset_password_code, residence_address_id, role_id, status, id)
values (null, null, 'satybaldievazamat08@gmail.com', 'Азамат', false, false, 'Сатыбалдиев', ' ', 5, '$2a$10$bEQJAukLr38Kkv2eFCcfh.qWFW2iF7nKaN4xoT039bM6ZSdFxDeBC', ' ', 'local', ' ', 5, null, 5, 1, 'ACTIVE', 5);

update "image_user" set image_user_id=5 where id=17;
update "image_user" set image_user_id=5 where id=18;
update "image_user" set image_user_id=5 where id=19;
update "image_user" set image_user_id=5 where id=20;

