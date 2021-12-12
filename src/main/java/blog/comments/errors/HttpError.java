package blog.comments.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class HttpError  extends  RuntimeException{
    protected final HttpStatus status;
    protected final String message;
}
