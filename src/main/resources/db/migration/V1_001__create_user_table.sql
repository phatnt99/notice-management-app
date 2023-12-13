create table _user (
        id uuid not null,
        created_by varchar(255),
        created_date timestamp(6),
        is_deleted boolean default false,
        modified_by varchar(255),
        modified_date timestamp(6),
        password varchar(255),
        username varchar(50),
        primary key (id)
    )