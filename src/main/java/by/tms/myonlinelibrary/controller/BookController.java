package by.tms.myonlinelibrary.controller;

import by.tms.myonlinelibrary.entity.Account;
import by.tms.myonlinelibrary.entity.Book;
import by.tms.myonlinelibrary.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


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
    public String show(@PathVariable("bookId") Long bookId,
                       @AuthenticationPrincipal Account account,
                       Model model) {
        try {
            Book book = bookService.getBookById(bookId);
            model.addAttribute("book", book);
            model.addAttribute(account);
            return "showBook";
        } catch (EntityNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "redirect: /";
        }
    }

    @GetMapping("/{bookId}/read")
    public String read(@PathVariable("bookId") Long bookId, Model model) {
        try {
            String content=  bookService.readBook(bookId);
            model.addAttribute("content", content);
            return "readBook";
        } catch (EntityNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "redirect: /";
        }
    }

    @PostMapping("/{bookId}/delete")
    public String delete(@PathVariable("bookId") Long bookId,@AuthenticationPrincipal Account account) {
        bookService.deleteBook(bookId,account);
        return "redirect:/book/all";
    }

    @GetMapping("/search")
    public String search(Model model) {
        return "search";
    }

    @PostMapping("/search")
    public String search(@RequestParam String critery,@RequestParam String keyword,Model model) {
        List<Book> books = bookService.searchBook(critery, keyword);
        model.addAttribute("books",books);
        return"/search";

    }
}