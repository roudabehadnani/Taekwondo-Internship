package se.taekwondointernship.data.service;

import org.springframework.transaction.annotation.Transactional;
import se.taekwondointernship.data.models.dto.PersonDto;
import se.taekwondointernship.data.models.form.PersonForm;

import java.util.List;

public interface PersonService {
    PersonDto create(PersonForm form);
    List<PersonDto> findAll();
    PersonDto findById(Integer id);
    PersonDto findByName(String name);

    @Transactional
    PersonDto addPassToPerson(Integer personId, Integer passId);
}
