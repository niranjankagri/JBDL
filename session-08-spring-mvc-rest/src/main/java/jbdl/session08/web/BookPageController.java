package jbdl.session08.web;

import jbdl.session08.books.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Classic MVC with a server-rendered page: the CONTROLLER fills the MODEL and returns the name of a
 * VIEW ("books" = templates/books.html); Thymeleaf renders the HTML from both.
 */
@Controller
public class BookPageController {

    private final BookService service;

    public BookPageController(BookService service) {
        this.service = service;
    }

    @GetMapping("/books")
    public String books(@RequestParam(required = false) String author, Model model) {
        model.addAttribute("books", service.findAll(author));
        model.addAttribute("author", author);
        return "books";
    }
}
