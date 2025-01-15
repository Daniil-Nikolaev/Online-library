package by.tms.myonlinelibrary.repository;

import by.tms.myonlinelibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    public List<Book> findByTitle(String title);
    public List<Book> findByAuthor(String author);
}
