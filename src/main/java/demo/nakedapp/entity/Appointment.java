package demo.nakedapp.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Appointment {

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH':'mm");

  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private Long id;
  private String description;
  private Date date;

  protected Appointment() {}

  public Appointment(String description, Date date) {
    this.description = description;
    this.date = date;
  }

  @Override
  public String toString() {
    String formattedDate = SIMPLE_DATE_FORMAT.format(this.date);
    return String.format("Appointment[id=%d, description='%s', date=%s", this.id, this.description, formattedDate);
  }

}
