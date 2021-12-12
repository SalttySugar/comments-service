package blog.comments.repository;

import blog.comments.model.Comment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepository extends ReactiveMongoRepository<Comment, String> {
}
