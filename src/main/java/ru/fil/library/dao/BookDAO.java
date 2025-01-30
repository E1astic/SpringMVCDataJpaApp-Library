package ru.fil.library.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.fil.library.models.Book;
import ru.fil.library.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public BookDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Book> getAll(){
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Book").getResultList();
    }

    @Transactional
    public Book getById(int id){
        Session session = sessionFactory.getCurrentSession();
        return session.get(Book.class, id);
    }

    @Transactional
    public void setOwner(int id, Person updatedPerson){
        Session session = sessionFactory.getCurrentSession();
        Book book=session.get(Book.class, id);
        book.setOwner(updatedPerson);
        if(updatedPerson!=null) {
            updatedPerson=session.merge(updatedPerson);
            updatedPerson.addBook(book);
        }
    }

    @Transactional
    public Optional<Person> getOwner(int id){
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(Book.class, id).getOwner());
    }

    @Transactional
    public void add(Book book){
        Session session = sessionFactory.getCurrentSession();
        session.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        Session session = sessionFactory.getCurrentSession();
        Book bookToBeUpdated=session.get(Book.class, id);
        bookToBeUpdated.setName(updatedBook.getName());
        bookToBeUpdated.setAuthor(updatedBook.getAuthor());
        bookToBeUpdated.setYear(updatedBook.getYear());
    }

    @Transactional
    public void delete(int id){
        Session session = sessionFactory.getCurrentSession();
        Book bookToBeDeleted=session.get(Book.class, id);
        session.remove(bookToBeDeleted);
        bookToBeDeleted.getOwner().getBooks().remove(bookToBeDeleted);
    }
}
