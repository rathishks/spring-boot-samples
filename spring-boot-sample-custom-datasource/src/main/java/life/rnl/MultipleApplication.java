package life.rnl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "life.rnl.multiple")
public class MultipleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultipleApplication.class, args);
	}
}