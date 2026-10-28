package jbdl.session08;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point. @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan:
 * Spring scans this package and below for @Controller/@Service/@Component classes, creates them as
 * beans, and auto-configures Spring MVC and an embedded Tomcat because they are on the classpath.
 */
@SpringBootApplication
public class BookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }
}
