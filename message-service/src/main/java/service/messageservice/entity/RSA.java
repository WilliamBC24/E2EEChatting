package service.messageservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rsa")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RSA {
    @Id
    private String id;
    private String publicKey;
    private String privateKey;
}
