package blog.comments.events;

import blog.comments.model.Comment;

public final class CommentUpdatedEvent extends CommentEvent {
    public CommentUpdatedEvent(Object source, Comment comment) {
        super(source, comment);
    }
}
