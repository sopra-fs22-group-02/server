package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class PlaceRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void findByPlaceId_success() {
        // given
        Place place = new Place();
        place.setProviderId(2);
        place.setName("name");
        place.setAddress("Universitätsstrasse 1");
        place.setClosestCampus(Campus.CENTER);
        place.setDescription("this is my room");
        place.setPictureOfThePlace("some link");

        entityManager.persist(place);
        entityManager.flush();

        // when
        Place found = placeRepository.findByPlaceId(place.getPlaceId());

        // then
        assertEquals(found.getPlaceId(), place.getPlaceId());
        assertEquals(found.getProviderId(), place.getProviderId());
        assertEquals(found.getName(), place.getName());
        assertEquals(found.getAddress(), place.getAddress());
        assertEquals(found.getClosestCampus(), place.getClosestCampus());
        assertEquals(found.getDescription(), place.getDescription());
        assertEquals(found.getPictureOfThePlace(), place.getPictureOfThePlace());
    }

    @Test
    public void findByProviderId_success() {
        // given
        Place place = new Place();
        place.setProviderId(2);
        place.setName("name");
        place.setAddress("Universitätsstrasse 1");
        place.setClosestCampus(Campus.CENTER);
        place.setDescription("this is my room");
        place.setPictureOfThePlace("some link");

        entityManager.persist(place);
        entityManager.flush();

        // when
        Place found = placeRepository.findByProviderId(place.getProviderId());

        // then
        assertEquals(found.getPlaceId(), place.getPlaceId());
        assertEquals(found.getProviderId(), place.getProviderId());
        assertEquals(found.getName(), place.getName());
        assertEquals(found.getAddress(), place.getAddress());
        assertEquals(found.getClosestCampus(), place.getClosestCampus());
        assertEquals(found.getDescription(), place.getDescription());
        assertEquals(found.getPictureOfThePlace(), place.getPictureOfThePlace());
    }
}
