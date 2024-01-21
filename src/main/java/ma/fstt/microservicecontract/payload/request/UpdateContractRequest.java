package ma.fstt.microservicecontract.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class UpdateContractRequest {
    @NotBlank
    @Indexed(unique = true)
    private String name;
    @NotBlank
    private double price;
    @NotBlank
    private String[] options;
    private String description;

}