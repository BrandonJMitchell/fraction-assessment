package com.baseball.fractionassessment.repository;

import com.baseball.fractionassessment.model.BaseballPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseballRepository extends JpaRepository<BaseballPlayer, Long> {
}
