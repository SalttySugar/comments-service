package blog.comments.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel("Update comment")
public final class UpdateCommentDTO {
    @NotBlank(message = "field \"message\" is required and cannot be blank")
    String message;
}
