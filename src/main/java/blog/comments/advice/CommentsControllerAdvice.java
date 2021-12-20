package blog.comments.advice;

import blog.comments.exceptions.CommentNotFoundException;
import blog.comments.exceptions.HttpException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommentsControllerAdvice {
    @ExceptionHandler(HttpException.class)
    ResponseEntity<Object> exceptionHandler(CommentNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
