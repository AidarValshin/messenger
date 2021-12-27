insert into messenger.roles(description,name)
values
('User','User'),
('Admin','Admin');


insert into messenger.user_credentials
values
('89370000001','1A1DC91C907325C69271DDF0C944BC72'),
('89370000002','1A1DC91C907325C69271DDF0C944BC72'),
('89370000003','1A1DC91C907325C69271DDF0C944BC72'),
('89370000004','1A1DC91C907325C69271DDF0C944BC72'),
('89370000005','1A1DC91C907325C69271DDF0C944BC72'),
('89370000006','1A1DC91C907325C69271DDF0C944BC72'),
('89370000007','1A1DC91C907325C69271DDF0C944BC72'),
('89370000008','1A1DC91C907325C69271DDF0C944BC72'),
('89370000009','1A1DC91C907325C69271DDF0C944BC72'),
('89370000010','1A1DC91C907325C69271DDF0C944BC72'),
('89370000011','1A1DC91C907325C69271DDF0C944BC72'),
('89370000012','1A1DC91C907325C69271DDF0C944BC72');

insert into messenger.users
values ('89370000001','22/01/2000','Aidar','m',false,false,'ValshinAidaaaaaaar',null,'Valshin'),
	   ('89370000002','21/12/1999','Danila','m',false,false,
	   'VerstovDanilaaaa','https://secure.gravatar.com/avatar/818c212b9f8830dfef491b3f7da99a14?d=identicon&version=1','Verstov'),
	   ('89370000003','22/01/2000','Dmitry','m',false,false,'KarpenkoDimaaaa',null,'Karpenko'),
	   ('89370000004','22/01/2000','Nazar','m',false,false,'KopekovNazaraaar',null,'Kopekov'),
	   ('89370000005','22/01/2000','Alexandr','m',false,false,'KoromyslovAleksandr',null,'Koromyslov'),
	   ('89370000006','22/01/2000','Kiril','m',false,false,'KuznetsovKiriiiiil',null,'Kuznetsov'),
	   ('89370000007','22/01/2000','Ivan','m',false,false,'SofronovIvaaaan',null,'Sofronov'),
	   ('89370000008','22/01/2000','Vasilina','f',false,false,'SaprykinaVasilinaaaa',null,'Saprykina'),
	   ('89370000009','01/01/1998','Admin','m',false,false,'AdminAdmiiin',null,'Admiiin'),
	   ('89370000010','01/01/1998','Admin1','f',false,false,'AdminAdmiiin1',null,'Admiiin1'),
	   ('89370000011','01/01/1998','Admin2','m',false,false,'AdminAdmiiin2',null,'Admiiin2'),
	   ('89370000012','01/01/1998','Admin3','f',false,false,'AdminAdmiiin3',null,'Admiiin3');

insert into messenger.roles_users_mapping
values
(2,'89370000001'),
(2,'89370000002'),

(1,'89370000001'),
(1,'89370000002'),
(1,'89370000003'),
(1,'89370000004'),
(1,'89370000005'),
(1,'89370000006'),

(2,'89370000007'),
(2,'89370000008'),
(2,'89370000009'),
(2,'89370000010'),
(2,'89370000011'),
(2,'89370000012');


insert into messenger.chats(name)
values
('1_stream'),
('2_stream'),
('3_stream'),
('4_stream'),
('5_stream'),
('6_stream'),
('7_stream'),
('8_stream'),
('9_stream');

insert into messenger.chat_contacts(id_chat,telephone_number)
values
(1,'89370000001'),
(1,'89370000002'),
(2,'89370000001'),
(3,'89370000002');