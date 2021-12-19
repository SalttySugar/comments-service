package blog.comments.service.impl;

import blog.comments.dto.CreateCommentDTO;
import blog.comments.dto.UpdateCommentDTO;
import blog.comments.exceptions.CommentNotFoundException;
import blog.comments.events.CommentCreatedEvent;
import blog.comments.events.CommentDeletedEvent;
import blog.comments.events.CommentUpdatedEvent;
import blog.comments.model.Comment;
import blog.comments.repository.CommentsRepository;
import blog.comments.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository repository;
    private final ApplicationEventPublisher publisher;


    @Override
    public Mono<Comment> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new CommentNotFoundException(id)));
    }

    @Override
    public Flux<Comment> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Comment> create(CreateCommentDTO dto) {
        return Mono.just(dto)
                .map(this::createCommentFromDto)
                .flatMap(repository::save)
                .doOnNext(comment -> publisher.publishEvent(new CommentCreatedEvent(this, comment)));
    }

    @Override
    public Mono<Comment> update(String id, UpdateCommentDTO dto) {
        return repository.findById(id)
                .map(updateCommentFromDto(dto))
                .doOnNext(comment -> publisher.publishEvent(new CommentUpdatedEvent(this, comment)));
    }

    @Override
    public Mono<Comment> addReply(String id, CreateCommentDTO dto) {
        return repository.findById(id)
                .flatMap(comment ->
                        Mono.just(dto)
                                .flatMap(this::create)
                                .map(c -> {
                                    comment.getReplies().add(c);
                                    return comment;
                                })
                );

    }

    @Override
    public Flux<Comment> getReplies(String id) {
        return repository.findById(id)
                .flatMapIterable(Comment::getReplies);
    }

    @Override
    public Mono<Comment> save(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public Mono<Boolean> existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.findById(id)
                .doOnNext(comment -> publisher.publishEvent(new CommentDeletedEvent(this, comment)))
                .flatMap(repository::delete);
    }


    // ========= ----- HELPERS ----- ========= //
    protected Function<Comment, Comment> updateCommentFromDto(UpdateCommentDTO dto) {
        return comment -> {
            comment.setMessage(dto.getMessage());
            return comment;
        };
    }

    protected Flux<Comment> getRepliesFromIdsCollection(Collection<String> ids) {
        return Flux.fromStream(ids.stream())
                .flatMap(repository::findById);
    }


    protected Comment createCommentFromDto(CreateCommentDTO d) {
        return Comment.builder()
                .message(d.getMessage())
                .publisherId(d.getPublisherId())
                .recordId(d.getRecordId())
                .publishedOn(new Date())
                .build();
    }
}
