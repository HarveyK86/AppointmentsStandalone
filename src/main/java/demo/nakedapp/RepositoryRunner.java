package demo.nakedapp;

import demo.nakedapp.entity.Appointment;
import demo.nakedapp.entity.Customer;
import demo.nakedapp.repository.AppointmentRepository;
import demo.nakedapp.repository.CustomerRepository;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RepositoryRunner implements CommandLineRunner {

  @Autowired
  private AppointmentRepository appointmentRepository;

  @Autowired
  private CustomerRepository customerRepository;

  private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryRunner.class);

  @Override
  public void run(String... args) throws Exception {
    LOGGER.info(String.format("customerRepository==%s, appointmentRepository==%s", this.customerRepository, this.appointmentRepository));
    /*this.customerRepository.save(new Customer("Jack", "Bauer"));
    this.customerRepository.save(new Customer("Chloe", "O'Brian"));
    this.customerRepository.save(new Customer("Kim", "Bauer"));
    this.customerRepository.save(new Customer("David", "Palmer"));
    this.customerRepository.save(new Customer("Michelle", "Dessler"));
    LOGGER.info("Customers found with findAll():");
    LOGGER.info("-------------------------------");
    for (Customer customer : this.customerRepository.findAll()) {
      LOGGER.info(customer.toString());
    }
    LOGGER.info("");
    this.customerRepository.findById(1L).ifPresent(customer -> {
      LOGGER.info("Customer found with findById(1L):");
      LOGGER.info("---------------------------------");
      LOGGER.info(customer.toString());
      LOGGER.info("");
    });
    LOGGER.info("Customer found with findByLastName('Bauer'):");
    LOGGER.info("--------------------------------------------");
    this.customerRepository.findByLastName("Bauer").forEach(bauer -> {
      LOGGER.info(bauer.toString());
    });
    LOGGER.info("");
    this.appointmentRepository.save(new Appointment(new Date(), "Lorem Ipsum"));
    this.appointmentRepository.save(new Appointment(new Date(), "Dolor Sit Amet"));
    LOGGER.info("Appointments found with findAll():");
    LOGGER.info("----------------------------------");
    for (Appointment appointment : this.appointmentRepository.findAll()) {
      LOGGER.info(appointment.toString());
    }
    LOGGER.info("");
    this.appointmentRepository.findById(6L).ifPresent(appointment -> {
      LOGGER.info("Appointment found with findById(1L):");
      LOGGER.info("------------------------------------");
      LOGGER.info(appointment.toString());
      LOGGER.info("");
    });
    LOGGER.info("Done");*/
  }

}
