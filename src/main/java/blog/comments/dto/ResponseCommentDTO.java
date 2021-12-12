package blog.comments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public final class ResponseCommentDTO {
    String id;
    String message;
    List<String> replies;
    @JsonProperty("published_on")
    Date publishedOn;
    @JsonProperty("updated_on")
    Date updatedOn;
    @JsonProperty("record_id")
    String recordId;
    @JsonProperty("publisher_id")
    String publisherId;
}
