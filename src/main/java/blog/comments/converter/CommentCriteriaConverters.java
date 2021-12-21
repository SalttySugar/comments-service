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


            if (source.getOwner() != null) {
                query.addCriteria(Criteria.where("owner_id").is(source.getOwner()));
            }


            if (source.getBefore() != null || source.getAfter() != null) {
                var criteria = Criteria.where("published_on");
                if(source.getBefore() != null) {
                    criteria.lte(source.getBefore());
                }

                if(source.getAfter() != null) {
                    criteria.gte(source.getAfter());
                }

                query.addCriteria(criteria);
            }

            if(source.getRecord() != null) {
                query.addCriteria(Criteria.where("record_id").is(source.getRecord()));
            }

            return query;
        }
    }
}
