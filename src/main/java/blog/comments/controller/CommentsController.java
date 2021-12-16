package blog.comments.controller;

import blog.comments.constant.API;
import blog.comments.dto.CreateCommentDTO;
import blog.comments.dto.CommentDTO;
import blog.comments.dto.UpdateCommentDTO;
import blog.comments.service.CommentsService;
import blog.comments.utils.ApplicationConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(API.PATH)
@RequiredArgsConstructor
@Api(tags = "Comments")
public class CommentsController {
    private final CommentsService service;
    private final ApplicationConverter converter;

    @GetMapping
    @ApiOperation("Retrieve list of comments")
    Flux<CommentDTO> findAll() {
        return service.findAll().
                map(converter.convert(CommentDTO.class));
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve comment by id")
    Mono<CommentDTO> findById(@PathVariable String id) {
        return service.findById(id)
                .map(converter.convert(CommentDTO.class));
    }


    @PostMapping
    @ApiOperation("Create new comment")
    Mono<CommentDTO> create(@RequestBody CreateCommentDTO dto) {
        return service.create(dto)
                .map(converter.convert(CommentDTO.class));
    }

    @PutMapping("/{id}")
    @ApiOperation("Update comment by id")
    Mono<CommentDTO> update(@PathVariable String id, @RequestBody UpdateCommentDTO dto) {
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
