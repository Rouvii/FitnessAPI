package dat.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Message(int status, String message, String timestamp) {


    public Message(int status, String message) {
        this(status, message, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
    }

}