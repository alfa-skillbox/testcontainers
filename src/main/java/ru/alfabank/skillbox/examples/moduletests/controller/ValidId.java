package ru.alfabank.skillbox.examples.moduletests.controller;

import com.nimbusds.jose.Payload;

import javax.validation.Constraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotBlank(message = "Id is not valid")
@Pattern(regexp = "^[0-9]{1,3}$", message = "Id should contain only digits from 0 to 999")
@Target({FIELD, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface ValidId {
    String message() default "Id is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
