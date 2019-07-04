package demo.nakedapp.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name="appointments")
public final class Appointment {

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
    new SimpleDateFormat("dd/MM/yyyy HH':'mm z");

  private static final Logger LOGGER =
    LoggerFactory.getLogger(Appointment.class);

  @Id
  @GeneratedValue(
    strategy=GenerationType.SEQUENCE,
    generator="appointments_id_seq"
  )
  private Long id;
  private String description;
  private Date date;

  protected Appointment() {}

  public Appointment(final Date date, final String description) {
    if (date == null || StringUtils.isBlank(description)) {
      final String message = String.format("Illegal argument; date==%s, "
        + "description==%s");
      throw new IllegalArgumentException(message);
    }
    final String message = String.format("Appointment[date==%s, "
      + "description=='%s']", date, description);
    LOGGER.info(message);
    this.description = description;
    this.date = date;
  }

  public String getDescription() {
    return this.description;
  }

  public String getDateString(final TimeZone timeZone) {
    SIMPLE_DATE_FORMAT.setTimeZone(timeZone);
    return SIMPLE_DATE_FORMAT.format(this.date);
  }

  @Override
  public String toString() {
    final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
    final String formattedDate = this.getDateString(utcTimeZone);
    return String.format("Appointment[id=%d, date=%s, description='%s'",
      this.id, formattedDate, this.description);
  }

}
