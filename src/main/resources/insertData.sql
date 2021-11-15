insert into messenger.roles(description,name)
values
('User','User'),
('Admin','Admin');

insert into messenger.user_credentials
values
('VerstovDanilaaaa','Password','Token'),
('ValshinAidaaaaaaar','Password','Token'),
('AdminAdmiiin','Password','Token');

insert into messenger.users
values('89169943834','21/12/1999','Danila','m',false,false,
	   'VerstovDanilaaaa','https://secure.gravatar.com/avatar/818c212b9f8830dfef491b3f7da99a14?d=identicon&version=1','Verstov'),
	   ('89169743834','22/01/2000','Aidar','m',false,false,'ValshinAidaaaaaaar',null,'Valshin'),
	   ('89169740000','01/01/1998','Admin','f',false,false,'AdminAdmiiin',null,'Admiiin');

insert into messenger.roles_users_mapping
values
(1,'89169943834'),
(2,'89169743834'),
(2,'89169740000');