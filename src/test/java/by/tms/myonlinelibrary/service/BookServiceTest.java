package by.tms.myonlinelibrary.service;

import by.tms.myonlinelibrary.entity.Book;
import by.tms.myonlinelibrary.entity.Genre;

import by.tms.myonlinelibrary.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBook() {
        Book book = new Book();
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setPublisher("Publisher");
        book.setPages(100);
        book.setGenre(Genre.ACTION);
        book.setRating(4);
        book.setDescription("Description");
        book.setImage("image");

        bookService.createBook(book);

        verify(bookRepository).save(argThat(book1 ->
                book1.getTitle().equals("Title")&&
                book1.getAuthor().equals("Author")&&
                book1.getPublisher().equals("Publisher")&&
                book1.getPages()==100&&
                book1.getGenre().equals(Genre.ACTION)&&
                book1.getRating()==4&&
                book1.getDescription().equals("Description")&&
                book1.getImage().equals("image")));
    }

    @Test
    void getAllBooks() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("First");
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Second");

        List<Book> expected = List.of(book1, book2);

        when(bookRepository.findAll()).thenReturn(expected);

        List<Book> actual = bookService.getAllBooks();

        assertEquals(2, actual.size());
        assertEquals("First", actual.get(0).getTitle());
        assertEquals("Second", actual.get(1).getTitle());

    }

    @Test
    void getBookById() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("First");

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        Book actual = bookService.getBookById(book.getId());

        assertNotNull(actual);
        assertEquals(book.getId(),actual.getId());
        assertEquals("First", actual.getTitle());
    }

    @Test
    void searchBookByTitle() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Title");
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Title");

        List<Book> expected = List.of(book1, book2);
        when(bookRepository.findByTitle(book1.getTitle())).thenReturn(expected);

        List<Book> actual=bookService.searchBook("title","Title");

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    void searchBookByAuthor() {
        Book book1 = new Book();
        book1.setId(1L);
        book1.setAuthor("Author");
        Book book2 = new Book();
        book2.setId(2L);
        book2.setAuthor("Author");

        List<Book> expected = List.of(book1, book2);
        when(bookRepository.findByAuthor(book1.getAuthor())).thenReturn(expected);

        List<Book> actual=bookService.searchBook("author","Author");

        assertNotNull(actual);
        assertEquals(2, actual.size());
    }
}