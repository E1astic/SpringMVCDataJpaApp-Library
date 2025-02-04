package ru.fil.library.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.fil.library.models.Book;
import ru.fil.library.models.Person;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findByNameStartingWithIgnoreCase(String prefixName);

    @Modifying
    @Query("UPDATE Book SET owner=null, rentedAt=null WHERE owner=:person")
    void deleteOwner(@Param("person") Person person);
}
