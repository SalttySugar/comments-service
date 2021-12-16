package blog.comments;

import blog.comments.constant.API;
import blog.comments.dto.CreateCommentDTO;
import blog.comments.dto.UpdateCommentDTO;
import blog.comments.model.Comment;
import blog.comments.service.CommentsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.test.StepVerifier;

import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest
@AutoConfigureWebTestClient
class CommentsApplicationTests extends BaseIntegrationTest {
    @Autowired
    CommentsService commentsService;

    @Autowired
    WebTestClient client;


    @Test
    void contextLoads() {
    }


    @Test
    void shouldReturnCommentById() {
        var comment = commentsService.create(CreateCommentDTO.builder()
                .message("test comment")
                .publisherId("test_publisher_id")
                .recordId("test_record_id")
                .build())
                .block();

        assert comment != null;
        client.get()
                .uri(API.PATH + "/" + comment.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").value(equalTo(comment.getId()))
                .jsonPath("$.message").value(equalTo(comment.getMessage()))
                .jsonPath("$.publisher_id").value(equalTo(comment.getPublisherId()))
                .jsonPath("$.record_id").value(equalTo(comment.getRecordId()))
                .jsonPath("$.published_on").value(equalTo(comment.getPublishedOn()))
                .jsonPath("$.updated_on").value(equalTo(comment.getUpdatedOn()));
    }


    @Test
    void shouldReturn404WhenCommentDoesNotExists() {
        var uri = API.PATH + "/" + "sure_that_this_does_not_exists";
        var dto = new UpdateCommentDTO("valid comment message");
        client.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

        client.put()
                .uri(uri)
                .body(BodyInserters.fromValue(dto))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void shouldCreateNewCommentAndThenReturnIt() {
        var dto = CreateCommentDTO.builder()
                .message("test comment")
                .publisherId("test_publisher_id")
                .recordId("test_record_id")
                .build();

        client.post()
                .uri(API.PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.message").value(equalTo(dto.getMessage()))
                .jsonPath("$.publisher_id").value(equalTo(dto.getPublisherId()))
                .jsonPath("$.record_id").value(equalTo(dto.getRecordId()))
                .jsonPath("$.published_on").exists()
                .jsonPath("$.updated_on").exists();
    }

    @Test
    void shouldUpdateCommentAndThenReturnIt() {
        var dto = UpdateCommentDTO.builder()
                .message("hello, world!")
                .build();

        var comment = commentsService.create(CreateCommentDTO.builder()
                .message("test comment")
                .publisherId("test_publisher_id")
                .recordId("test_record_id")
                .build())
                .block();

        assert comment != null;
        client.put()
                .uri(API.PATH + "/" + comment.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").value(equalTo(comment.getId()))
                .jsonPath("$.message").value(equalTo(dto.getMessage()))
                .jsonPath("$.publisher_id").value(equalTo(comment.getPublisherId()))
                .jsonPath("$.record_id").value(equalTo(comment.getRecordId()))
                .jsonPath("$.published_on").value(equalTo(comment.getPublishedOn()))
                .jsonPath("$.updated_on").exists();

    }


    @Test
    void shouldDeleteCommentById() {
        var comment = commentsService.save(Comment.builder()
                .message("test comment")
                .publisherId("test_publisher_id")
                .recordId("test_record_id")
                .build())
                .block();

        assert comment != null;
        client.delete()
                .uri(API.PATH + "/" + comment.getId())
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseBody().blockFirst();

        StepVerifier.create(commentsService.existsById(comment.getId()).log())
                .expectNext(false)
                .verifyComplete();
    }


    @Test
    void shouldReturn400IfCreateCommentDTOIsInvalid() {
        var dto = new CreateCommentDTO();

        client.post()
                .uri(API.PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    void shouldReturn400IfUpdateCommentDTOIsInvalid() {
        var comment = commentsService.create(CreateCommentDTO.builder()
                .recordId("test_record_id")
                .message("test_message")
                .publisherId("test_publisher_id")
                .build()).block();

        var dto = new UpdateCommentDTO();
        client.put()
                .uri(API.PATH + "/" + comment.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(dto))
                .exchange()
                .expectStatus().isBadRequest();
    }
}
