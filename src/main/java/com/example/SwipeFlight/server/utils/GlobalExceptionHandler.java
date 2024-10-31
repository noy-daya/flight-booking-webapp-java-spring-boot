package com.example.SwipeFlight.server.utils;

import org.springframework.context.ApplicationContextException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.SwipeFlight.server.utils.custom_exceptions.RequestAttributeNotFoundException;
import com.example.SwipeFlight.server.utils.custom_exceptions.SessionAttributeNotFoundException;
import com.example.SwipeFlight.server.utils.custom_exceptions.SessionIsInvalidException;

/**
 * The class consists methods for handling different exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handler for ApplicationContextException
     * @param e - the exception
     * @param model - to pass attributes to the view.
     * @return the URL of the page which should be rendered. (in that case: error page)
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public String handleBadSqlGrammarException(BadSqlGrammarException e, Model model) {
        model.addAttribute("error_message", e.getClass() + ": " + e.getMessage());
        return "error"; // display page
    }
    
    /**
     * Handler for MissingServletRequestParameterException
     * @param e - the exception
     * @param model - to pass attributes to the view.
     * @return the URL of the page which should be rendered. (in that case: error page)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, Model model) {
    	model.addAttribute("error_message", ex.getClass() + ": "
    				+ "Required request parameter '" + ex.getParameterName() + "' is missing.");
        return "error"; // display page
    }
    
    /**
     * Handler for RequestAttributeNotFoundException (appears in package utils.custom_exceptions):
     * @param e - the exception
     * @param model - to pass attributes to the view.
     * @return the URL of the page which should be rendered. (in that case: error page)
     */
    @ExceptionHandler(RequestAttributeNotFoundException.class)
    public String handleRequestAttributeNotFoundException(RequestAttributeNotFoundException e, Model model) {
    	model.addAttribute("error_message", e.getClass() + ": " + e.getMessage());
        return "error"; // display page
    }
    
    /**
     * Handler for SessionAttributeNotFoundException (appears in package utils.custom_exceptions):
     * @param e - the exception
     * @param model - to pass attributes to the view.
     * @return the URL of the page which should be rendered. (in that case: error page)
     */
    @ExceptionHandler(SessionAttributeNotFoundException.class)
    public String handleSessionAttributeNotFoundException(SessionAttributeNotFoundException e, Model model) {
    	model.addAttribute("error_message", e.getClass() + ": " + e.getMessage());
        return "error"; // display page
    }
    
    /**
     * Handler for SessionIsInvalidException (appears in package utils.custom_exceptions):
     * @param e - the exception
     * @param model - to pass attributes to the view.
     * @return the URL of the page which should be rendered. (in that case: error page)
     */
    @ExceptionHandler(SessionIsInvalidException.class)
    public String handleSessionIsInvalidException(SessionIsInvalidException e, Model model) {
    	model.addAttribute("error_message", e.getClass() + ": " + e.getMessage());
        return "error"; // display page
    }
    
    /**
     * Handler for IllegalArgumentException
     * @param e - the exception
     * @param model - to pass attributes to the view.
     * @return the URL of the page which should be rendered. (in that case: error page)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        model.addAttribute("error_message", e.getClass() + ": " + e.getMessage());
        return "error"; // display page
    }
}
