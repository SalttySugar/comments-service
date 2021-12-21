package blog.comments.converter;

import blog.comments.dto.CommentDTO;
import blog.comments.model.Comment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public final class CommentToResponseCommentDTO implements Converter<Comment, CommentDTO> {
    @Override
    public CommentDTO convert(Comment source) {
        List<String> replies = source.getReplies()
                .stream()
                .map(Comment::getId)
                .collect(Collectors.toList());

        return CommentDTO.builder()
                .id(source.getId())
                .message(source.getMessage())
                .publishedOn(source.getPublishedOn())
                .updatedOn(source.getUpdatedOn())
                .recordId(source.getRecordId())
                .updatedOn(source.getUpdatedOn())
                .ownerId(source.getOwnerId())
                .replies(replies)
                .build();
    }
}
