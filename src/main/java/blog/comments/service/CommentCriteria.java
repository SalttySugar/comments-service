package blog.comments.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentCriteria {
    String ids;

    @JsonProperty("owner_id")
    String ownerId;

    @JsonProperty("record_id")
    String recordId;

    Date before;
    Date after;

}
