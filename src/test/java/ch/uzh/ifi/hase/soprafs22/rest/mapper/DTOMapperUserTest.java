package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperUserTest {
  @Test
  public void testCreateUser_fromUserPostDTO_toUser_success() {
    // create UserPostDTO
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setEmail("name");
    userPostDTO.setUsername("username");
    userPostDTO.setPassword("1234");

    // MAP -> Create user
    User user = DTOMapperUser.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // check content
    assertEquals(userPostDTO.getEmail(), user.getEmail());
    assertEquals(userPostDTO.getUsername(), user.getUsername());
    assertEquals(userPostDTO.getPassword(), user.getPassword());
  }

  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    // create User
    User user = new User();
    user.setEmail("hello@uzh.ch");
    user.setFirstName("Firstname");
    user.setLastName("Lastname");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setBio("hi, I am a person.");
    user.setProfilePicture("some link");

    // MAP -> Create UserGetDTO
    UserGetDTO userGetDTO = DTOMapperUser.INSTANCE.convertEntityToUserGetDTO(user);

    // check content
    assertEquals(user.getUserId(), userGetDTO.getUserId());
    assertEquals(user.getEmail(), userGetDTO.getEmail());
    assertEquals(user.getFirstName(), userGetDTO.getFirstName());
    assertEquals(user.getLastName(), userGetDTO.getLastName());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
    assertEquals(user.getStatus(), userGetDTO.getStatus());
    assertEquals(user.getToken(), userGetDTO.getToken());
    assertEquals(user.getBio(), userGetDTO.getBio());
    assertEquals(user.getProfilePicture(), userGetDTO.getProfilePicture());
  }
}
