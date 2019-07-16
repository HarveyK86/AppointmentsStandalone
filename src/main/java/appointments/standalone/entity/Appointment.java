package appointments.standalone.entity;

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

/**
 * <code>Appointment</code> is a business entity that can accessed via
 * {@link AppointmentController} and {@link AppointmentRepository}.
 * 
 * @see demo.nakedapp.controller.AppointmentController
 * @see demo.nakedapp.repository.AppointmentRepository
 */
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

  /**
   * Creates a new instance of <code>Appointment</code>.
   */
  protected Appointment() {}

  /**
   * Creates a new instance of <code>Appointment</code> usinf the specified
   * <b>date</b> and <b>description</b>.
   * 
   * @param date The date of the Appointment. This cannot be <code>null</code>.
   * @param description A description of the Appointment. This cannot be
   *          <code>null</code>, empty or whitespace only.
   * 
   * @throws IllegalArgumentException If <b>date</b> is <code>null</code> or if
   *          <b>description</b> is <code>null</code>, empty or whitespace only.
   */
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

  /**
   * Returns the description of <i>this</i> instance of
   * <code>Appointment</code>.
   * 
   * @return The description of <i>this</i> instance of
   *          <code>Appointment</code>. This cannot be <code>null</code>, empty
   *          or whitespace only.
   */
  public String getDescription() {
    final String message = String.format("getDescription returns '%s'",
      this.description);
    LOGGER.info(message);
    return this.description;
  }

  /**
   * Returns the date of <i>this</i> instance of <code>Appointment</code> for
   * specified <b>timeZone</b> in the format "dd/MM/yyyy HH:mm z".
   * 
   * @param timeZone The <code>TimeZone</code> for the date. This cannot be
   *          <code>null</code>.
   * 
   * @return The date of <i>this</i> instance of <code>Appointment</code> in the
   *          format "dd/MM/yyyy HH:mm z". This cannot be <code>null</code>,
   *          empty or whitespace only.
   * 
   * @throws IllegalArgumentException If <b>timeZone</b> is <code>null</code>.
   */
  public String getDateString(final TimeZone timeZone) {
    if (timeZone == null) {
      final String message = String.format("Illegal argument; timeZone==%s",
        timeZone);
      throw new IllegalArgumentException(message);
    }
    String message = String.format("getDateString[timeZone=='%s']",
      timeZone.getID());
    LOGGER.info(message);
    SIMPLE_DATE_FORMAT.setTimeZone(timeZone);
    final String dateString = SIMPLE_DATE_FORMAT.format(this.date);
    message = String.format("getDateString returns '%s'", dateString);
    LOGGER.info(message);
    return dateString;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    final TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
    final String formattedDate = this.getDateString(utcTimeZone);
    return String.format("Appointment[id=%d, date=%s, description='%s'",
      this.id, formattedDate, this.description);
  }

}
