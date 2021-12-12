package blog.comments.controller;

import blog.comments.constant.API;
import blog.comments.dto.CreateCommentDTO;
import blog.comments.dto.ResponseCommentDTO;
import blog.comments.dto.UpdateCommentDTO;
import blog.comments.service.CommentsService;
import blog.comments.utils.ApplicationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(API.V1.PATH)
@RequiredArgsConstructor
public class CommentsController {
    private final CommentsService service;
    private final ApplicationConverter converter;

    @GetMapping
    Flux<ResponseCommentDTO> findAll() {
        return service.findAll().
                map(converter.convert(ResponseCommentDTO.class));
    }

    @GetMapping("/{id}")
    Mono<ResponseCommentDTO> findById(@PathVariable String id) {
        return service.findById(id)
                .map(converter.convert(ResponseCommentDTO.class));
    }


    @PostMapping
    Mono<ResponseCommentDTO> create(@RequestBody CreateCommentDTO dto) {
        return service.create(dto)
                .map(converter.convert(ResponseCommentDTO.class));
    }

    @PutMapping("/{id}")
    Mono<ResponseCommentDTO> update(@PathVariable String id, @RequestBody UpdateCommentDTO dto) {
        return service.update(id, dto)
                .map(converter.convert(ResponseCommentDTO.class));

    }

    @DeleteMapping("/{id}")
    Mono<Void> delete(@PathVariable String id) {
        return service.deleteById(id);
    }
}
