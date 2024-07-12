package com.baseball.fractionassessment.service;

import com.baseball.fractionassessment.exceptions.PlayerDoesNotExistException;
import com.baseball.fractionassessment.model.BaseballPlayer;
import com.baseball.fractionassessment.repository.BaseballRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BaseballService {

    private final BaseballRepository baseballRepository;
    private final ObjectMapper objectMapper;
    private final String path = "data/baseball_data.json";


    @Autowired
    public BaseballService(BaseballRepository baseballRepository, ObjectMapper objectMapper) {
        this.baseballRepository = baseballRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void loadJsonData() {
        log.info("Running loadJsonData...");
        long count = this.baseballRepository.count();
        if (count > 0) {
            log.info("Baseball table is already loaded.");
        } else {
            try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
                TypeReference<List<BaseballPlayer>> typeReference = new TypeReference<>() {};
                List<BaseballPlayer> players = this.objectMapper.readValue(inputStream, typeReference);

                List<BaseballPlayer> sortedByHits =
                        players.stream().sorted(Comparator.comparingInt(BaseballPlayer::getHits).reversed())
                                .collect(Collectors.toList());

                this.updatePlayerRanks(sortedByHits);

                this.baseballRepository.saveAll(players);
                log.info(this.baseballRepository.count() + " rows loaded successfully.");
            } catch (IOException exception) {
                log.error("loadJsonData failed: " + exception.getMessage());
            }
        }
    }

    private void updatePlayerRanks(List<BaseballPlayer> sortedList) {
        log.info("updating ranks...");
        int currentRank = 1;
        int currentHits = sortedList.get(0).getHits();

        for(BaseballPlayer player : sortedList) {
            if (player.getHits() < currentHits) {
                currentRank = currentRank + 1;
                currentHits = player.getHits();
            }

            player.setRank(String.valueOf(currentRank));
        }
    }

    public Page<BaseballPlayer> retrieveBaseballPlayers(PageRequest pageRequest) {
        return this.baseballRepository.findAll(pageRequest);
    }

    public List<BaseballPlayer> retrieveAllBaseballPlayers() {
        return this.baseballRepository.findAll();
    }

    public BaseballPlayer retrieveBaseballPlayer(long id) throws PlayerDoesNotExistException {
        Optional<BaseballPlayer> opt = this.baseballRepository.findById(id);
        if (opt.isEmpty()) {
            throw new PlayerDoesNotExistException(MessageFormat.format("Player with id: {0} does not exist.", id));
        }
        return opt.get();
    }

    public BaseballPlayer updateBaseballPlayer(BaseballPlayer baseballPlayer) throws PlayerDoesNotExistException {
        Long id = baseballPlayer.getId();
        boolean exist = this.baseballRepository.existsById(id);

        if (exist) {
            return this.baseballRepository.save(baseballPlayer);
        }
        throw new PlayerDoesNotExistException(MessageFormat.format("Player with id: {0} does not exist.", id));
    }
    
    public BaseballPlayer createBaseballPlayer(BaseballPlayer baseballPlayer) {
        return this.baseballRepository.save(baseballPlayer);
    }
    
    public void deleteBaseballPlayer(Long id) throws PlayerDoesNotExistException {
        boolean exist = this.baseballRepository.existsById(id);
        if (!exist) {
            throw new PlayerDoesNotExistException(MessageFormat.format("Player with id: {0} does not exist.", id));
        }
        this.baseballRepository.deleteById(id);
    }
}
