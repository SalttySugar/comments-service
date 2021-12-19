package blog.comments.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class HttpException extends  RuntimeException{
    protected final HttpStatus status;
    protected final String message;
}
