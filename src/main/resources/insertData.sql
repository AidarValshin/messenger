insert into messenger.roles(description,name)
values
('User','User'),
('Admin','Admin');


insert into messenger.user_credentials
values
('VerstovDanilaaaa','Password1','Token1'),
('ValshinAidaaaaaaar','Password2','Token2'),
('KarpenkoDimaaaa','Password3','Token3'),
('KopekovNazaraaar','Password4','Token4'),
('KoromyslovAleksandr','Password5','Token5'),
('KuznetsovKiriiiiil','Password6','Token6'),
('SofronovIvaaaan','Password7','Token7'),
('SaprykinaVasilinaaaa','Password8','Token8'),
('AdminAdmiiin','Password9','Token9'),
('AdminAdmiiin1','Password10','Token10'),
('AdminAdmiiin2','Password11','Token11'),
('AdminAdmiiin3','Password12','Token12');

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
