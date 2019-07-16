package appointments.standalone.controller;

import appointments.standalone.repository.AppointmentRepository;

import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.ui.Model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public final class AppointmentControllerTest {

  @Mock
  private AppointmentRepository appointmentRepository;

  private static final String DATE = "2019-07-05";
  private static final String TIME = "13:20";
  private static final String TIME_ZONE = "Etc\\GMT-1";
  private static final String DESCRIPTION = "This is a test Appointment "
    + "created by AppointmentControllerTest";
  private static final String DEFAULT_TIME_ZONE = "Etc\\GMT";

  private static final String ERROR_PARAM = "error";

  /**
   * <b>Given</b> a <code>null</code> {@link AppointmentRepository}<br>
   * <b>when</b> creating a new instance of {@link AppointmentController}<br>
   * <b>then</b> the constructor <i>should</i> throw an
   * <code>IllegalArgumentException</code>.
   * 
   * @see demo.nakedapp.repository.AppointmentRepository
   * @see AppointmentController
   */
  @Test(expected=IllegalArgumentException.class)
  public void constructorShouldThrowIllegalArgument() {
    // Given a null AppointmentRepository
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
   * @see demo.nakedapp.repository.AppointmentRepository
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
   * <i>and</i> <code>false</code> <b>simulateError</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only
   * <b>defaultTimeZone</b><br>
   * <i>and</i> a <b>model</b><br>
   * <b>when</b> invoking <code>appointments</code><br>
   * <b>then</b> the method <i>should</i> populate an error.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsDefaultTimeZoneShouldPopulateError() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a false simulateError
    final String simulateError = Boolean.FALSE.toString();
    // and a null, empty or whitespace only defaultTimeZone
    for (final String defaultTimeZone : new String[] { null, "", " " }) {
      // and a model
      final Map<String, String> attributesMap = new HashMap<>();
      final Model model = this.getMockModel(attributesMap);
      // when invoking appointments
      appointmentController.appointments(defaultTimeZone, simulateError, model);
      // then the method should populate an error.
      final String errorParam = attributesMap.get(ERROR_PARAM);
      assertNotNull("appointments should populate an error for a null, empty or"
        + "whitespace only defaultTimeZone.", errorParam);
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>null</code>, empty, whitepsace only or <code>true</code>
   * <b>simulateError</b><br>
   * <i>and</i> a <b>model</b><br>
   * <b>when</b> invoking <code>appointments</code><br>
   * <b>then</b> the method <i>should</i> populate an error.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentSimulateErrorShouldPopulateError() {
    // Given an instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a null, empty, whitespace only or true simulateError
    for (final String simulateError : new String[] { null, "", " ",
      Boolean.TRUE.toString() }) {
      // and a model
      final Map<String, String> attributesMap = new HashMap<>();
      final Model model = this.getMockModel(attributesMap);
      // when invoking appointments
      appointmentController.appointments(defaultTimeZone, simulateError, model);
      // then the method should populate an error.
      final String errorParam = attributesMap.get(ERROR_PARAM);
      assertNotNull("appointments should populate an error for a null, empty, "
        + "whitespace only or true simulateError.", errorParam);
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>false</code> <b>simulateError</b><br>
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
    // and a false simulateError
    final String simulateError = Boolean.FALSE.toString();
    // and a null model
    final Model model = null;
    // when invoking appointments
    appointmentController.appointments(defaultTimeZone, simulateError, model);
    // then the method should throw an IllegalArgumentException.
    fail("appointments should throw an IllegalArgumentException for a "
      + "null model.");
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>false</code> <b>simulateError</b><br>
   * <i>and</i> a <b>model</b><br>
   * <b>when</b> invoking <code>appointments</code><br>
   * <b>then</b> the method <i>should not</i> populate an error.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsShouldNotPopulateError() {
    // Given and instance of AppointmentController
    final AppointmentController appointmentController =
      new AppointmentController(this.appointmentRepository);
    // and a defaultTimeZone
    final String defaultTimeZone = DEFAULT_TIME_ZONE;
    // and a false simulateError
    final String simulateError = Boolean.FALSE.toString();
    // and a model
    final Map<String, String> attributesMap = new HashMap<>();
    final Model model = this.getMockModel(attributesMap);
    // when invoking appointments
    final String response = appointmentController.appointments(defaultTimeZone,
      simulateError, model);
    // then the method should not populate an error.
    final String errorParam = attributesMap.get(ERROR_PARAM);
    assertNull("appointments should not populate an error.", errorParam);
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>false</code> <b>simulateError</b><br>
   * <i>and</i> a <code>null</code>, empty, whitespace only or incorrectly
   * formatted <b>date</b><br>
   * <i>and</i> a <b>model</b><br>
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
    // and a false simulateError
    final String simulateError = Boolean.FALSE.toString();
    // and a null, empty, whitespace only or incorrectly formatted date
    for (final String date : new String[] { null, "", " ", "Incorrect "
      + "Formatting" }) {
      // and a model
      final Map<String, String> attributesMap = new HashMap<>();
      final Model model = this.getMockModel(attributesMap);
      // when invoking appointmentsCreate
      appointmentController.appointmentsCreate(date, time, timeZone,
        description, defaultTimeZone, simulateError, model);
      // then the method should populate an error.
      final String errorParam = attributesMap.get(ERROR_PARAM);
      assertNotNull("appointmentsCreate should populate an error for a null, "
        + "empty, whitespace only or incorrectly formatted date.", errorParam);
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>false</code> <b>simulateError</b><br>
   * <i>and</i> a <code>null</code>, empty, whitespace only or incorrectly
   * formatted <b>time</b><br>
   * <i>and</i> a <b>model</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> populate an error.
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
    // and a false simulateError
    final String simulateError = Boolean.FALSE.toString();
    // and a null, empty, whitespace only or incorrectly formatted time
    for (final String time : new String[] { null, "", " ", "Incorrect "
      + "Formatting" }) {
      // and a model
      final Map<String, String> attributesMap = new HashMap<>();
      final Model model = this.getMockModel(attributesMap);
      // when invoking appointmentsCreate
      appointmentController.appointmentsCreate(date, time, timeZone,
        description, defaultTimeZone, simulateError, model);
      // then the method should populate an error.
      final String errorParam = attributesMap.get(ERROR_PARAM);
      assertNotNull("appointmentsCreate should poplulate an error for a null, "
        + " empty, whitespace only or incorrectly formatted time.", errorParam);
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>false</code> <b>simulateError</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only
   * <b>timeZone</b><br>
   * <i>and</i> a <b>model</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> populate an error.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateTimeZoneShouldPopulateError() {
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
    // and a false simulateError
    final String simulateError = Boolean.FALSE.toString();
    // and a null, empty or whitespace only timeZone
    for (final String timeZone : new String[] { null, "", " " }) {
      // and a model
      final Map<String, String> attributesMap = new HashMap<>();
      final Model model = this.getMockModel(attributesMap);
      // when invoking appointmentsCreate
      appointmentController.appointmentsCreate(date, time, timeZone,
        description, defaultTimeZone, simulateError, model);
      // then the method should populate an error.
      final String errorParam = attributesMap.get(ERROR_PARAM);
      assertNotNull("appointmentsCreate should populate an error for a null, "
        + "empty or whitespace only timeZone.", errorParam);
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>false</code> <b>simulateError</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only
   * <b>description</b><br>
   * <i>and</i> a <b>model</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> populate an error.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateDescriptionShouldPopulateError() {
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
    // and a false simulateError
    final String simulateError = Boolean.FALSE.toString();
    // and a null, empty or whitespace only description
    for (final String description : new String[] { null, "", " " }) {
      // and a model
      final Map<String, String> attributesMap = new HashMap<>();
      final Model model = this.getMockModel(attributesMap);
      // when invoking appointmentsCreate
      appointmentController.appointmentsCreate(date, time, timeZone,
        description, defaultTimeZone, simulateError, model);
      // then the method should populate an error.
      final String errorParam = attributesMap.get(ERROR_PARAM);
      assertNotNull("appointmentsCreate should populate an error for a null, "
        + "empty or whitespace only description.", errorParam);
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <code>false</code> <b>simulateError</b><br>
   * <i>and</i> a <code>null</code>, empty or whitespace only
   * <b>defaultTimeZone</b><br>
   * <i>and</i> a <b>model</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> populate an error.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateDefaultTimeZoneShouldPopulateError() {
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
    // and a false simulateError
    final String simulateError = Boolean.FALSE.toString();
    // and a null, empty or whitespace only defaultTimeZone
    for (final String defaultTimeZone : new String[] { null, "", " " }) {
      // and a model
      final Map<String, String> attributesMap = new HashMap<>();
      final Model model = this.getMockModel(attributesMap);
      // when invoking appointmentsCreate
      appointmentController.appointmentsCreate(date, time, timeZone,
        description, defaultTimeZone, simulateError, model);
      // then the method should populate an error.
      final String errorParam = attributesMap.get(ERROR_PARAM);
      assertNotNull("appointmentsCreate should populate an error for a null, "
        + "empty or whitespace only defaultTimeZone.", errorParam);
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>null</code>, empty, whitespace only or <code>true</code>
   * <b>simulateError</b><br>
   * <i>and</i> a <b>model</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should</i> populate an error.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateSimulateErrorShouldPopulateError() {
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
    // and a null, empty, whitespace only or true simulateError
    for (final String simulateError : new String[] { null, "", " ",
      Boolean.TRUE.toString() }) {
      // and a model
      final Map<String, String> attributesMap = new HashMap<>();
      final Model model = this.getMockModel(attributesMap);
      // when invoking appointmentsCreate
      appointmentController.appointmentsCreate(date, time, timeZone,
        description, defaultTimeZone, simulateError, model);
      // then the method should populate an error.
      final String errorParam = attributesMap.get(ERROR_PARAM);
      assertNotNull("appointmentsCreate should populate an error for a null, "
        + "empty, whitespace only or true simulateError.", errorParam);
    }
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>false</code> <b>simulateError</b><br>
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
    // and a false simulateError
    final String simulateError = Boolean.FALSE.toString();
    // and a null model
    final Model model = null;
    // when invoking appointmentsCreate
    appointmentController.appointmentsCreate(date, time, timeZone, description,
      defaultTimeZone, simulateError, model);
    // then the method should throw an IllegalArgumentException.
    fail("appointmentsCreate should throw an IllegalArgumentException for a "
      + "null model.");
  }

  /**
   * <b>Given</b> an instance of {@link AppointmentController}<br>
   * <i>and</i> a <b>date</b> (formatted <code>dd/MM/yyyy</code>)<br>
   * <i>and</i> a <b>time</b> (formatted <code>HH:mm</code>)<br>
   * <i>and</i> a <b>timeZone</b><br>
   * <i>and</i> a <b>description</b><br>
   * <i>and</i> a <b>defaultTimeZone</b><br>
   * <i>and</i> a <code>false</code> <b>simulateError</b><br>
   * <i>and</i> a <b>model</b><br>
   * <b>when</b> invoking <code>appointmentsCreate</code><br>
   * <b>then</b> the method <i>should not</i> populate an error.
   * 
   * @see AppointmentController
   */
  @Test
  public void appointmentsCreateShouldNotPopulateAnError() {
    // Given an instance of AppointmentsController
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
    // and a false simulateError
    final String simulateError = Boolean.FALSE.toString();
    // and a model
    final Map<String, String> attributesMap = new HashMap<>();
    final Model model = this.getMockModel(attributesMap);
    // when invoking appointmentsCreate
    appointmentController.appointmentsCreate(date, time, timeZone, description,
      defaultTimeZone, simulateError, model);
    // then the method should not populate an error.
    final String errorParam = attributesMap.get(ERROR_PARAM);
    assertNull("appointmentsCreate should not populate an error.", errorParam);
  }

  private Model getMockModel(final Map<String, String> attributesMap) {
    final Model model = mock(Model.class);
    doAnswer(new Answer<Void>() {

      @Override
      public Void answer(final InvocationOnMock invocation) {
        final Object[] args = invocation.getArguments();
        attributesMap.put((String) args[0], (String) args[1]);
        return null;
      }

    }).when(model).addAttribute(anyString(), anyString());
    return model;
  }

}