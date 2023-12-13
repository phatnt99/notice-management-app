create table notice_view (
        id uuid not null,
        created_by varchar(255),
        created_date timestamp(6),
        is_deleted boolean default false,
        modified_by varchar(255),
        modified_date timestamp(6),
        notice_id uuid,
        viewer_id uuid,
        primary key (id)
    );

alter table if exists notice_view
       add constraint FKevabuj4q7i1oc3d9s3jn7c33o
       foreign key (notice_id)
       references notice;

alter table if exists notice_view
       add constraint FKoio82anetwv4qxbfdbvoadg22
       foreign key (viewer_id)
       references _user;