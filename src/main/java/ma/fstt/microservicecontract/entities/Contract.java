package ma.fstt.microservicecontract.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "contracts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Contract {
    @Id
    private String id;
    private String name;
    private double price;
    private String[] options;
    private String description;
    private boolean active;

    public Contract(String name, double price, String[] options, String description, boolean active) {
        this.name = name;
        this.price = price;
        this.options = options;
        this.description = description;
        this.active = active;
    }

}
