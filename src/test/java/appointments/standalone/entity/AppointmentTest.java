package appointments.standalone.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public final class AppointmentTest {

  private static final String DATE = "07/04/2019 10:33 UTC";
  private static final String DESCRIPTION = "This is a test Appointment "
    + "created by AppointmentTest";

  private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");
  private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
    new SimpleDateFormat("dd/MM/yyyy HH':'mm z");

  /**
   * <b>Given</b> a <code>null</code> <b>date</b><br>
   * <i>and</i> a <b>description</b><br>
   * <b>when</b> creating a new instance of {@link Appointment}<br>
   * <b>then</b> the constructor <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see Appointment
   */
  @Test(expected=IllegalArgumentException.class)
  public void constructorShouldThrowDateIllegalArgument() {
    // Given a null date
    final Date date = null;
    // and a description
    final String description = DESCRIPTION;
    // when creating a new instance of Appointment
    new Appointment(date, description);
    // then the constructor should throw an IllegalArgumentException.
    fail("Appointment should throw an IllegalArgumentException for a null "
      + "date.");
  }

  /**
   * <b>Given</b> a <b>date</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only
   * <b>description</b><br>
   * <b>when</b> creating a new instance of {@link Appointment}<br>
   * <b>then</b> the constructor <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see Appointment
   */
  @Test
  public void constructorShouldThrowDescriptionIllegalArgument() {
    // Given a date
    final Date date = this.getDate();
    // and a null, empty or whitespace only description
    for (final String description : new String[] { null, "", " ", }) {
      try {
        // when creating a new instance of Appointment
        new Appointment(date, description);
        // then the constructor should throw an IllegalArgumentException.
        fail("Appointment should throw an IllegalArgumentException for a "
          + "null, empty or whitespace only description.");
      } catch(final IllegalArgumentException e) {
        // pass
      }
    }
  }

  /**
   * <b>Given</b> a <b>date</b><br>
   * <i>and</i> a <b>description</b><br>
   * <b>when</b> creating a new instance of {@link Appointment}<br>
   * <b>then</b> the constructor <i>should</i> execute without exception.
   *
   * @see Appointment
   */
  @Test
  public void constructorShouldExecute() {
    // Given a date
    final Date date = this.getDate();
    // and a description
    final String description = DESCRIPTION;
    // when creating a new instance of Appointment
    new Appointment(date, description);
    // then the constructor should execute without exception.
  }

  /**
   * <b>Given</b> an instance of {@link Appointment}<br>
   * <b>when</b> invoking <code>getDescripton</code><br>
   * <b>then</b> the method <i>should</i> return the description used during
   * instantiation.
   * 
   * @see Appointment
   */
  @Test
  public void getDescriptionShouldReturnInstantiationDescription() {
    // Given an instance of Appointment
    final Date date = this.getDate();
    final String description = DESCRIPTION;
    final Appointment appointment = new Appointment(date, description);
    // when invoking getDescription
    final String returned = appointment.getDescription();
    // then the method should return the description used during instantiation.
    assertEquals("Appointment should return the description used during "
      + "instantiation.", returned, description);
  }

  /**
   * <b>Given</b> an instance of {@link Appointment}<br>
   * <i>and</i> a <code>null</code> <b>timeZone</b><br>
   * <b>when</b> invoking <code>getDateString</code><br>
   * <b>then</b> the method <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see Appointment
   */
  @Test(expected=IllegalArgumentException.class)
  public void getDateStringShouldThrowIllegalArgument() {
    // Given an instance of Appointment
    final Date date = this.getDate();
    final String description = DESCRIPTION;
    final Appointment appointment = new Appointment(date, description);
    // and a null timeZone
    final TimeZone timeZone = null;
    // when invoking getDateString
    appointment.getDateString(timeZone);
    // then the method should throw an IllegalArgumentException.
    fail("getDateString should throw an IllegalArgumentException for a null "
      + "timeZone.");
  }

  /**
   * <b>Given</b> an instance of {@link Appointment}<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <b>when</b> invoking <code>getDateString</code><br>
   * <b>then</b> the method <i>should</i> return the date used during
   * instantiation in the correct format.
   * 
   * @see Appointment
   */
  @Test
  public void getDateStringShouldReturnInstantiationDate() {
    // Given an instance of Appointment
    final Date date = this.getDate();
    final String description = DESCRIPTION;
    final Appointment appointment = new Appointment(date, description);
    // and a timeZone
    final TimeZone timeZone = TIME_ZONE;
    // when invoking getDateString
    final String returned = appointment.getDateString(timeZone);
    // then the method should return the date used during instantiation in the
    // correct format.
    assertEquals("Appointment should return the date used during instantiation "
      + "in the correct format.", DATE, returned);
  }

  private Date getDate() {
    SIMPLE_DATE_FORMAT.setTimeZone(TIME_ZONE);
    final Date date;
    try {
      date = SIMPLE_DATE_FORMAT.parse(DATE);
    } catch(final ParseException e) {
      throw new IllegalStateException(e);
    }
    return date;
  }

}