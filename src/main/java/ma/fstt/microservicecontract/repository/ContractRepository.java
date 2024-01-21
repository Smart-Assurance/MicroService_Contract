package ma.fstt.microservicecontract.repository;

import ma.fstt.microservicecontract.entities.Contract;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ContractRepository extends MongoRepository<Contract, String> {

}