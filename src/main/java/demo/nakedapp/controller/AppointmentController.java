package demo.nakedapp.controller;

import demo.nakedapp.entity.Appointment;
import demo.nakedapp.repository.AppointmentRepository;

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

@Controller
public final class AppointmentController {

  @Autowired
  private AppointmentRepository repository;

  private final List<TimeZone> timeZones = new ArrayList<>();

  private static final String APPOINTMENTS_PARAM = "appointments";
  private static final String TIME_ZONES_PARAM = "timeZones";
  private static final String DEFAULT_TIME_ZOME_PARAM = "defaultTimeZone";

  private static final String APPOINMENTS_TEMPLATE = "appointments";

  private static final String TIME_ZONE_REGEX = "Etc/GMT([+-][1-9]\\d?)?";
  private static final Pattern TIME_ZONE_PATTERN =
    Pattern.compile(TIME_ZONE_REGEX);

  private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
    new SimpleDateFormat("yyyy-MM-dd HH':'mm");

  private static final Logger LOGGER =
    LoggerFactory.getLogger(AppointmentController.class);

  public AppointmentController() {
    super();
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

  @GetMapping("/appointments")
  public String appointments(
    @RequestParam(name="tz", required=false, defaultValue="Etc/GMT")
    final String defaultTimeZone,
    final Model model) {
    if (StringUtils.isBlank(defaultTimeZone)) {
      final String message = String.format("Illegal argument; "
        + "defaultTimeZone==%s", defaultTimeZone);
      throw new IllegalArgumentException(message);
    }
    final String message = String.format("appointments[defaultTimeZone=='%s']",
      defaultTimeZone);
    LOGGER.info(message);
    this.populateModel(model, defaultTimeZone);
    return APPOINMENTS_TEMPLATE;
  }

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
    final Model model) {
    if (StringUtils.isBlank(date) || StringUtils.isBlank(time)
      || StringUtils.isBlank(timeZone) || StringUtils.isBlank(description)
      || StringUtils.isBlank(defaultTimeZone)) {
      final String message = String.format("Illegal argument; date==%s, "
        + "time==%s, timeZone==%s, description==%s, defaultTimeZone==%s", date,
        time, timeZone, description, defaultTimeZone);
      throw new IllegalArgumentException(message);
    }
    String message = String.format("appointmentsCreate[date=='%s', time=='%s', "
      + "timeZone=='%s', description=='%s', defaultTimeZone=='%s']", date, time,
      timeZone, description, defaultTimeZone);
    LOGGER.info(message);
    final TimeZone timeZoneObject = TimeZone.getTimeZone(timeZone);
    SIMPLE_DATE_FORMAT.setTimeZone(timeZoneObject);
    final String dateTime = String.format("%s %s", date, time);
    try {
      final Date dateObject = SIMPLE_DATE_FORMAT.parse(dateTime);
      final Appointment appointment = new Appointment(dateObject, description);
      this.repository.save(appointment);
    } catch(final ParseException e) {
      message = e.getMessage();
      model.addAttribute("error", message);
      message = String.format("ParseException caught while attempting to parse "
        + "date and time. [dateTime=='%s']", dateTime);
      LOGGER.error(message, e);
    }
    this.populateModel(model, defaultTimeZone);
    return APPOINMENTS_TEMPLATE;
  }

  private void populateModel(final Model model, final String defaultTimeZone) {
    final Iterable<Appointment> appointments = this.repository.findAll();
    model.addAttribute(APPOINTMENTS_PARAM, appointments);
    model.addAttribute(TIME_ZONES_PARAM, this.timeZones);
    final TimeZone defaultTimeZoneObject =
      TimeZone.getTimeZone(defaultTimeZone);
    model.addAttribute(DEFAULT_TIME_ZOME_PARAM, defaultTimeZoneObject);
  }

}
