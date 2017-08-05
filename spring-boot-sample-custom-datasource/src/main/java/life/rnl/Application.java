package life.rnl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "life.rnl.single")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}