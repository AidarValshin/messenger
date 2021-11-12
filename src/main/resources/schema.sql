DROP SCHEMA IF EXISTS messenger CASCADE  ;
CREATE SCHEMA IF NOT EXISTS messenger;
    create table messenger.messages (
       id_message bigserial not null,
        last_changes_date timestamp not null,
        get_is_deleted boolean,
        id_chat int8 not null,
        telephone_number varchar(20),
        text varchar(1000) not null,
        primary key (id_message)
    )
;

    create table messenger.roles (
       id_role serial not null,
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

    create table messenger.user_credentials (
       login varchar(20) not null,
        password varchar(50) not null,
        token varchar(500) not null,
        primary key (login)
    )
;
    create table messenger.users (
       telephone_number varchar(20) not null,
        locked boolean not null,
        date_of_birth timestamp not null,
        first_name varchar(20) not null,
        id_photo int8 not null,
        is_deleted boolean not null,
        login varchar(20) not null,
        photo_url varchar(30) not null,
        second_name varchar(20) not null,
        primary key (telephone_number)
    );

    alter table if exists messenger.messages
       add constraint FKcuttjwmjon1n2l9x3dhd4kcd7
       foreign key (telephone_number)
       references messenger.users;

    alter table if exists messenger.roles_users_mapping
       add constraint FKlg3rtaqj3cjnk78ydvijl6knx
       foreign key (id_role)
       references messenger.roles;

    alter table if exists messenger.roles_users_mapping
       add constraint FKoxwtifjp5v8dklgchav5vy8yy
       foreign key (telephone_number)
       references messenger.users;

    alter table if exists messenger.users
       add constraint FK2oq7g3lh6kjiv3qsoec3oli0m
       foreign key (login)
       references messenger.user_credentials;
