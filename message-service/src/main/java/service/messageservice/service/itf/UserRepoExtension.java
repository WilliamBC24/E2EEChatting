package service.messageservice.service.itf;

import reactor.core.publisher.Mono;
import service.messageservice.entity.dto.UserDTOKey;

public interface UserRepoExtension {
    Mono<Void> updatePublicKeyByUsername(UserDTOKey userDTOKey);
}
