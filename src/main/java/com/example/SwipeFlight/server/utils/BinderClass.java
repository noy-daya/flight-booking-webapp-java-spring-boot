package com.example.SwipeFlight.server.utils;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * The class populates command and form object arguments of annotated handler methods.
 */
@ControllerAdvice
public class BinderClass {

	
	/**
	 * The method performs data binding from web request parameters to JavaBean objects.
	 * (before they are reaching the controller -> before binding the value to the parameter received in controller)
	 * It registers a custom editor for converting strings of different variable types.
	 * @param binder
	 */
	@InitBinder // annotation, which allows the customization of web request parameter binding.
    public void initBinder(WebDataBinder binder) throws IllegalArgumentException{
        // check for Date
		// ========================
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                } else {
                    try {
                        setValue(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(text).getTime()));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("Invalid date format. Please use dd.MM.yyyy");
                        
                    }
                }
            }
        });
	    // check for Time
     // ========================
        binder.registerCustomEditor(Time.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException
            {
                if (text == null || text.isEmpty())
                    setValue(null);
                else // we won't reach this point because when the user picks a date, the format is forced
                {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        sdf.setLenient(false);
                        setValue(new Time(sdf.parse(text).getTime()));
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("Invalid time format. Please use HH:mm:ss");
                    }
                }
            }
        });
        
        // check for Duration
        // ========================
        binder.registerCustomEditor(Duration.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                    return;
                }
                else if(!isValidDurationFormat(text)) {
                    setValue(Duration.ZERO);
                    return;
                }
                
                // parse input and convert it to a Duration object
                String[] parts = text.split(":");
                long hours = Long.parseLong(parts[0]);
                long minutes = Long.parseLong(parts[1]);
                Duration duration = Duration.ofHours(hours).plusMinutes(minutes);
                setValue(duration);
            }

            // validate duration format (HH:mm)
            private boolean isValidDurationFormat(String text) {
            	Pattern pattern = Pattern.compile("^([0-9]|[0-9][0-9]):([0-5][0-9])$");
                Matcher matcher = pattern.matcher(text);
                return matcher.matches();
            }

            @Override
            public String getAsText() {
                Duration duration = (Duration) getValue();
                if (duration == null) {
                    return ""; // Return empty string if Duration is null
                }
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;

                return String.format("%02d:%02d", hours, minutes);
            }
        });
        
        // check for double
        // ========================
        binder.registerCustomEditor(double.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isEmpty()) {
                    setValue(0.0); // Set the value to 0 if the text is empty
                } else {
                    try {
                        setValue(Double.parseDouble(text));
                    } catch (NumberFormatException e) {
                    	setValue(0.0);
                    }
             
                }
            }
        });
    }
}
