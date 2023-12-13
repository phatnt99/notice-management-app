package com.phatnt15.noticemanagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import java.util.UUID;

/**
 * The type Attachment.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FilterDef(name = "deletedAttachmentsFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedAttachmentsFilter", condition = "is_deleted = :isDeleted")
@SQLDelete(sql = "UPDATE attachment SET is_deleted = true WHERE id=?")
public class Attachment extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String fileName;

    @Column
    private String filePath;

    @Column
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
