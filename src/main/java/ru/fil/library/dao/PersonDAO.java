package ru.fil.library.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.fil.library.models.Book;
import ru.fil.library.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public PersonDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Person> getAll(){
        Session session=sessionFactory.getCurrentSession();
        return session.createQuery("from Person").getResultList();
    }

    @Transactional(readOnly = true)
    public Person getById(int id){
        Session session=sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Transactional(readOnly = true)
    public Optional<Person> getByName(String name){
        Session session=sessionFactory.getCurrentSession();
        return session.createQuery("from Person where name = :name").setParameter("name", name).getResultList()
                .stream().findAny();
    }

    @Transactional
    public void add(Person person){
        Session session=sessionFactory.getCurrentSession();
        session.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        Session session=sessionFactory.getCurrentSession();
        Person personToBeUpdated=session.get(Person.class, id);

        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setYear(updatedPerson.getYear());
    }

    @Transactional
    public void delete(int id){
        Session session=sessionFactory.getCurrentSession();
        Person deletedPerson=session.get(Person.class, id);
        List<Book> books=deletedPerson.getBooks();
        for(Book book : books){
            book.setOwner(null);
        }
        session.remove(deletedPerson);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks(int id){
        Session session=sessionFactory.getCurrentSession();
        return session.createQuery("from Book where owner.id = :id").setParameter("id", id).getResultList();
    }
}
