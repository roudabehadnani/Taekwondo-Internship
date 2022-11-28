package se.taekwondointernship.data.service;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.taekwondointernship.data.models.dto.PersonDto;
import se.taekwondointernship.data.models.entity.Person;
import se.taekwondointernship.data.models.form.PersonForm;
import se.taekwondointernship.data.exceptions.ResourceNotFoundException;
import se.taekwondointernship.data.repository.PassRepository;
import se.taekwondointernship.data.repository.PersonRepository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Service
public class PersonServiceImpl implements PersonService{
    PersonRepository personRepository;
    PassRepository passRepository;
    ModelMapper modelMapper;
    JSONArray memberList = new JSONArray();
    JSONParser jsonParser = new JSONParser();
    public PersonServiceImpl(PersonRepository personRepository,PassRepository passRepository, ModelMapper modelMapper){
        this.personRepository = personRepository;
        this.passRepository = passRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    @Transactional
    public PersonDto create(PersonForm form) {
        Person person = personRepository.save(modelMapper.map(form, Person.class));
        JSONObject jsonPersonDetails = new JSONObject();
        jsonPersonDetails.put("personId", person.getPersonId());
        jsonPersonDetails.put("firstName", person.getFirstName());
        jsonPersonDetails.put("lastName", person.getLastName());
        jsonPersonDetails.put("phoneNumber", person.getPhoneNumber());
        jsonPersonDetails.put("email", person.getEmail());
        jsonPersonDetails.put("socialSecurityNumber", person.getSocialSecurityNumber());
        jsonPersonDetails.put("parentName", person.getParentName());
        jsonPersonDetails.put("parentNumber", person.getParentNumber());
        jsonPersonDetails.put("permissionPhoto", person.isPermissionPhoto());
        JSONObject jsonPerson = new JSONObject();
        jsonPerson.put("participant", jsonPersonDetails);
        memberList.add(jsonPerson);
        try (FileWriter file = new FileWriter("members.json")){
            file.write(memberList.toJSONString());
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonDto> findAll() {
        List<Person> personList = new ArrayList<>();
        try (FileReader reader = new FileReader("members.json")) {
            Object obj = jsonParser.parse(reader);
            JSONArray memberList = (JSONArray) obj;
            System.out.println(memberList);
            memberList.forEach(mbr -> parseJsonPerson( (JSONObject) mbr));
            personList = memberList;
        } catch (ParseException | IOException e){
            e.printStackTrace();
        }
        List<PersonDto> personDtoList = new ArrayList<>();
        personList.forEach((person -> personDtoList.add(modelMapper.map(person, PersonDto.class))));
        return personDtoList;
    }


    private void parseJsonPerson(JSONObject person){
        JSONObject jsonPerson = (JSONObject) person.get("participant");
        String personId = (String) jsonPerson.get("personId");
        String firstName = (String) jsonPerson.get("firstName");
        String lastName = (String) jsonPerson.get("lastName");
        String phoneNumber = (String) jsonPerson.get("phoneNumber");
        String email = (String) jsonPerson.get("email");
        String socialSecurityNumber = (String) jsonPerson.get("socialSecurityNumber");
        String parentName = (String) jsonPerson.get("parentName");
        String parentNumber = (String) jsonPerson.get("parentNumber");
        boolean permissionPhoto = (boolean) Boolean.parseBoolean(String.valueOf(jsonPerson.get("permissionPhoto")));
    }

    @Override
    @Transactional(readOnly = true)
    public PersonDto findByName(String firstName, String lastName) {
        if (firstName == null && lastName == null) throw new IllegalArgumentException("Name can not be null");
        Person foundByName = personRepository.findByName(firstName, lastName).orElseThrow(
                () -> new ResourceNotFoundException("Participant can not be Found."));
        return modelMapper.map(foundByName, PersonDto.class);
    }
}
