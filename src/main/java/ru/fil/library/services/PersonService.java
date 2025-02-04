package ru.fil.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fil.library.models.Book;
import ru.fil.library.models.Person;
import ru.fil.library.repositories.PersonRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final BookService bookService;

    @Autowired
    public PersonService(PersonRepository personRepository, BookService bookService) {
        this.personRepository = personRepository;
        this.bookService = bookService;
    }

    public List<Person> getAll(){
        return personRepository.findAll();
    }

    public Person getById(int id){
        return personRepository.findById(id).orElse(null);
    }

    public Person getByName(String name){
        return personRepository.findByName(name).orElse(null);
    }

    public List<Book> getBooksByOwnerId(int id){
        Person owner = personRepository.findById(id).orElse(null);
        List<Book> books=null;
        if(owner!=null){
            books = owner.getBooks();
            for(Book book: books){
                if(ChronoUnit.DAYS.between(book.getRentedAt(), LocalDateTime.now())>10){
                    book.setOverdue(true);
                }
            }
        }
        return books;
    }

    @Transactional
    public void save(Person person){
        personRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        personRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){
        Person deletedPerson= personRepository.findById(id).orElse(null);
        if(deletedPerson != null){
            bookService.deleteOwner(deletedPerson);
            personRepository.delete(deletedPerson);
        }

    }

}
