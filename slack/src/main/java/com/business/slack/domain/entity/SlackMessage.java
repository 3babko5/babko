package com.business.slack.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_slack_message")
@Comment("슬랙 메시지")
public class SlackMessage {

  @Id
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("슬랙 메시지 ID")
  private UUID slackMessageId;

  @NotNull
  @JdbcTypeCode(SqlTypes.UUID)
  @Column(nullable = false)
  @Comment("수신자 Slack ID")
  private UUID notifiedSlackId;

  @NotNull
  @Column(nullable = false)
  @Comment("메시지 내용")
  private String message;

  @NotNull
  @Column(nullable = false)
  @Comment("발송 시간")
  private LocalDateTime sentAt;
}
