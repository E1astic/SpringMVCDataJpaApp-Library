package ru.fil.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fil.library.dao.BookDAO;
import ru.fil.library.dao.PersonDAO;
import ru.fil.library.models.Book;
import ru.fil.library.models.Person;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("books", bookDAO.getAll());
        return "books/allBooks";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        List<Person> people=null;
        boolean hasOwner=false;
        Person person=new Person();

        Optional<Person> owner=bookDAO.getOwner(id);
        if(owner.isPresent()) {    // если книга занята
            person=owner.get();
            hasOwner=true;
        }
        else{
            people=personDAO.getAll();
        }

        model.addAttribute("book", bookDAO.getById(id));
        model.addAttribute("people", people);
        model.addAttribute("hasOwner", hasOwner);
        model.addAttribute("person", person);

        return "books/currBook";
    }

    @PatchMapping("/{id}/addPerson")
    public String addPerson(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        Person owner=personDAO.getById(person.getId());
        bookDAO.setOwner(id, owner);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/delPerson")
    public String delPerson(@PathVariable("id") int id){
        bookDAO.setOwner(id, null);
        return "redirect:/books/{id}";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.getById(id));
        return "books/editBook";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "books/editBook";
        }
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String addNew(@ModelAttribute("book") Book book){
        return "books/newBook";
    }

    @PostMapping()
    public String save(@ModelAttribute("book") @Valid Book book,
                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "books/newBook";
        }
        bookDAO.add(book);
        return "redirect:/books";
    }

}
