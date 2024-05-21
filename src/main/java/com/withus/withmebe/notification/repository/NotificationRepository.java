package com.withus.withmebe.notification.repository;

import com.withus.withmebe.notification.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  List<Notification> findAllByReceiverAndReadDttmIsNull(Long receiver);

}
