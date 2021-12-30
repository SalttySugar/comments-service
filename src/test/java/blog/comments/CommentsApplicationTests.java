package blog.comments;

import blog.comments.constant.API;
import blog.comments.constant.Headers;
import blog.comments.dto.CommentDTO;
import blog.comments.dto.CreateCommentDTO;
import blog.comments.dto.UpdateCommentDTO;
import blog.comments.model.Comment;
import blog.comments.service.CommentCriteria;
import blog.comments.service.CommentsService;
import blog.comments.utils.ApplicationConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest
@AutoConfigureWebTestClient
class CommentsApplicationTests extends BaseIntegrationTest {
    public static abstract class Helpers {
        public static MultiValueMap<String, String> convertToMultiValueMap(Object obj) {
            var mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.setDateFormat(new ISO8601DateFormat());
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            Map<String, String> maps = mapper.convertValue(obj, new TypeReference<>() {
            });
            parameters.setAll(maps);

            return parameters;
        }
    }

    @Autowired
    CommentsService commentsService;

    @Autowired
    ApplicationConverter converter;

    @Autowired
    WebTestClient client;

    @AfterEach
    void tearDown() {
        commentsService.deleteAll().block();
    }

    @Test
    void contextLoads() {

    }


    @Nested
    @SpringBootTest
    class CommentsApplicationCrudTests {
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
                    .jsonPath("$.id").value(is(comment.getId()))
                    .jsonPath("$.message").value(is(comment.getMessage()))
                    .jsonPath("$.owner_id").value(is(comment.getOwnerId()))
                    .jsonPath("$.record_id").value(is(comment.getRecordId()))
                    .jsonPath("$.published_on").value(not(empty()))
                    .jsonPath("$.updated_on").value(is(comment.getUpdatedOn()));
        }

        @Test
        void shouldReturnPaginatedCollectionOfComments() {
            for (int i = 0; i < 20; i++) {
                commentsService.save(Comment.builder().build()).block();
            }


            client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(API.PATH)
                            .queryParam("offset", 0)
                            .queryParam("limit", 15)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().value(Headers.TOTAL_COUNT, is("20"))
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$").value(hasSize(15));


            client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(API.PATH)
                            .queryParam("offset", 15)
                            .queryParam("limit", 15)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().value(Headers.TOTAL_COUNT, is("20"))
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$").value(hasSize(5));
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
                    .jsonPath("$.owner_id").value(equalTo(dto.getPublisherId()))
                    .jsonPath("$.record_id").value(equalTo(dto.getRecordId()))
                    .jsonPath("$.published_on").exists()
                    .jsonPath("$.updated_on").value(nullValue());
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
                    .jsonPath("$.id").value(is(comment.getId()))
                    .jsonPath("$.message").value(is(dto.getMessage()))
                    .jsonPath("$.owner_id").value(is(comment.getOwnerId()))
                    .jsonPath("$.record_id").value(is(comment.getRecordId()))
                    .jsonPath("$.updated_on").value(not(empty()));

        }


        @Test
        void shouldDeleteCommentById() {
            var comment = commentsService.save(Comment.builder()
                    .message("test comment")
                    .ownerId("test_publisher_id")
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
            assert comment != null;
            client.put()
                    .uri(API.PATH + "/" + comment.getId())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(dto))
                    .exchange()
                    .expectStatus().isBadRequest();
        }

    }



    @Nested
    @SpringBootTest
    class CommentsApplicationFiltersTests {
        @Test
        void shouldReturnCollectionOfCommentsBeforeSpecificDate() {
            //TODO: add date check
            commentsService.save(Comment.builder()
                    .publishedOn(LocalDateTime.now().minusWeeks(1))
                    .build()
            ).block();

            commentsService.save(Comment.builder()
                    .publishedOn(LocalDateTime.now().minusWeeks(1))
                    .build()
            ).block();


            commentsService.save(Comment.builder()
                    .publishedOn(LocalDateTime.now())
                    .build()
            ).block();

            commentsService.save(Comment.builder()
                    .publishedOn(LocalDateTime.now())
                    .build()
            ).block();


            var filters = CommentCriteria.builder()
                    .before(LocalDateTime.now().minusDays(1))
                    .build();
            client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(API.PATH)
                            .queryParams(Helpers.convertToMultiValueMap(filters))
                            .build()
                    )
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().value(Headers.TOTAL_COUNT, is("2"))
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$").value(hasSize(2));

        }

        @Test
        void shouldReturnCollectionOfCommentsAfterSpecificDate() {
            //TODO: add date check
            commentsService.save(Comment.builder()
                    .publishedOn(LocalDateTime.now().minusWeeks(3))
                    .build()
            ).block();

            commentsService.save(Comment.builder()
                    .publishedOn(LocalDateTime.now().minusWeeks(3))
                    .build()
            ).block();


            commentsService.save(Comment.builder()
                    .publishedOn(LocalDateTime.now().minusWeeks(1))
                    .build()
            ).block();

            commentsService.save(Comment.builder()
                    .publishedOn(LocalDateTime.now().minusWeeks(1))
                    .build()
            ).block();


            var filters = CommentCriteria.builder()
                    .after(LocalDateTime.now().minusWeeks(2))
                    .build();
            client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(API.PATH)
                            .queryParams(Helpers.convertToMultiValueMap(filters))
                            .build()
                    )
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().value(Headers.TOTAL_COUNT, is("2"))
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$").value(hasSize(2));
        }
        @Test
        void shouldReturnCollectionOfCommentsCreatedBySpecificOwner() {
            List<Comment> comments = new ArrayList<>();

            commentsService.save(Comment.builder()
                    .ownerId("1")
                    .build())
                    .doOnNext(comments::add)
                    .block();


            commentsService.save(Comment.builder()
                    .ownerId("1")
                    .build())
                    .doOnNext(comments::add)
                    .block();


            commentsService.save(Comment.builder()
                    .ownerId("2")
                    .build())
                    .doOnNext(comments::add)
                    .block();


            commentsService.save(Comment.builder()
                    .ownerId("2")
                    .build())
                    .doOnNext(comments::add)
                    .block();

            var match = comments
                    .stream()
                    .filter(comment -> comment.getOwnerId().equals("2"))
                    .map(converter.convert(CommentDTO.class))
                    .map(CommentDTO::getId)
                    .collect(Collectors.toList());


            var params = Helpers.convertToMultiValueMap(CommentCriteria.builder()
                    .owner("2")
                    .build());

            client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(API.PATH)
                            .queryParams(params)
                            .build()
                    )
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().value(Headers.TOTAL_COUNT, is("2"))
                    .expectBody()
                    .jsonPath("$").value(hasSize(match.size()))
                    .jsonPath("$[*].id").value((is(match)));

        }

        @Test
        void shouldReturnCollectionOfCommentsThatBelongsToSpecificRecord() {
            commentsService.save(Comment.builder()
                    .ownerId("1")
                    .recordId("1")
                    .build()
            )
                    .block();

            commentsService.save(Comment.builder()
                    .ownerId("1")
                    .recordId("1")
                    .build()
            )
                    .block();

            commentsService.save(Comment.builder()
                    .ownerId("1")
                    .recordId("2")
                    .build()
            )
                    .block();


            commentsService.save(Comment.builder()
                    .ownerId("1")
                    .recordId("2")
                    .build()
            )
                    .block();


            commentsService.save(Comment.builder()
                    .ownerId("1")
                    .recordId("2")
                    .build()
            )
                    .block();


            var query = Helpers.convertToMultiValueMap(CommentCriteria.builder()
                    .record("2")
                    .build());

            client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(API.PATH)
                            .queryParams(query)
                            .build()
                    )
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().value(Headers.TOTAL_COUNT, is("3"))
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$").value(hasSize(3))
                    .jsonPath("$[0].record_id").value(is("2"))
                    .jsonPath("$[1].record_id").value(is("2"))
                    .jsonPath("$[2].record_id").value(is("2"));
        }

    }

}
