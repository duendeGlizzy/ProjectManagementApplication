package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.Bill;
import com.JobTracker.demo.Service.BillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.findAll();
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable long id) {
        Bill bill = billService.findById(id);
        return ResponseEntity.ok(bill);
    }

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestBody Bill bill,
                                           @RequestParam Long vendorId,
                                           @RequestParam Long taskId){

        Bill newBill = billService.createBill(bill, vendorId, taskId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBill);
    }


}
