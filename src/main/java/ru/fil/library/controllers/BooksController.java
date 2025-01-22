package ru.fil.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fil.library.dao.BookDAO;
import ru.fil.library.models.Book;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDAO bookDAO;

    @Autowired
    public BooksController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("books", bookDAO.getAll());
        return "books/allBooks";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.getById(id));
        return "books/currBook";
    }

    @GetMapping("/new")
    public String addNew(@ModelAttribute("book") Book book){
        return "books/newBook";
    }

    @PostMapping()
    public String save(@ModelAttribute("book") Book book){
        bookDAO.add(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.getById(id));
        return "books/editBook";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("book") Book book){
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }
}
