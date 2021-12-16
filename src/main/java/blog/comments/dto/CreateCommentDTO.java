package blog.comments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel("Create comment")
public final class CreateCommentDTO {
    @NotBlank(message = "field \"message\" is required and cannot be blank")
    String message;
    @NotBlank(message = "field \"published_id\" is required and cannot be blank")
    @JsonProperty("publisher_id")
    String publisherId;
    @NotBlank(message = "field \"record_id\" is required and cannot be blank")
    @JsonProperty("record_id")
    String recordId;
}
