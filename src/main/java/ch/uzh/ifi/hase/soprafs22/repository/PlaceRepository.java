package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository("placeRepository")
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Place findByPlaceId(int placeId);

    Place findByProviderId(int providerId);
}
