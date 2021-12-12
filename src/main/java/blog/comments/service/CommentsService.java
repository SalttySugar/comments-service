package blog.comments.service;

import blog.comments.dto.CreateCommentDTO;
import blog.comments.dto.UpdateCommentDTO;
import blog.comments.model.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentsService {
    Mono<Comment> findById(String id);
    Flux<Comment> findAll();
    Mono<Comment> create(CreateCommentDTO dto);
    Mono<Comment> update(String id, UpdateCommentDTO dto);
    Mono<Comment> addReplyToComment(String id, CreateCommentDTO dto);
    Mono<Void> deleteById(String id);
}