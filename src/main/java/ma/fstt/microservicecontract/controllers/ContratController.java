package ma.fstt.microservicecontract.controllers;


import ma.fstt.microservicecontract.entities.Contract;
import ma.fstt.microservicecontract.payload.request.AddContractRequest;
import ma.fstt.microservicecontract.payload.request.UpdateContractRequest;
import ma.fstt.microservicecontract.payload.response.MessageResponse;
import ma.fstt.microservicecontract.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contracts")

public class ContratController {

    @Autowired
    public ContractRepository contractRepository;

    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addContract(@RequestBody AddContractRequest addContractRequest) {
        try {
            Contract contract = new Contract(
                    addContractRequest.getName(),
                    addContractRequest.getPrice(),
                    addContractRequest.getOptions(),
                    addContractRequest.getDescription(),true
            );
            contractRepository.save(contract);
            return ResponseEntity.ok(new MessageResponse(201,"Contract saved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(400,"Contract doesn't save "));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Contract>> getAllContract() {
        try {
            List<Contract> contracts = contractRepository.findAll();

            return ResponseEntity.ok(contracts);
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Erreur interne du serveur
        }
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<Contract> getContractById(@PathVariable String contractId) {
        try {
            Optional<Contract> contract = contractRepository.findById(contractId);
            if (contract.isPresent()) {
                return ResponseEntity.ok(contract.get());
            } else {
                return ResponseEntity.status(404).build(); // Ressource non trouvée
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build(); // Erreur interne du serveur
        }
    }


    @DeleteMapping("/{contractId}")
    public ResponseEntity<MessageResponse> deleteContract(@PathVariable String contractId) {
        try {
            Optional<Contract> contract = contractRepository.findById(contractId);
            if (contract.isPresent()) {
                if(contract.get().isActive()){
                    return ResponseEntity.status(403).body(new MessageResponse(403, "Contract not inactive"));
                }
                contractRepository.delete(contract.get());
                return ResponseEntity.ok(new MessageResponse(200, "Contract deleted successfully"));
            } else {
                return ResponseEntity.status(404).body(new MessageResponse(404, "Contract not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse(500, "Internal server error"));
        }
    }

    //active/inactive contract
    @PutMapping("/state/{contractId}")
    public ResponseEntity<MessageResponse> contractUpdateState(
            @PathVariable String contractId) {
        try {
            Optional<Contract> optionalContract = contractRepository.findById(contractId);
            if (optionalContract.isPresent()) {
                Contract contract = optionalContract.get();
                String updatedState = contract.isActive() ? "inactive" : "active";
                contract.setActive(!contract.isActive());
                contractRepository.save(contract);
                return ResponseEntity.ok(new MessageResponse(200, "Contract is now " + updatedState));
            } else {
                return ResponseEntity.status(404).body(new MessageResponse(404, "Contract not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse(500, "Internal server error"));
        }
    }

    //update contract
    @PutMapping("/{contractId}")
    public ResponseEntity<MessageResponse> contractUpdate(
            @PathVariable String contractId,
            @RequestBody UpdateContractRequest updatedClientRequest
    ) {
        try {
            Optional<Contract> optionalContract = contractRepository.findById(contractId);
            if (optionalContract.isPresent()) {
                Contract contract = optionalContract.get();

                // Mettre à jour tous les champs de contract
                contract.setName(updatedClientRequest.getName());
                contract.setDescription(updatedClientRequest.getDescription());
                contract.setPrice(updatedClientRequest.getPrice());
                contract.setOptions(updatedClientRequest.getOptions());

                contractRepository.save(contract);

                return ResponseEntity.ok(new MessageResponse(200, "Contract updated successfully"));
            } else {
                return ResponseEntity.status(404).body(new MessageResponse(404, "Contract not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse(500, "Internal server error"));
        }
    }

}