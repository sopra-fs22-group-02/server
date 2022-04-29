package ch.uzh.ifi.hase.soprafs22.rest.dto;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
<<<<<<< HEAD
import ch.uzh.ifi.hase.soprafs22.entity.Notification;

=======
import ch.uzh.ifi.hase.soprafs22.entity.Calendar;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;

import javax.persistence.OneToMany;
>>>>>>> origin/calendar_stuff
import java.util.List;

public class UserGetDTO {

    private int userId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private UserStatus status;
    private String token;
    private String bio;
    private String profilePicture;
<<<<<<< HEAD
    private List<Notification> myNotifications;
=======
    private List<SleepEvent> myCalendarAsApplicant;
    private List<SleepEvent> myCalendarAsProvider;
>>>>>>> origin/calendar_stuff

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

<<<<<<< HEAD
    public List<Notification> getMyNotifications(){
        return myNotifications;
    }

    public void setMyNotifications(List<Notification>  myNotifications){
        this.myNotifications = myNotifications;
=======
    public List<SleepEvent> getMyCalendarAsApplicant() {
        return myCalendarAsApplicant;
    }

    public void setMyCalendarAsApplicant(List<SleepEvent> myCalendarAsApplicant) {
        this.myCalendarAsApplicant = myCalendarAsApplicant;
    }

    public List<SleepEvent> getMyCalendarAsProvider() {
        return myCalendarAsProvider;
    }

    public void setMyCalendarAsProvider(List<SleepEvent> myCalendarAsProvider) {
        this.myCalendarAsProvider = myCalendarAsProvider;
>>>>>>> origin/calendar_stuff
    }
}
