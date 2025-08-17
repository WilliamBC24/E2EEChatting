package service.messageservice.service;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import service.messageservice.entity.User;
import service.messageservice.entity.dto.UserDTOKey;
import service.messageservice.service.itf.UserRepoExtension;

@Service
public class UserRepoImpl implements UserRepoExtension {
    private ReactiveMongoTemplate mongoTemplate;
    public UserRepoImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Mono<Void> updatePublicKeyByUsername(UserDTOKey userDTOKey) {
        Query query = Query.query(Criteria.where("username").is(userDTOKey.getUsername()));
        Update update = new Update().set("publicKey", userDTOKey.getKey());
        return mongoTemplate.updateFirst(query, update, User.class).then();
    }
}
