package jbdl.session08;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jbdl.session08.books.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * The whole application starts (controllers, service, exception handler, Jackson, validation);
 * MockMvc sends requests through the DispatcherServlet without opening a network port.
 */
@SpringBootTest
@AutoConfigureMockMvc
class BookApiTest {

    private static final String CLEAN_CODE = """
            {"title": "Clean Code", "author": "Robert C. Martin", "isbn": "9780132350884",
             "price": 450.00, "publishedYear": 2008}""";
    private static final String EFFECTIVE_JAVA = """
            {"title": "Effective Java", "author": "Joshua Bloch", "isbn": "9780134685991",
             "price": 699.00, "publishedYear": 2018}""";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookService service;

    /** The service is a singleton bean shared by all tests in the context: reset it before each one. */
    @BeforeEach
    void emptyStore() {
        service.clear();
    }

    /** Test helper: create a book through the API itself (not the service), expecting 201. */
    private void create(String json) throws Exception {
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST creates: 201, Location header, JSON body with the new id")
    void postCreates() throws Exception {
        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(CLEAN_CODE))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/books/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    @DisplayName("GET lists all, filters by author, and gets one by id")
    void getReads() throws Exception {
        create(CLEAN_CODE);
        create(EFFECTIVE_JAVA);

        mvc.perform(get("/api/books")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)));
        mvc.perform(get("/api/books").param("author", "joshua bloch"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Effective Java"));
        mvc.perform(get("/api/books/2")).andExpect(status().isOk()).andExpect(jsonPath("$.publishedYear").value(2018));
    }

    @Test
    @DisplayName("PUT replaces the whole book and is idempotent")
    void putReplaces() throws Exception {
        create(CLEAN_CODE);
        String secondEdition = CLEAN_CODE.replace("450.00", "499.00").replace("2008", "2009");

        for (int i = 0; i < 2; i++) { // the same PUT twice leaves the same state
            mvc.perform(put("/api/books/1").contentType(MediaType.APPLICATION_JSON).content(secondEdition))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.price").value(499.00))
                    .andExpect(jsonPath("$.publishedYear").value(2009));
        }
        mvc.perform(get("/api/books")).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("PATCH changes only the price")
    void patchChangesPart() throws Exception {
        create(CLEAN_CODE);

        mvc.perform(patch("/api/books/1").contentType(MediaType.APPLICATION_JSON).content("{\"price\": 399.50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(399.50))
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    @DisplayName("DELETE: 204 the first time, 404 the second")
    void deleteRemoves() throws Exception {
        create(CLEAN_CODE);

        mvc.perform(delete("/api/books/1")).andExpect(status().isNoContent());
        mvc.perform(delete("/api/books/1")).andExpect(status().isNotFound());
        mvc.perform(get("/api/books/1")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Unknown id: 404 as application/problem+json")
    void notFoundProblem() throws Exception {
        mvc.perform(get("/api/books/42"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Book not found"))
                .andExpect(jsonPath("$.detail").value("book 42 not found"));
    }

    @Test
    @DisplayName("Invalid body: 400 with one message per field")
    void validationProblem() throws Exception {
        // four broken fields at once: blank title, 3-digit ISBN, negative price, year before printing
        String invalid = """
                {"title": "", "author": "X", "isbn": "123", "price": -5, "publishedYear": 1200}""";

        mvc.perform(post("/api/books").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation failed"))
                .andExpect(jsonPath("$.errors.title").exists())
                .andExpect(jsonPath("$.errors.isbn").value("must be 13 digits"))
                .andExpect(jsonPath("$.errors.price").exists())
                .andExpect(jsonPath("$.errors.publishedYear").exists());
    }

    @Test
    @DisplayName("Spring answers wrong method (405) and wrong media type (415) by itself")
    void frameworkErrors() throws Exception {
        // no controller code handles these: Spring MVC answers from the mappings and converters
        mvc.perform(post("/api/books/1")).andExpect(status().isMethodNotAllowed());
        mvc.perform(post("/api/books").contentType(MediaType.TEXT_PLAIN).content("hello"))
                .andExpect(status().isUnsupportedMediaType());
    }
}
