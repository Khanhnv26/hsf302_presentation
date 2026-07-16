package org.ats.exceptions;

import jakarta.persistence.NoResultException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class AppExceptionHandling {

    @ExceptionHandler(NoResultException.class)
    public ModelAndView noResultObject(NoResultException ex){
        ModelAndView mv = new ModelAndView();
        mv.addObject("errorMessage", "Invalid email or password");
        mv.setViewName("auths/login");
        return mv;

    }

    @ExceptionHandler(JobNotFoundException.class)
    public  ModelAndView jobNotFound(JobNotFoundException ex){
        ModelAndView mv = new ModelAndView();
        mv.addObject("errorMessage", ex.getMessage());
        mv.setViewName("errors/404");
        return mv;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public  ModelAndView badRequest(MissingServletRequestParameterException ex){
        ModelAndView mv = new ModelAndView();
        mv.addObject("errorMessage", ex.getMessage());
        mv.setViewName("errors/400");
        return mv;
    }
}
