create table messenger.device (
    registration_id varchar(4096) not null,
    primary key (registration_id)
);

create table messenger.users_devices (
    telephone_number varchar(20) not null,
    registration_id varchar(4096) not null,
    primary key (telephone_number, registration_id)
);

alter table if exists messenger.users_devices
    add constraint FKfgdu09gdvy10w3cv5rpkp684x
        foreign key (registration_id)
            references messenger.device;

alter table if exists messenger.users_devices
    add constraint FK9w9je0qgwfkp7nhfjgl3erfw5
        foreign key (telephone_number)
            references messenger.users;
