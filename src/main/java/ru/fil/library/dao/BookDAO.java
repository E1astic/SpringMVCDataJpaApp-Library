package ru.fil.library.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.fil.library.models.Book;
import ru.fil.library.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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

    public void setPersonId(int id, Person updatedPerson){
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE id=?",
                updatedPerson==null ? null : updatedPerson.getId(), id);
    }

    public Optional<Integer> getPersonId(int id){
        return jdbcTemplate.query("SELECT person_id FROM book WHERE id=?", new Object[]{id},
                new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Integer person_id=rs.getInt("person_id");
                        if(rs.wasNull()){
                            return null;
                        }
                        return person_id;
                    }
                }).stream().map(Optional::ofNullable).findAny().orElse(Optional.empty());
                // изначально тут возникала ошибка, когда обращение шло к книге, у которой person_id=null
                // тогда весь stream() состоял из null-значений и возникал NullPointerException
    }

    public Optional<Person> getOwner(int id){
        return jdbcTemplate.query("SELECT person.id, person.name, person.year " +
                "FROM book JOIN person ON book.person_id=person.id WHERE book.id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public void add(Book book){
        jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES(?,?,?)",
                book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book updatedBook){
        jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE id=?",
                updatedBook.getName(), updatedBook.getAuthor(), updatedBook.getYear(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM book WHERE id=?", id);
    }
}
