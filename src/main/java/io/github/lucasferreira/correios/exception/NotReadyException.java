package io.github.lucasferreira.correios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE, reason = "Serviço em instalação, aguarde 3 a 5 min!")
public class NotReadyException extends RuntimeException {
    public NotReadyException(String message) {
        super(message);
    }
}
