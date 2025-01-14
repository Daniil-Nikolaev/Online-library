package by.tms.myonlinelibrary.repository;

import by.tms.myonlinelibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
