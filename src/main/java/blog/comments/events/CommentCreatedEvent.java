package blog.comments.events;

import blog.comments.model.Comment;

public final class CommentCreatedEvent extends CommentEvent {
    public CommentCreatedEvent(Object source, Comment comment) {
        super(source, comment);
    }
}
