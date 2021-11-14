insert into messenger.roles(description,name)
values('Test','Test');

insert into messenger.user_credentials
values('Test_c17-501','Password','Token');

insert into messenger.users
values('+79376674005','22/01/2000','Test','m',null,false,false,'Test_c17-501',null,'Testing');

insert into messenger.roles_users_mapping
values(1,'+79376674005');