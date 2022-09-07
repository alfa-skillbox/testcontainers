package ru.alfabank.skillbox.examples.moduletests.services.validation;

import feign.Request;
import feign.codec.DecodeException;
import lombok.Getter;

@Getter
class NotFoundDecodeException extends DecodeException {

    private JsonValidationResponse jsonValidationResponse;

    NotFoundDecodeException(int status, String message, Request request,
                                   JsonValidationResponse jsonValidationResponse) {
        super(status, message, request);
        this.jsonValidationResponse = jsonValidationResponse;
    }

    NotFoundDecodeException(int status, String message, Request request, Throwable cause,
                                   JsonValidationResponse jsonValidationResponse) {
        super(status, message, request, cause);
        this.jsonValidationResponse = jsonValidationResponse;
    }
}
