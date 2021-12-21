package blog.comments.controller;

import blog.comments.constant.API;
import blog.comments.constant.Headers;
import blog.comments.dto.CommentDTO;
import blog.comments.dto.CreateCommentDTO;
import blog.comments.dto.UpdateCommentDTO;
import blog.comments.service.CommentCriteria;
import blog.comments.service.CommentsService;
import blog.comments.utils.ApplicationConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(API.PATH)
@RequiredArgsConstructor
@Validated
@Api(tags = "Comments")
public class CommentsController {
    private final CommentsService service;
    private final ApplicationConverter converter;

    @GetMapping
    @ApiOperation("Retrieve list of comments")
    Mono<ResponseEntity<List<CommentDTO>>> findAll(
            CommentCriteria criteria,
            @RequestParam(required = false, defaultValue = "0") Long offset,
            @RequestParam(required = false, defaultValue = "10") Long limit
    ) {
        return Mono.just(ResponseEntity.ok())
                .flatMap(response -> Optional.of(criteria)
                        .map(service::count)
                        .orElseGet(service::count)
                        .map(total -> response.header(Headers.TOTAL_RECORDS, String.valueOf(total))))
                .flatMap(response -> Optional.of(criteria)
                        .map(service::findAll)
                        .orElseGet(service::findAll)
                        .skip(offset)
                        .take(limit)
                        .map(converter.convert(CommentDTO.class))
                        .collectList()
                        .map(response::body)
                );

    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve comment by id")
    Mono<CommentDTO> findById(@PathVariable String id) {
        return service.findById(id)
                .map(converter.convert(CommentDTO.class));

    }


    @PostMapping
    @ApiOperation("Create new comment")
    Mono<CommentDTO> create(@RequestBody @Valid CreateCommentDTO dto) {
        return service.create(dto)
                .map(converter.convert(CommentDTO.class));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update comment by id")
    Mono<CommentDTO> update(@PathVariable String id, @RequestBody @Valid UpdateCommentDTO dto) {
        return service.update(id, dto)
                .map(converter.convert(CommentDTO.class));

    }


    @DeleteMapping("/{id}")
    @ApiOperation("Delete comment by id")
    Mono<Void> delete(@PathVariable String id) {
        return service.deleteById(id);
    }


    // === REPLIES === //
    @PostMapping("/{id}/replies")
    Mono<CommentDTO> createReply(@PathVariable String id, @RequestBody CreateCommentDTO dto) {
        return service.addReply(id, dto)
                .map(converter.convert(CommentDTO.class));
    }

    @GetMapping("/{id}/replies")
    Flux<CommentDTO> getReplies(@PathVariable String id) {
        return service.getReplies(id)
                .map(converter.convert(CommentDTO.class));
    }
}
