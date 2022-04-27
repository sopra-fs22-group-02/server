package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.entity.Notification;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.NotificationRepository;
import ch.uzh.ifi.hase.soprafs22.repository.PlaceRepository;
import ch.uzh.ifi.hase.soprafs22.repository.SleepEventRepository;
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
import java.util.Collections;
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

        // find user to which the new notification belongs
        User correspondingUser = userRepository.findByUserId(userId);

        newNotification = notificationRepository.save(newNotification);
        notificationRepository.flush();

        // add notification to the users list of notifications
        correspondingUser.addNotifications(newNotification);

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

    /** method regarding state of notification*/

    @Scheduled(fixedDelay = 1800000)
    public void checkIfExpiredOrOver(Notification updatedNotification) {
        LocalDateTime creationDate = updatedNotification.getCreationDate();

        long timeDifference = creationDate.until(LocalDateTime.now(), ChronoUnit.HOURS);

        if(timeDifference > 24L){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The sleep event is too long (max 12 hours) and can therefore not be updated!");
        }
    }

}


