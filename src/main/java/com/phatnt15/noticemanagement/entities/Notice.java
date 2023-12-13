package com.phatnt15.noticemanagement.entities;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The type Notice.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FilterDef(name = "deletedNoticesFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedNoticesFilter", condition = "is_deleted = :isDeleted")
@SQLDelete(sql = "UPDATE notice SET is_deleted = true WHERE id=?")
public class Notice extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "notice", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Attachment> attachments;

    @OneToMany(mappedBy = "notice", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<NoticeView> noticeView;

    @Transient
    private String numberOfViewsUrl;
}
