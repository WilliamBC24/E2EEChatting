package service.messageservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import service.messageservice.entity.RSA;
import service.messageservice.entity.dto.RSADTO;
import service.messageservice.entity.response.ApiResponse;
import service.messageservice.repo.RSARepo;
import service.messageservice.util.ResponseBuilder;

@RestController
@RequestMapping("/security")
public class SecurityController {
    private final RSARepo rsaRepo;

    public SecurityController(RSARepo rsaRepo) {
        this.rsaRepo = rsaRepo;
    }

    @PutMapping("/saveRSA")
    public Mono<ResponseEntity<ApiResponse<RSA>>> saveRSA(@RequestBody RSADTO rsaDTO) {
        return rsaRepo.save(RSA.builder()
                        .publicKey(rsaDTO.getPublicKey())
                        .privateKey(rsaDTO.getPrivateKey())
                        .build())
                .map(rsa -> ResponseBuilder.buildSuccessResponse("Saved RSA", rsa));
    }

    @GetMapping("/getRSA")
    public Mono<ResponseEntity<ApiResponse<RSA>>> getRSA() {
        return rsaRepo.findAll()
                .next()
                .map(rsa -> ResponseBuilder.buildSuccessResponse("Got RSA", rsa));
    }
}
