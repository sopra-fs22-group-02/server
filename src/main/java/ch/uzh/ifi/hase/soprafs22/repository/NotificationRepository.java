package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("notificationRepository")
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByNotificationId(int notificationId);
}
