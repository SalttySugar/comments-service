package blog.comments.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Comment {
    @Id
    String id;
    String message;
    String recordId;
    String publisherId;
    Date publishedOn;
    Date updatedOn;
    @Builder.Default
    List<Comment>replies = new ArrayList<>();
}
