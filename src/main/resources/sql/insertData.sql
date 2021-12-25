insert into messenger.roles(description,name)
values
('User','User'),
('Admin','Admin');


insert into messenger.user_credentials
values
('VerstovDanilaaaa','1A1DC91C907325C69271DDF0C944BC72'),
('ValshinAidaaaaaaar','1A1DC91C907325C69271DDF0C944BC72'),
('KarpenkoDimaaaa','1A1DC91C907325C69271DDF0C944BC72'),
('KopekovNazaraaar','1A1DC91C907325C69271DDF0C944BC72'),
('KoromyslovAleksandr','1A1DC91C907325C69271DDF0C944BC72'),
('KuznetsovKiriiiiil','1A1DC91C907325C69271DDF0C944BC72'),
('SofronovIvaaaan','1A1DC91C907325C69271DDF0C944BC72'),
('SaprykinaVasilinaaaa','1A1DC91C907325C69271DDF0C944BC72'),
('AdminAdmiiin','1A1DC91C907325C69271DDF0C944BC72'),
('AdminAdmiiin1','1A1DC91C907325C69271DDF0C944BC72'),
('AdminAdmiiin2','1A1DC91C907325C69271DDF0C944BC72'),
('AdminAdmiiin3','1A1DC91C907325C69271DDF0C944BC72');

insert into messenger.users
values ('89370000001','22/01/2000','Aidar','m',false,false,'ValshinAidaaaaaaar',null,'Valshin'),
	   ('89370000002','21/12/1999','Danila','m',false,false,
	   'VerstovDanilaaaa','https://secure.gravatar.com/avatar/818c212b9f8830dfef491b3f7da99a14?d=identicon&version=1','Verstov'),
	   ('89160000001','22/01/2000','Dmitry','m',false,false,'KarpenkoDimaaaa',null,'Karpenko'),
	   ('89160000002','22/01/2000','Nazar','m',false,false,'KopekovNazaraaar',null,'Kopekov'),
	   ('89160000003','22/01/2000','Alexandr','m',false,false,'KoromyslovAleksandr',null,'Koromyslov'),
	   ('89160000004','22/01/2000','Kiril','m',false,false,'KuznetsovKiriiiiil',null,'Kuznetsov'),
	   ('89160000005','22/01/2000','Ivan','m',false,false,'SofronovIvaaaan',null,'Sofronov'),
	   ('89160000006','22/01/2000','Vasilina','f',false,false,'SaprykinaVasilinaaaa',null,'Saprykina'),

	   ('89169740000','01/01/1998','Admin','m',false,false,'AdminAdmiiin',null,'Admiiin'),
	   ('89169740001','01/01/1998','Admin1','f',false,false,'AdminAdmiiin1',null,'Admiiin1'),
	   ('89169740002','01/01/1998','Admin2','m',false,false,'AdminAdmiiin2',null,'Admiiin2'),
	   ('89169740003','01/01/1998','Admin3','f',false,false,'AdminAdmiiin3',null,'Admiiin3');

insert into messenger.roles_users_mapping
values
(2,'89370000001'),
(2,'89370000002'),

(1,'89160000001'),
(1,'89160000002'),
(1,'89160000003'),
(1,'89160000004'),
(1,'89160000005'),
(1,'89160000006'),

(2,'89169740000'),
(2,'89169740001'),
(2,'89169740002'),
(2,'89169740003');

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