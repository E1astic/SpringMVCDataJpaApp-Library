package ru.fil.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fil.library.models.Book;
import ru.fil.library.models.Person;
import ru.fil.library.services.BookService;
import ru.fil.library.services.PersonService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BooksController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String getAll(@RequestParam(value = "page", required = false) Integer page,
                         @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                         @RequestParam(value = "sort_by_year",required = false) boolean sortByYear,
                         Model model) {
        model.addAttribute("books", bookService.getAll(page, booksPerPage, sortByYear));
        return "books/allBooks";
    }

    @GetMapping("/search")
    public String search(Model model){
        String name=null;
        model.addAttribute("name", name);
        return "books/searchBooks";
    }

    @GetMapping("/findByName")
    public String findByName(@RequestParam("name") String name,
                             Model model) {
        model.addAttribute("books", bookService.findByPrefixName(name));
        return "books/searchBooks";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        List<Person> people=null;
        boolean hasOwner=false;
        Person person=new Person();

        Book book=bookService.getById(id);
        Person owner=book.getOwner();

        if(owner!=null) {    // если книга занята
            person=owner;
            hasOwner=true;
        }
        else{
            people=personService.getAll();
        }

        model.addAttribute("book", book);
        model.addAttribute("people", people);
        model.addAttribute("hasOwner", hasOwner);
        model.addAttribute("person", person);

        return "books/currBook";
    }

    @PatchMapping("/{id}/addPerson")
    public String addPerson(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        Person owner=personService.getById(person.getId());
        bookService.setOwner(id, owner);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}/delPerson")
    public String delPerson(@PathVariable("id") int id){
        bookService.setOwner(id, null);
        return "redirect:/books/{id}";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.getById(id));
        return "books/editBook";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "books/editBook";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
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
        bookService.save(book);
        return "redirect:/books";
    }

}
