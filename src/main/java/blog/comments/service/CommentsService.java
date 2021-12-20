package blog.comments.service;

import blog.comments.dto.CreateCommentDTO;
import blog.comments.dto.UpdateCommentDTO;
import blog.comments.model.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentsService {
    Mono<Comment> findById(String id);
    Flux<Comment> findAll();
    Flux<Comment> findAll(CommentCriteria criteria);
    Mono<Comment> findOne(CommentCriteria criteria);
    Mono<Comment> create(CreateCommentDTO dto);
    Mono<Comment> update(String id, UpdateCommentDTO dto);
    Mono<Comment> addReply(String id, CreateCommentDTO dto);
    Flux<Comment> getReplies(String id);
    Mono<Comment> save(Comment comment);
    Mono<Boolean> existsById(String id);
    Mono<Void> deleteById(String id);
    Mono<Long> count();
    Mono<Long> count(CommentCriteria criteria);
}
