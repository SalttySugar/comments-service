package blog.comments.exceptions;

import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends HttpException {
    public CommentNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, String.format("could not find comment with id: %s", id));
    }
}
