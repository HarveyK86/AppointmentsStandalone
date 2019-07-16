package appointments.standalone.repository;

import appointments.standalone.entity.Appointment;

import org.springframework.data.repository.CrudRepository;

public interface AppointmentRepository
  extends CrudRepository<Appointment, Long> {

}
