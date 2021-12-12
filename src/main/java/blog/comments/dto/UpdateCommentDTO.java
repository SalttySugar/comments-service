package blog.comments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public final class UpdateCommentDTO {
    @JsonProperty(required = true)
    String message;

    @Builder.Default
    List<String> replies = new ArrayList<>();
}
