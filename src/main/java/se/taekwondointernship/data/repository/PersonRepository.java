package se.taekwondointernship.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import se.taekwondointernship.data.models.entity.Person;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query("SELECT p FROM Person p WHERE p.firstName like :firstName or p.lastName like :lastName")
    Optional<Person> findByName(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
