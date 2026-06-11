package com.JobTracker.demo.Repository;

import com.JobTracker.demo.Entity.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineItemRepository extends JpaRepository<LineItem, Long> {

    List<LineItem> findByBill_Id(Long id);
}
