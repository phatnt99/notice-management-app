create table attachment (
        id uuid not null,
        created_by varchar(255),
        created_date timestamp(6),
        is_deleted boolean default false,
        modified_by varchar(255),
        modified_date timestamp(6),
        content_type varchar(255),
        file_name varchar(255),
        file_path varchar(255),
        notice_id uuid,
        user_id uuid,
        primary key (id)
    );

alter table if exists attachment
       add constraint FK9kimkt5aliowdkvbw25000h1y
       foreign key (notice_id)
       references notice;

alter table if exists attachment
       add constraint FKq5rcfgddkfujx2pne0cf7wjll
       foreign key (user_id)
       references _user;