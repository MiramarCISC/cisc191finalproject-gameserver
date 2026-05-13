package edu.sdccd.cisc191.server.repository;

import edu.sdccd.cisc191.server.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, String> {
    List<MatchEntity> findTop10ByPlayerNameOrderByMatchIdDesc(String playerName);
}
