package blog.comments.events;

import blog.comments.model.Comment;

public final class CommentDeletedEvent extends CommentEvent {
    public CommentDeletedEvent(Object source, Comment comment) {
        super(source, comment);
    }
}
