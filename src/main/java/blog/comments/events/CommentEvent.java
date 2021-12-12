package blog.comments.events;

import blog.comments.model.Comment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
public abstract class CommentEvent extends ApplicationEvent {
    private final Comment comment;
    private final Object source;

    public CommentEvent(Object source, Comment comment) {
        super(source);
        this.comment = comment;
        this.source = source;
    }
}
