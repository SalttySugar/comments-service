package blog.comments.errors;

import org.springframework.http.HttpStatus;

public class CommentNotFound extends HttpError{
    public CommentNotFound(String id) {
        super(HttpStatus.NOT_FOUND, String.format("could not find comment with id: %s", id));
    }
}
