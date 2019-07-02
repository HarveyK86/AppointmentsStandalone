package demo.nakedapp;

import demo.nakedapp.entity.Customer;
import demo.nakedapp.repository.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner demo(CustomerRepository repository) {
		return (args) -> {
			repository.save(new Customer("Jack", "Bauer"));
			repository.save(new Customer("Chloe", "O'Brian"));
			repository.save(new Customer("Kim", "Bauer"));
			repository.save(new Customer("David", "Palmer"));
			repository.save(new Customer("Michelle", "Dessler"));
			LOGGER.info("Customers found with findAll():");
			LOGGER.info("-------------------------------");
			for (Customer customer : repository.findAll()) {
				LOGGER.info(customer.toString());
			}
			LOGGER.info("");
			repository.findById(1L).ifPresent(customer -> {
				LOGGER.info("Customer found with findById(1L):");
				LOGGER.info("---------------------------------");
				LOGGER.info(customer.toString());
				LOGGER.info("");
			});
			LOGGER.info("Customer found with findByLastName('Bauer'):");
			LOGGER.info("--------------------------------------------");
			repository.findByLastName("Bauer").forEach(bauer -> {
				LOGGER.info(bauer.toString());
			});
			LOGGER.info("");
		};
	}

}
