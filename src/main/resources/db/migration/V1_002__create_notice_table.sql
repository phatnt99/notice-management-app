create table notice (
        id uuid not null,
        created_by varchar(255),
        created_date timestamp(6),
        is_deleted boolean default false,
        modified_by varchar(255),
        modified_date timestamp(6),
        content varchar(255) not null,
        end_time timestamp(6),
        start_time timestamp(6),
        title varchar(255) not null,
        user_id uuid,
        primary key (id)
    );

alter table if exists notice
       add constraint FK36fvm3kpxgl57hv5v039c4fum
       foreign key (user_id)
       references _user;