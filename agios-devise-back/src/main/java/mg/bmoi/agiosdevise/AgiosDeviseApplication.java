package mg.bmoi.agiosdevise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class AgiosDeviseApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgiosDeviseApplication.class, args);
    }

}
