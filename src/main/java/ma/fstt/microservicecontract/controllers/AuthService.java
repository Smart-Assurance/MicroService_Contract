package ma.fstt.microservicecontract.controllers;



import ma.fstt.microservicecontract.payload.request.TokenValidationRequest;
import ma.fstt.microservicecontract.payload.response.TokenValidationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final RestTemplate restTemplate;

    public AuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isValidEmployeeToken(String token) {
        String validationUrl = authServiceUrl + "/validate-token";
        TokenValidationRequest validationRequest = new TokenValidationRequest();
        validationRequest.setToken(token);
        ResponseEntity<TokenValidationResponse> responseEntity = restTemplate.postForEntity(validationUrl, validationRequest, TokenValidationResponse.class);
        return responseEntity.getBody().isResponse() == true && "administrator".equals(responseEntity.getBody().getMessage());
    }
}