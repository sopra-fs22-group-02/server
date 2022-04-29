package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Notification;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.NotificationRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NotificationService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationService(@Qualifier("notificationRepository") NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public Notification createNotification(int userId, Notification newNotification) {

        // find the receiver of the message
        User receiver = userRepository.findByUserId(userId);
        newNotification.setCreationDate(LocalDateTime.now());
        newNotification.setReceiverId(userId);

        newNotification = notificationRepository.save(newNotification);
        notificationRepository.flush();

        // add notification to the receiver's list of notifications
        receiver.addNotifications(newNotification);

        return newNotification;
    }

    public List<Notification> getAllNotificationsForUser(int userId){
        User user = userRepository.findByUserId(userId);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "This user does not exist!");
        }

        return user.getMyNotifications();
    }

    /** delete old messages*/

    @Scheduled(fixedDelay = 5000)
    public void checkIfOlderThan24h() {
        // fetch all notifications
        List<Notification> allNotifications = notificationRepository.findAll();

        // abort if there are no notifications
        if(allNotifications.isEmpty()){return;}

        List<Notification> toBeDeleted = new ArrayList<>();

        // go through all the notifications
        for(Notification notification : allNotifications){
            // check how old the notification
            long timeDifference = notification.getCreationDate().until(LocalDateTime.now(), ChronoUnit.MINUTES);
            // delete it if > 24h
            if(timeDifference > 1440L){
                toBeDeleted.add(notification);
            }
        }

        for(Notification notification : toBeDeleted){
            deleteExpiredNotification(notification);
        }
    }

    private void deleteExpiredNotification(Notification notification){
        // find receiver
        User receiver = userRepository.findByUserId(notification.getReceiverId());
        // fetch all notifications within the receiver
        List<Notification> notificationsReceiver = receiver.getMyNotifications();

        // remove the notification from the receiver's notifications list
        notificationsReceiver.removeIf(n -> n.getNotificationId() == notification.getNotificationId());
        // remove the notification from the notification repository
        notificationRepository.delete(notification);
    }

}


