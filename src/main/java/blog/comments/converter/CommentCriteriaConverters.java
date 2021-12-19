package blog.comments.converter;

import blog.comments.service.CommentCriteria;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;


public abstract class CommentCriteriaConverters {
    @Component
    public static final class CommentCriteriaToMongoQueryConverter implements Converter<CommentCriteria, Query> {

        @Override
        public Query convert(CommentCriteria source) {
            Query query = new Query();

            if (source.getRecordId() != null) {
                query.addCriteria(Criteria.where("record_id").is(source.getRecordId()));
            }

            if (source.getRecordId() != null) {
                query.addCriteria(Criteria.where("owner_id").is(source.getRecordId()));
            }

            if (source.getAfter() != null) {
                query.addCriteria(Criteria.where("uploaded_on").gte(source.getAfter()));
            }

            if (source.getBefore() != null) {
                query.addCriteria(Criteria.where("uploaded_on").lte(source.getBefore()));
            }

            if (source.getIds() != null) {
                query.addCriteria(Criteria.where("id").in(source.getIds()));
            }


            return query;
        }
    }
}
