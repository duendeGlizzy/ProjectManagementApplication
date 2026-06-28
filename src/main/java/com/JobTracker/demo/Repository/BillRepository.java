package com.JobTracker.demo.Repository;

import com.JobTracker.demo.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("SELECT DISTINCT b FROM Bill b " +
            "LEFT JOIN FETCH b.vendor " +
            "LEFT JOIN FETCH b.lineItems " +
            "WHERE b.issueDate BETWEEN :startDate AND :endDate ")
    List<Bill> findAllByIssueDateBetween(@Param("startDate") LocalDate issueDateAfter,
                                         @Param("endDate") LocalDate issueDateAfter1);
}
