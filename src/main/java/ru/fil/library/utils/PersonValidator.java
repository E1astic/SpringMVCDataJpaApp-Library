package ru.fil.library.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.fil.library.models.Person;
import ru.fil.library.services.PersonService;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(Person.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person=(Person)target;

        // если человек с таким именем уже существует (!=null)
        if(personService.getByName(person.getName())!=null){
            errors.rejectValue("name", "", "Person with this name already exists");
        }
    }
}
