package blog.comments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public final class CreateCommentDTO {
    @JsonProperty(required = true)
    String message;
    @JsonProperty( value = "publisher_id", required = true)
    String publisherId;
    @JsonProperty( value = "record_id", required = true)
    String recordId;
}
