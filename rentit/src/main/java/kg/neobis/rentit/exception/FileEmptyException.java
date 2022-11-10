package kg.neobis.rentit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileEmptyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileEmptyException(String message){
        super(message);
    }
}
