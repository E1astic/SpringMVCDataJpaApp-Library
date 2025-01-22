package ru.fil.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.fil.library.models.Book;

import java.util.List;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getAll(){
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getById(int id){
        return jdbcTemplate.query("SELECT * FROM book WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class))
                .stream().findFirst().orElse(null);
    }

    public void add(Book book){
        jdbcTemplate.update("INSERT INTO book(person_id, name, author, year) VALUES(?,?,?,?)",
                book.getPerson_id(), book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book updatedBook){
        jdbcTemplate.update("UPDATE book SET person_id=?, name=?, author=?, year=? WHERE id=?",
                updatedBook.getPerson_id(), updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }
}
