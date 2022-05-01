package ch.uzh.ifi.hase.soprafs22.controller;


import ch.uzh.ifi.hase.soprafs22.entity.Notification;
import ch.uzh.ifi.hase.soprafs22.rest.dto.NotificationGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.NotificationPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapperNotification;
import ch.uzh.ifi.hase.soprafs22.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    NotificationController(NotificationService notificationService){
        this.notificationService = notificationService;
    }

    /** POST endpoints */

    // create a notification and putting it into the list of the users notifications
    @PostMapping("/users/{userId}/notifications")
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationGetDTO createNotification(@PathVariable int userId, @RequestBody NotificationPostDTO notificationPostDTO){
        Notification newNotification = DTOMapperNotification.INSTANCE.convertNotificationPostDTOtoEntity(notificationPostDTO);

        Notification createdNotification = notificationService.createNotification(userId, newNotification);

        return DTOMapperNotification.INSTANCE.convertEntityToNotificationGetDTO(createdNotification);
    }


    /** GET endpoints */

    // fetch the users list of notifications
    @GetMapping("/users/{userId}/notifications")
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationGetDTO> getListOfNotification(@PathVariable int userId){
        // fetch all notifications for this user in the internal representation
        List<Notification> notifications = notificationService.getAllNotificationsForUser(userId);
        List<NotificationGetDTO> notificationGetDTOs = new ArrayList<>();

        // convert each notification to the API representation
        for (Notification notification : notifications) {
            notificationGetDTOs.add(DTOMapperNotification.INSTANCE.convertEntityToNotificationGetDTO(notification));
        }
        return notificationGetDTOs;
    }

}
