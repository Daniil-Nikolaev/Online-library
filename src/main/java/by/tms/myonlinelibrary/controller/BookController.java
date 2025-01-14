package by.tms.myonlinelibrary.controller;

import by.tms.myonlinelibrary.entity.Book;
import by.tms.myonlinelibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;




@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/all")
    public String all(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "all";
    }

    @GetMapping("/add-title")
    public String addTitle() {
        return "addTitle";
    }

    @PostMapping("/add-title")
    public String addTitle(Book book) {
        bookService.createBook(book);
        return "redirect:/book/add-file/"+book.getId();
    }

    @GetMapping("add-file/{bookId}")
    public String addFile(@PathVariable("bookId") Long bookId) {
        return "addFile";
    }

    @PostMapping("add-file/{bookId}")
    public String addFile(@PathVariable("bookId") Long bookId,@RequestParam MultipartFile file)  {
        bookService.saveContentBook(bookId, file);
        return "redirect:/";
    }

    @GetMapping("/{bookId}")
    public String show(@PathVariable("bookId") Long bookId, Model model) {
        model.addAttribute("book", bookService.getBookById(bookId));
        return "showBook";
    }

    @GetMapping("/{bookId}/read")
    public String read(@PathVariable("bookId") Long bookId, Model model) {
        String content=  bookService.readBook(bookId);
        model.addAttribute("content", content);
        return "readBook";
    }

    @PostMapping("/{bookId}/delete")
    public String delete(@PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return "redirect:/book/all";
    }
}