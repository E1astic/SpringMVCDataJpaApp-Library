package ru.fil.library.controllers;

import jakarta.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fil.library.models.Book;
import ru.fil.library.models.Person;
import ru.fil.library.services.BookService;
import ru.fil.library.services.PersonService;
import ru.fil.library.utils.PersonValidator;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonService personService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String getAll(Model model){
        model.addAttribute("people", personService.getAll());
        return "people/allPeople";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personService.getById(id));
        model.addAttribute("books", personService.getBooksByOwnerId(id));
        return "people/currPerson";
    }

    @GetMapping("/new")
    public String addNew(@ModelAttribute("person") Person person){
        return "people/newPerson";
    }

    @PostMapping()
    public String save(@ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult){
        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors()){
            return "people/newPerson";
        }
        personService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personService.getById(id));
        return "people/editPerson";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "people/editPerson";
        }
        personService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personService.delete(id);
        return "redirect:/people";
    }

}
