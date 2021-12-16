package blog.comments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel("Update comment")
public final class UpdateCommentDTO {
    @JsonProperty(required = true)
    String message;
}
