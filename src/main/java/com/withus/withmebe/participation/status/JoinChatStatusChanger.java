package com.withus.withmebe.participation.status;

import static com.withus.withmebe.participation.type.Status.APPROVED;
import static com.withus.withmebe.participation.type.Status.CHAT_JOINED;
import static com.withus.withmebe.participation.type.Status.CHAT_LEFT;

import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class JoinChatStatusChanger extends ParticipationStatusChanger{
  private static Status newStatus = CHAT_JOINED;
  private static List<Status> availableStatus = List.of(APPROVED, CHAT_LEFT);
  public JoinChatStatusChanger(
      Participation participation, Long currentMemberId) {
    super(participation, currentMemberId, availableStatus, newStatus);
  }

  @Override
  boolean isAvailableTime() {
    return participation.getGathering().getRecruitmentStartDt().isBefore(LocalDate.now())
        && participation.getGathering().getGatheringDateTime().isAfter(LocalDateTime.now());
  }

  @Override
  boolean isAvailableUser() {
    return isParticipant();
  }
}
