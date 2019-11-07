package com.quemepongo.api;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.common.collect.Lists;
import com.quemepongo.domain.Contract;
import com.quemepongo.domain.User;
import com.quemepongo.persistence.partialsettlement.ContractRepository;
import com.quemepongo.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static java.math.BigDecimal.TEN;

@RestController
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("contracts")
@JsonSerialize(using = LocalDateTimeSerializer.class)
@Transactional
public class ContractController extends AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(ContractController.class);
    private final ContractRepository contractRepository;

    public ContractController(UserRepository userRepository, ContractRepository contractRepository) {
        super(userRepository);
        this.contractRepository = contractRepository;
    }

    @GetMapping()
    public ResponseEntity getAll(@RequestHeader(value="Authorization",required = false) String authToken){
        User user = getUserFromToken(authToken);
        List<Contract> contracts = contractRepository.findAll();
        return ResponseEntity.ok(contracts);
    }

    @PutMapping("/{contractId}")
    public ResponseEntity adjustContract(@RequestHeader(value="Authorization",required = false) String authToken,
                                         @RequestBody Contract newDate,
                                         @PathVariable Long contractId){
            User user = getUserFromToken(authToken);
            contractRepository.modifyContractDate(contractId, newDate.getDeliveryDate());
            return ResponseEntity.ok().build();
    }


    @PostMapping("/createAll")
    public ResponseEntity createAll(){
        List<Contract> contracts =  Lists.newArrayList(
                new Contract(1L, "37896505", "Roli", "Mi Casa", LocalDate.of(2019, 11, 13), TEN, TEN, TEN),
                new Contract(1L, "37896505", "Roli", "Mi Casa", LocalDate.of(2019, 11, 13), TEN, TEN, TEN),
                new Contract(1L, "37896505", "Roli", "Mi Casa", LocalDate.of(2019, 11, 13), TEN, TEN, TEN),
                new Contract(2L, "39267695", "Achus", "La casa de Achus", LocalDate.of(2019, 11, 13), TEN, TEN, TEN),
                new Contract(2L, "39267695", "Achus", "La casa de Achus", LocalDate.of(2019, 11, 13), TEN, TEN, TEN)
        );
        contracts.forEach(contractRepository::save);
        return ResponseEntity.ok().build();
    }

}
