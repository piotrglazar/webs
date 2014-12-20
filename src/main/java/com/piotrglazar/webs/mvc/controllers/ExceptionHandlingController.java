package com.piotrglazar.webs.mvc.controllers;

import com.piotrglazar.webs.business.exceptions.MoneyTransferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.lang.invoke.MethodHandles;

@ControllerAdvice
public class ExceptionHandlingController {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExceptionHandler(MoneyTransferException.class)
    public ModelAndView handleMoneyTransferException(final MoneyTransferException exception) {
        LOG.error("Failed to transfer money", exception);

        final ModelAndView modelAndView = new ModelAndView("verboseError");

        modelAndView.addObject("message", exception.getMessage());

        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(final Exception exception) {
        LOG.error("Generic exception occurred", exception);

        return "error";
    }

}
