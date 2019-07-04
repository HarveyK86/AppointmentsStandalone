package demo.nakedapp.repository;

import demo.nakedapp.entity.Appointment;

import org.springframework.data.repository.CrudRepository;

public interface AppointmentRepository
  extends CrudRepository<Appointment, Long> {

}
