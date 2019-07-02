package demo.nakedapp.controller;

import demo.nakedapp.entity.Appointment;
import demo.nakedapp.repository.AppointmentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppointmentController {

  @Autowired
  private AppointmentRepository repository;

  @GetMapping("/appointments")
  public String appointments(Model model) {
    Iterable<Appointment> appointments = this.repository.findAll();
    model.addAttribute("appointments", appointments);
    return "appointments";
  }

}
