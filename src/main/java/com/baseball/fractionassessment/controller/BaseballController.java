package com.baseball.fractionassessment.controller;

import com.baseball.fractionassessment.dto.BaseballPlayerDTO;
import com.baseball.fractionassessment.exceptions.PlayerDoesNotExistException;
import com.baseball.fractionassessment.model.BaseballPlayer;
import com.baseball.fractionassessment.service.BaseballService;
import com.baseball.fractionassessment.service.OpenAIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/baseball")
@CrossOrigin(origins = "http://localhost:4200")
public class BaseballController {

    private final BaseballService baseballService;
    private final OpenAIService openAIService;

    @Autowired
    public BaseballController(BaseballService baseballService, OpenAIService openAIService) {
        this.baseballService = baseballService;
        this.openAIService = openAIService;
    }

    @GetMapping("/players/paged")
    public ResponseEntity<Page<BaseballPlayerDTO>> retrievePlayers(@RequestParam int page, @RequestParam int size,
                                                                   @RequestParam String sortBy, @RequestParam String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<BaseballPlayer> result = this.baseballService.retrieveBaseballPlayers(pageable);
        Page<BaseballPlayerDTO> pagedDTO = result.map((d) -> BaseballPlayerDTO.from(d));
        return ResponseEntity.ok().body(pagedDTO);
    }

    @GetMapping("/players")
    public ResponseEntity<List<BaseballPlayerDTO>> retrieveAllPlayers() {
        List<BaseballPlayer> players = this.baseballService.retrieveAllBaseballPlayers();
        return ResponseEntity.ok().body(players.stream().map(BaseballPlayerDTO::from).toList());
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<BaseballPlayerDTO> retrievePlayerById(@PathVariable("id") long id) throws PlayerDoesNotExistException {
        return ResponseEntity.ok().body(BaseballPlayerDTO.from(this.baseballService.retrieveBaseballPlayer(id)));
    }

    @PutMapping("/player")
    public ResponseEntity<BaseballPlayerDTO> updatePlayer(@RequestBody BaseballPlayerDTO dto) throws PlayerDoesNotExistException {
        BaseballPlayer updatedPlayer = this.baseballService.updateBaseballPlayer(BaseballPlayerDTO.to(dto));
        return ResponseEntity.ok().body(BaseballPlayerDTO.from(updatedPlayer));
    }

    @PostMapping("/player")
    public ResponseEntity<BaseballPlayerDTO> createPlayer(@RequestBody BaseballPlayerDTO dto) {
        return ResponseEntity.ok().body(BaseballPlayerDTO.from(this.baseballService.createBaseballPlayer(BaseballPlayerDTO.to(dto))));
    }

    @DeleteMapping("/player/{id}")
    public ResponseEntity<Void> deletePlayerById(@PathVariable("id") long id) throws PlayerDoesNotExistException {
        this.baseballService.deleteBaseballPlayer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/player/description")
    public ResponseEntity<BaseballPlayerDTO> createPlayerDescription(@RequestBody BaseballPlayerDTO dto) throws JsonProcessingException {
        return ResponseEntity.ok().body(this.openAIService.generatePlayerDescription(dto));
    }
}
