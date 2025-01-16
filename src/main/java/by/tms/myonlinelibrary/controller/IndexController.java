package by.tms.myonlinelibrary.controller;

import by.tms.myonlinelibrary.entity.Book;
import by.tms.myonlinelibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    BookService bookService;

    @GetMapping
    public String greeting(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "homepage";
    }

}