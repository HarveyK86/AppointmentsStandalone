package appointments.standalone.controller;

import appointments.standalone.entity.Appointment;
import appointments.standalone.repository.AppointmentRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * <code>AppointmentController</code> is a controller for the
 * {@link Appointment} entity that uses a provided
 * {@link AppointmentRepository}.
 * </p>
 * <p>
 * <code>AppointmentController</code> contains mappings for;
 * <ul>
 * <li><code>/appointments</code> - {@link #appointments(String, Model)}</li>
 * <li><code>/appointmentsCreate</code> -
 * {@link #appointmentsCreate(String, String, String, String, String, Model)</li>
 * </ul>
 * </p>
 * 
 * @see demo.nakedapp.entity.Appointment
 * @see demo.nakedapp.repository.AppointmentRepository
 * @see #appointments(String, Model)
 * @see #appointmentsCreate(String, String, String, String, String, Model)
 */
@Controller
public final class AppointmentController {

  private final AppointmentRepository appointmentRepository;

  private final List<TimeZone> timeZones = new ArrayList<>();

  private static final String APPOINTMENTS_PARAM = "appointments";
  private static final String TIME_ZONES_PARAM = "timeZones";
  private static final String DEFAULT_TIME_ZOME_PARAM = "defaultTimeZone";
  private static final String ERROR_PARAM = "error";

  private static final String APPOINMENTS_TEMPLATE = "appointments";

  private static final String TIME_ZONE_REGEX = "Etc/GMT([+-][1-9]\\d?)?";
  private static final Pattern TIME_ZONE_PATTERN =
    Pattern.compile(TIME_ZONE_REGEX);

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
    new SimpleDateFormat("yyyy-MM-dd HH':'mm");

  private static final Logger LOGGER =
    LoggerFactory.getLogger(AppointmentController.class);

  /**
   * <p>
   * Creates a new instance of <code>AppointmentController</code> using the
   * specified <b>appointmentRepository</b>.
   * </p>
   * <p>
   * Also populates the list of available <code>TimeZone</code>s.
   * </p>
   * 
   * @param appointmentRepository An instance of {@link AppointmentRepository}.
   *          This cannot be <code>null</code>.
   * 
   * @throws IllegalArgumentException If <b>appointmentRepository</b> is
   *          <code>null</code>.
   * 
   * @see demo.nakedapp.repository.AppointmentRepository
   */
  public AppointmentController(
    @Autowired
    final AppointmentRepository appointmentRepository
  ) {
    super();
    if (appointmentRepository == null) {
      final String message = String.format("Illegal argument; "
        + "appointmentRepository=={%s}", appointmentRepository != null);
      throw new IllegalArgumentException(message);
    }
    LOGGER.info("AppointmentController[appointmentRepository]");
    this.appointmentRepository = appointmentRepository;
    final String[] timeZoneIds = TimeZone.getAvailableIDs();
    Matcher matcher;
    TimeZone timeZone;
    for (final String timeZoneId : timeZoneIds) {
      matcher = TIME_ZONE_PATTERN.matcher(timeZoneId);
      if (matcher.matches()) {
        timeZone = TimeZone.getTimeZone(timeZoneId);
        this.timeZones.add(timeZone);
      }
    }
    Collections.sort(this.timeZones, new Comparator<TimeZone>() {

      @Override
      public int compare(final TimeZone timeZone1, final TimeZone timeZone2) {
        String timeZoneId = timeZone1.getID();
        Matcher matcher = TIME_ZONE_PATTERN.matcher(timeZoneId);
        matcher.find();
        String offsetString = matcher.group(1);
        final int offset1, offset2;
        if (offsetString == null) {
          offset1 = 0;
        } else {
          offset1 = Integer.parseInt(offsetString);
        }
        timeZoneId = timeZone2.getID();
        matcher = TIME_ZONE_PATTERN.matcher(timeZoneId);
        matcher.find();
        offsetString = matcher.group(1);
        if (offsetString == null) {
          offset2 = 0;
        } else {
          offset2 = Integer.parseInt(offsetString);
        }
        return offset2 - offset1;
      }

    });
  }

  /**
   * <p>
   * <code>appointments</code> is a mapping for <code>GET</code> requests to
   * <code>/appointments</code>.
   * </p>
   * <p>
   * <b>defaultTimeZone</b> is a URL parameter that specifies the ID of the
   * default <code>TimeZone</code>. The default time zone is used as the default
   * value of the time zone select box in the appointment creation form as well
   * as the time zone existing appointments will be displayed in.
   * </p>
   * <p>
   * <b>simulateError</b> is a URL parameter that specifies if a test Exception
   * should be thrown.
   * </p>
   * 
   * @param defaultTimeZone The ID of the default <code>TimeZone</code>; this is
   *          mapped to the <code>tz</code> URL parameter, if the URL parameter
   *          is not provided this will default to "Etc/GMT" for Greenwich Mean
   *          Time. This parameter cannot be <code>null</code>, empty or
   *          whitespace only.
   * @param simulateError Indicates if a test Exception should be thrown; this
   *          is mapped to the <code>e</code> URL parameter, if the URL
   *          parameter is not provided this will default to "false" to indicate
   *          that a test Exception should not be thrown. This parameter cannot
   *          be <code>null</code>, empty or whitespace only.
   * @param model The model returned to the <code>Thymeleaf</code> template.
   *          This parameter cannot be <code>null</code>.
   * 
   * @return The name of the <code>Thymeleaf</code> template to render upon
   *          successful execution.
   * 
   * @throws IllegalArgumentException If <b>model</b> is <code>null</code>.
   */
  @GetMapping("/appointments")
  public String appointments(
    @RequestParam(name="tz", required=false, defaultValue="Etc/GMT")
    final String defaultTimeZone,
    @RequestParam(name="e", required=false, defaultValue="false")
    final String simulateError,
    final Model model) {
    if (model == null) {
      throw new IllegalArgumentException("Illegal argument; no model "
        + "specified");
    }
    try {
      if (StringUtils.isBlank(defaultTimeZone)
        || StringUtils.isBlank(simulateError)
        || Boolean.parseBoolean(simulateError)) {
        final String message = String.format("Illegal argument; "
          + "defaultTimeZone==%s, simulateError==%s", defaultTimeZone,
          simulateError);
        throw new IllegalArgumentException(message);
      }
      final String message = String.format("appointments["
        + "defaultTimeZone=='%s']", defaultTimeZone);
      LOGGER.info(message);
    } catch(final Exception e) {
      String message = e.getMessage();
      model.addAttribute(ERROR_PARAM, message);
      message = String.format("Exception caught by /appointments endpoint: "
        + "%s", message);
      if (e instanceof IllegalArgumentException) {
        LOGGER.warn(message);
      } else {
        LOGGER.warn(message, e);
      }
    } finally {
      try {
        this.populateModel(model, defaultTimeZone);
      } catch(final Exception e) {
        LOGGER.error("Unable to populate model", e);
      }
    }
    return APPOINMENTS_TEMPLATE;
  }

  /**
   * <p>
   * <code>appointmentsCreate</code> is a mapping for <code>POST</code> requests
   * to <code>/appointments/create</code>.
   * </p>
   * <p>
   * <code>appointmentsCreate</code> creates a new instance of
   * {@link Appointment} using the specified form parameters.
   * </p>
   * <b>defaultTimeZone</b> is a URL parameter that specifies the ID of the
   * default <code>TimeZone</code>. The default time zone is used as the default
   * value of the time zone select box in the appointment creation form as well
   * as the time zone existing appointments will be displayed in.
   * </p>
   * <p>
   * <b>simulateError</b> is a URL parameter that specifies if a test Exception
   * should be thrown.
   * </p>
   * 
   * @param date The date for the new instance of <code>Appointment</code>; this
   *          is mapped to the <code>date</code> form parameter and must be in
   *          the format <code>dd/MM/yyyy</code>. This parameter cannot be
   *          <code>null</code>, empty or whitespace only.
   * @param time The time of day for the new instance of
   *          <code>Appointment</code>; this is mapped to the <code>time</code>
   *          form parameter and must be in the format <code>HH:mm</code>. This
   *          parameter cannot be <code>null</code>, empty or whitespace only.
   * @param timeZone The ID of the <code>TimeZone</code> for the <b>time</b> for
   *          the new instance of <code>Appointment</code>; this is mapped to
   *          the <code>timeZone</code> form parameter. This parameter cannot be
   *          <code>null</code>, empty or whitesapce only.
   * @param description The description for the new instance of
   *          <code>Appointment</code>; this is mapped to the
   *          <code>description</code> form parameter. This parameter cannot be
   *          <code>null</code>, empty or whitespace only.
   * @param defaultTimeZone The ID of the default <code>TimeZone</code>; this is
   *          mapped to the <code>tz</code> URL parameter, if the URL parameter
   *          is not provided this will default to "Etc/GMT" for Greenwich Mean
   *          Time. This parameter cannot be <code>null</code>, empty or
   *          whitespace only.
   * @param simulateError Indicates if a test Exception should be thrown; this
   *          is mapped to the <code>e</code> URL parameter, if the URL
   *          parameter is not provided this will default to "false" to indicate
   *          that a test Exception should not be thrown. That parameter cannot
   *          be <code>null</code>, empty or whitespace only.
   * @param model The model returned to the <code>Thymeleaf</code> template.
   *          This parameter cannot be <code>null</code>.
   * 
   * @return The name of the <code>Thymeleaf</code> template to render upon
   *          successful execution.
   * 
   * @throws IllegalArgumentException If <b>model</b> is <code>null</code>.
   * 
   * @see demo.nakedapp.entity.Appointment
   */
  @PostMapping("/appointments/create")
  public String appointmentsCreate(
    @RequestParam(name="date", required=true)
    final String date,
    @RequestParam(name="time", required=true)
    final String time,
    @RequestParam(name="timeZone", required=true)
    final String timeZone,
    @RequestParam(name="description", required=true)
    final String description,
    @RequestParam(name="tz", required=false, defaultValue="Etc/GMT")
    final String defaultTimeZone,
    @RequestParam(name="e", required=false, defaultValue="false")
    final String simulateError,
    final Model model) {
    if (model == null) {
      throw new IllegalArgumentException("Illegal argument; no model "
        + "specified");
    }
    try {
      if (StringUtils.isBlank(date) || StringUtils.isBlank(time)
        || StringUtils.isBlank(timeZone) || StringUtils.isBlank(description)
        || StringUtils.isBlank(defaultTimeZone)
        || StringUtils.isBlank(simulateError)
        || Boolean.parseBoolean(simulateError)) {
        final String message = String.format("Illegal argument; date==%s, "
          + "time==%s, timeZone==%s, description==%s, defaultTimeZone==%s, "
          + "simulateError==%s", date, time, timeZone, description,
          defaultTimeZone, simulateError);
        throw new IllegalArgumentException(message);
      }
      final String message = String.format("appointmentsCreate[date=='%s', "
        + "time=='%s', timeZone=='%s', description=='%s', "
        + "defaultTimeZone=='%s']", date, time, timeZone, description,
        defaultTimeZone);
      LOGGER.info(message);
      final TimeZone timeZoneObject = TimeZone.getTimeZone(timeZone);
      SIMPLE_DATE_FORMAT.setTimeZone(timeZoneObject);
      final String dateTime = String.format("%s %s", date, time);
      final Date dateObject = SIMPLE_DATE_FORMAT.parse(dateTime);
      final Appointment appointment = new Appointment(dateObject, description);
      this.appointmentRepository.save(appointment);
    } catch (final Exception e) {
      String message = e.getMessage();
      model.addAttribute(ERROR_PARAM, message);
      message = String.format("Exception caught by /appointmentsCreate "
        + "endpoint: %s", message);
      if (e instanceof IllegalArgumentException
        || e instanceof ParseException) {
        LOGGER.warn(message);
      } else {
        LOGGER.warn(message, e);
      }
    } finally {
      try {
        this.populateModel(model, defaultTimeZone);
      } catch (final Exception e) {
        LOGGER.error("Unable to populate model", e);
      }
    }
    return APPOINMENTS_TEMPLATE;
  }

  private void populateModel(final Model model, final String defaultTimeZone) {
    final Iterable<Appointment> appointments =
      this.appointmentRepository.findAll();
    model.addAttribute(APPOINTMENTS_PARAM, appointments);
    model.addAttribute(TIME_ZONES_PARAM, this.timeZones);
    if (StringUtils.isNotBlank(defaultTimeZone)) {
      final TimeZone defaultTimeZoneObject =
        TimeZone.getTimeZone(defaultTimeZone);
      model.addAttribute(DEFAULT_TIME_ZOME_PARAM, defaultTimeZoneObject);
    }
  }

}
