package ru.fil.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fil.library.models.Book;
import ru.fil.library.models.Person;
import ru.fil.library.repositories.BookRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAll(){
        return bookRepository.findAll();
    }

    public List<Book> getAll(Integer page, Integer booksPerPage, boolean sortByYear){
        if(page==null || booksPerPage==null){
            if(sortByYear) {
                return bookRepository.findAll(Sort.by("year"));
            }
            return bookRepository.findAll();
        }
        page = page<0 ? 0 : page;
        booksPerPage = booksPerPage<1 ? 1 : booksPerPage;

        if(sortByYear) {
            return bookRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        }
        return bookRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book getById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> findByPrefixName(String prefix){
        if(prefix!=null && !prefix.isEmpty()) {
            return bookRepository.findByNameStartingWithIgnoreCase(prefix);
        }
        return new ArrayList<>();
    }

    @Transactional
    public void setOwner(int id, Person owner){
        Book book=bookRepository.findById(id).orElse(null);
        if(book!=null){
            if (owner == null) {
                book.setOverdue(false);
                book.setRentedAt(null);
            }
            else {
                book.setRentedAt(LocalDateTime.now());
            }
            book.setOwner(owner);
        }
    }

    @Transactional
    public void save(Book book){
        book.setOverdue(false);
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        Book sourceBook=bookRepository.findById(id).get();
        updatedBook.setOwner(sourceBook.getOwner());
        updatedBook.setRentedAt(sourceBook.getRentedAt());
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id){
        bookRepository.deleteById(id);
    }

    @Transactional
    public void deleteOwner(Person owner){
        bookRepository.deleteOwner(owner);
    }
}
