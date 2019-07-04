package demo.nakedapp.controller;

import demo.nakedapp.repository.AppointmentRepository;

import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public final class AppointmentControllerTest {

  @Mock
  private Model model;

  @Mock
  private AppointmentRepository appointmentRepository;

  private static final String DATE = "04/07/2019";
  private static final String TIME = "13:20";
  private static final String TIME_ZONE = "Etc\\GMT-1";
  private static final String DESCRIPTION = "This is a test Appointment "
    + "created by AppointmentControllerTest";
  private static final String DEFAULT_TIME_ZONE = "Etc\\GMT";

  private static final String ERROR_PARAM = "error";

  private static final String APPOINMENTS_TEMPLATE = "appointments";

  /**
   * <b>Given</b> a <code>null</code> {@link AppointmentRepository}<br>
   * <b>when</b> creating a new instance of {@link AppointmentController}<br>
   * <b>then</b> then constructor <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see AppointmentRepository
   * @see AppointmentController
   */
  @Test(expected=IllegalArgumentException.class)
  public void constructorShouldThrowIllegalArgument() {
    // Givem a null AppointmentRepository
    final AppointmentRepository appointmentRepository = null;
    // when creating a new instance of AppointmentController
    new AppointmentController(appointmentRepository);
    // then the constructor should throw an IllegalArgumentException.
    fail("AppointmentController should throw an IllegalArgumentException for a "
      + "null appointmentRepository.");
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentRepository}<br>
   * <b>when</b> creating a new instance of {@link AppointmentController}<br>
   * <b>then</b> the constructor <i>should</i> execute without exception.
   * 
   * @see AppointmentRepository
   * @see AppointmentController
   */
  @Test
  public void constructorShouldExecute() {
    // Given an instance of AppointmentRepository
    final AppointmentRepository appointmentRepository =
      this.appointmentRepository;
    // when creating a new instance of AppointmentController
    new AppointmentController(appointmentRepository);
    // then the constructor should execute without exception.
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>model</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only
   * <b>defaultTimeZone</b><br>
   * <b>when</b> invoking <code>appointments</code><br>
   * <b>then</b> the method <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsDefaultTimeZoneShouldThrowIllegalArgument() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a model
    final Model model = this.model;
    // and a null, empty or whitespace only defaultTimeZone
    for (final String defaultTimeZone : new String[] { null, "", " " }) {
      try {
        // when invoking appointments
        appointmentController.appointments(defaultTimeZone, model);
        // then the method should throw an IllegalArgumentException.
        fail("appointments should throw an IllegalArgumentException for a "
          + "null, empty or whitespace only defaultTimeZone.");
      } catch(final IllegalArgumentException e) {
        // pass
      }
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>null</code> <b>model</b><br>
   * <b>when</b> invoking <code>appointments</code><br>
   * <b>then</b> the method <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see AppointmentController
   */
  @Test(expected=IllegalArgumentException.class)
  public void appointmentsModelShouldThrowIllegalArgument() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a null model
    final Model model = null;
    // when invoking appointments
    appointmentController.appointments(defaultTimeZone, model);
    // then the method should throw an IllegalArgumentException.
    fail("appointments should throw an IllegalArgumentException for a "
      + "null model.");
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <b>model</b><br>
   * <b>when</b> invoking <code>appointments</code><br>
   * <b>then</b> the method <i>should</i> return the name of the appointments
   * <code>Thymeleaf</code> template.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsShouldReturnAppointmentsTemplate() {
    // Given and instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a model
    final Model model = this.model;
    // when invoking appointments
    final String response = appointmentController.appointments(defaultTimeZone,
      model);
    // then the method should return the name of the appointments Thymeleaf
    // template.
    assertEquals("appointments should return the name of the appointments "
      + "Thymeleaf template", APPOINMENTS_TEMPLATE, response);
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <b>model</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only <b>date</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateDateShouldThrowIllegalArgument() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a time (formatted HH:mm)
    final String time = TIME;
    // and a timeZone
    final String timeZone = TIME_ZONE;
    // and a description
    final String description = DESCRIPTION;
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a model
    final Model model = this.model;
    // and a null, empty or whitespace only date
    for (final String date : new String[] { null, "", " " }) {
      try {
        // when invoking appointmentsCreate
        appointmentController.appointmentsCreate(date, time, timeZone,
          description, defaultTimeZone, model);
        // then the method should throw an IllegalArgumentException.
        fail("appointmentsCreate should throw an IllegalArgumentException for "
          + "a null, empty or whitespace only date.");
      } catch(final IllegalArgumentException e) {
        // pass
      }
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <b>model</b><br>
   * <i>and</i> an incorrectly formatted <b>date</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> populate an error.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateDateShouldPopulateError() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a time (formatted HH:mm)
    final String time = TIME;
    // and a timeZone
    final String timeZone = TIME_ZONE;
    // and a description
    final String description = DESCRIPTION;
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a model
    final Map<String, String> storedAttributes = new HashMap<>();
    final Model model = mock(Model.class);
    doAnswer(new Answer<Void>() {

      @Override
      public Void answer(final InvocationOnMock invocation) {
        final Object[] args = invocation.getArguments();
        storedAttributes.put((String) args[0], (String) args[1]);
        return null;
      }

    }).when(model).addAttribute(anyString(), anyString());
    // and an incorrectly formatted date
    final String date = "Incorrect Formatting";
    // when invoking appointmentsCreate
    appointmentController.appointmentsCreate(date, time, timeZone, description,
      defaultTimeZone, model);
    // then the method should populate an error.
    final String error = storedAttributes.get(ERROR_PARAM);
    assertNotNull("appointmentsCreate should populate an error for an "
      + "incorrectly formatted date.", error);
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <b>model</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only <b>time</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateTimeShouldThrowIllegalArgument() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a date (formatted dd/MM/yyyy)
    final String date = DATE;
    // and a timeZone
    final String timeZone = TIME_ZONE;
    // and a description
    final String description = DESCRIPTION;
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a model
    final Model model = this.model;
    // and a null, empty or whitespace only time
    for (final String time : new String[] { null, "", " " }) {
      try {
        // when invoking appointmentsCreate
        appointmentController.appointmentsCreate(date, time, timeZone,
          description, defaultTimeZone, model);
        // then the method should throw an IllegalArgumentException.
        fail("appointmentsCreate should throw an IllegalArgumentException for "
          + "a null, empty or whitespace only time.");
      } catch(final IllegalArgumentException e) {
        // pass
      }
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyy</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <b>model</b><br>
   * <i>and</i> an incorrectly formatted <b>time</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> return populate an error.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateTimeShouldPopulateError() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a date (formatted dd/MM/yyyy)
    final String date = DATE;
    // and a timeZone
    final String timeZone = TIME_ZONE;
    // and a description
    final String description = DESCRIPTION;
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a model
    final Map<String, String> storedAttributes = new HashMap<>();
    final Model model = mock(Model.class);
    doAnswer(new Answer<Void>() {

      @Override
      public Void answer(final InvocationOnMock invocation) {
        final Object[] args = invocation.getArguments();
        storedAttributes.put((String) args[0], (String) args[1]);
        return null;
      }

    }).when(model).addAttribute(anyString(), anyString());
    // and an incorrectly formatted time
    final String time = "Incorrect Formatting";
    // when invoking appointmentsCreate
    appointmentController.appointmentsCreate(date, time, timeZone, description,
      defaultTimeZone, model);
    // then the method should populate an error.
    final String error = storedAttributes.get(ERROR_PARAM);
    assertNotNull("appointmentsCreate should populate an error for an "
      + "incorrectly formatted time.", error);
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <b>model</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only
   * <b>timeZone</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateTimeZoneShouldThrowIllegalArgument() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a date (formatted dd/MM/yyyy)
    final String date = DATE;
    // and a time (formatted HH:mm)
    final String time = TIME;
    // and a description
    final String description = DESCRIPTION;
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a model
    final Model model = this.model;
    // and a null, empty or whitespace only timeZone
    for (final String timeZone : new String[] { null, "", " " }) {
      try {
        // when invoking appointmentsCreate
        appointmentController.appointmentsCreate(date, time, timeZone,
          description, defaultTimeZone, model);
        // then the method should throw an IllegalArgumentException.
        fail("appointmentsCreate should throw an IllegalArgumentException for "
          + "a null, empty or whitespace only timeZone.");
      } catch(final IllegalArgumentException e) {
        // pass
      }
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <b>model</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only
   * <b>description</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateDescriptionShouldThrowIllegalArgument() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a date (formatted dd/MM/yyyy)
    final String date = DATE;
    // and a time (formatted HH:mm)
    final String time = TIME;
    // and a timeZone
    final String timeZone = TIME_ZONE;
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a model
    final Model model = this.model;
    // and a null, empty or whitespace only description
    for (final String description : new String[] { null, "", " " }) {
      try {
        // when invoking appointmentsCreate
        appointmentController.appointmentsCreate(date, time, timeZone,
          description, defaultTimeZone, model);
        // then the method should throw an IllegalArgumentException.
        fail("appointmentsCreate should throw an IllegalArgumentException for "
          + "a null, empty or whitespace only description.");
      } catch(final IllegalArgumentException e) {
        // pass
      }
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>model</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only
   * <b>defaultTimeZone</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateDefaultTimeZoneShouldThrowIllegalArgument() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a date (formatted dd/MM/yyyy)
    final String date = DATE;
    // and a time (formatted HH:mm)
    final String time = TIME;
    // and a timeZone
    final String timeZone = TIME_ZONE;
    // and a description
    final String description = DESCRIPTION;
    // and a model
    final Model model = this.model;
    // and a null, empty or whitespace only defaultTimeZone
    for (final String defaultTimeZone : new String[] { null, "", " " }) {
      try {
        // when invoking appointmentsCreate
        appointmentController.appointmentsCreate(date, time, timeZone,
          description, defaultTimeZone, model);
        // then the method should throw an IllegalArgumentException.
        fail("appointmentsCreate should throw an IllegalArgumentException for "
          + "a null, empty or whitespace only defaultTimeZone.");
      } catch(final IllegalArgumentException e) {
        // pass
      }
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>null</code> <b>model</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see AppointmentController
   */
  @Test(expected=IllegalArgumentException.class)
  public void appointmentsCreateModelShouldThrowIllegalArgument() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a date (formatted dd/MM/yyyy)
    final String date = DATE;
    // and a time (formatted HH:mm)
    final String time = TIME;
    // and a timeZone
    final String timeZone = TIME_ZONE;
    // and a description
    final String description = DESCRIPTION;
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a null model
    final Model model = null;
    // when invoking appointmentsCreate
    appointmentController.appointmentsCreate(date, time, timeZone, description,
      defaultTimeZone, model);
    // then the method should throw an IllegalArgumentException.
    fail("appointmentsCreate should throw an IllegalArgumentException for a "
      + "null model.");
  }

}