package com.railbookpro.repository;

import com.railbookpro.domain.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {

    Optional<Train> findByTrainNumber(String trainNumber);

    boolean existsByTrainNumber(String trainNumber);

    @Query("""
            SELECT t FROM Train t
            WHERE (:sourceCode IS NULL OR t.source.code = :sourceCode)
              AND (:destCode IS NULL OR t.destination.code = :destCode)
              AND (:trainNumber IS NULL OR t.trainNumber = :trainNumber)
              AND (:trainName IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :trainName, '%')))
            """)
    List<Train> search(@Param("sourceCode") String sourceCode,
                        @Param("destCode") String destCode,
                        @Param("trainNumber") String trainNumber,
                        @Param("trainName") String trainName);
}
