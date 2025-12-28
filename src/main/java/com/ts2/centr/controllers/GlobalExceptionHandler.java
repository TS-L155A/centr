package com.ts2.centr.controllers;


import com.ts2.centr.exceptions.HavkaValidationException;
import com.ts2.centr.exceptions.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException exception, Model model){
        model.addAttribute("message", exception.getMessage());
        return "error/404";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDbError(DataIntegrityViolationException e, Model model){
        model.addAttribute("message", e.getMessage());
        return "error/500";
    }
}
