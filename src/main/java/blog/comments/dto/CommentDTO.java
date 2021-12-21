package blog.comments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ApiModel("Comment")
public final class CommentDTO {
    String id;
    String message;
    List<String> replies;
    @JsonProperty("published_on")
    LocalDateTime publishedOn;
    @JsonProperty("updated_on")
    LocalDateTime updatedOn;
    @JsonProperty("record_id")
    String recordId;
    @JsonProperty("owner_id")
    String ownerId;
}
