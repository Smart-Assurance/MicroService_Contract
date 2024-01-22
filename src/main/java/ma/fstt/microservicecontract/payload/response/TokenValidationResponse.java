package ma.fstt.microservicecontract.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class TokenValidationResponse {
    private boolean response;
    private String message;

    public TokenValidationResponse(String message){
        this.message=message;
        this.response=true;
    }
    public TokenValidationResponse(){
        this.message="Invalid token";
        this.response=false;
    }

}
