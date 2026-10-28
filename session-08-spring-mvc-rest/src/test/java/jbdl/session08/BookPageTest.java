package jbdl.session08;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import jbdl.session08.books.BookRequest;
import jbdl.session08.books.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/** Model, View and Controller of the server-rendered page, checked one by one. */
@SpringBootTest
@AutoConfigureMockMvc
class BookPageTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookService service;

    @BeforeEach
    void oneBook() {
        service.clear();
        service.create(new BookRequest("Refactoring", "Martin Fowler", "9780134757599", new BigDecimal("650.00"), 2018));
    }

    @Test
    @DisplayName("The controller picks the view 'books' and puts the books in the model")
    void controllerFillsModelAndPicksView() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", hasSize(1)));
    }

    @Test
    @DisplayName("The view renders the model as HTML")
    void viewRendersHtml() throws Exception {
        mvc.perform(get("/books").param("author", "Martin Fowler"))
                .andExpect(content().contentTypeCompatibleWith("text/html"))
                .andExpect(content().string(containsString("Books by Martin Fowler")))
                .andExpect(content().string(containsString("<td>Refactoring</td>")));
    }
}
