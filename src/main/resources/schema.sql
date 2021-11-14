DROP SCHEMA IF EXISTS messenger CASCADE  ;
CREATE SCHEMA IF NOT EXISTS messenger;

    create table messenger.chat_contacts (
       id_chat_contact bigserial,
        id_chat int8 not null,
        id_message int8,
        telephone_number varchar(20) not null,
        primary key (id_chat_contact)
    )
;
CREATE INDEX id_chat_contacts_idx
ON messenger.chat_contacts(id_chat_contact);

    create table messenger.chats (
       id_chat bigserial ,
        last_message_date timestamp,
        last_message_id int8,
        photo_url varchar(80),
        primary key (id_chat)
    )
;
    CREATE INDEX id_chats_idx
ON messenger.chats( id_chat);

    create table messenger.messages (
       id_message bigserial ,
        is_deleted boolean,
        date timestamp not null,
        id_chat int8 not null,
        last_changes_date timestamp not null,
        telephone_number varchar(20) not null,
        text varchar(1000) not null,
        primary key (id_message)
    )
;
    CREATE INDEX id_messages_idx
ON messenger.messages( id_message);

    CREATE INDEX date_messages_idx
ON messenger.messages( date);




    create table messenger.roles  (
       id_role serial ,
        description varchar(500) not null,
        name varchar(50) not null,
        primary key (id_role)
    )
;

    create table messenger.roles_users_mapping (
       id_role int8 not null,
        telephone_number varchar(20) not null,
        primary key (id_role, telephone_number)
    )
;
CREATE INDEX role_roles_users_mapping_idx
ON messenger.roles_users_mapping( id_role);

CREATE INDEX user_roles_users_mapping_idx
ON messenger.roles_users_mapping( telephone_number);

    create table messenger.user_credentials (
       login varchar(20) not null,
        password varchar(50) not null,
        token varchar(500) not null,
        primary key (login)
    )
;
CREATE INDEX login_user_credentials_idx
ON messenger.user_credentials(login);

    create table messenger.users (
       telephone_number varchar(20) not null,
        date_of_birth date not null,
        first_name varchar(20) not null,
        gender varchar(1) not null,
        id_photo int8 not null,
        is_deleted boolean not null,
        is_locked boolean not null,
        login varchar(20) not null,
        photo_url varchar(80) ,
        second_name varchar(20) not null,
        primary key (telephone_number)
    );
	CREATE INDEX login_users_idx
ON messenger.users(login);

	CREATE INDEX telephone_number_users_idx
ON messenger.users(telephone_number);

	CREATE INDEX first_name_users_idx
ON messenger.users(first_name);

	CREATE INDEX second_name_users_idx
ON messenger.users(second_name);



    alter table if exists messenger.chat_contacts
       add constraint FK6am5genhsdtkc85yy5me0vpcl
       foreign key (id_chat)
       references messenger.chats
;

    alter table if exists messenger.chat_contacts
       add constraint FKiv3eg0vo25u0ornhay7oieb0v
       foreign key (id_message)
       references messenger.messages
;

    alter table if exists messenger.chat_contacts
       add constraint FKnscr3ivtdmifx26ua9wu543s5
       foreign key (telephone_number)
       references messenger.users
;

    alter table if exists messenger.chats
       add constraint FKnsbqqie3vo05v9j6au5m62n1q
       foreign key (last_message_id)
       references messenger.messages (id_message)
;

    alter table if exists messenger.messages
       add constraint FKcuttjwmjon1n2l9x3dhd4kcd7
       foreign key (telephone_number)
       references messenger.users
;

    alter table if exists messenger.roles_users_mapping
       add constraint FKlg3rtaqj3cjnk78ydvijl6knx
       foreign key (id_role)
       references messenger.roles
;

    alter table if exists messenger.roles_users_mapping
       add constraint FKoxwtifjp5v8dklgchav5vy8yy
       foreign key (telephone_number)
       references messenger.users
;

    alter table if exists messenger.users
       add constraint FK2oq7g3lh6kjiv3qsoec3oli0m
       foreign key (login)
       references messenger.user_credentials ;
