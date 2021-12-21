package blog.comments.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document
public class Comment {
    @Id
    String id;
    String message;
    @Field("record_id")
    String recordId;
    @Field("owner_id")
    String ownerId;
    @Field("published_on")
    LocalDateTime publishedOn;
    @Field("updated_on")
    LocalDateTime updatedOn;
}
