package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.LineItem;
import com.JobTracker.demo.Service.LineItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/line_items")
public class LineItemController {

    private final LineItemService lineItemService;

    public LineItemController(LineItemService lineItemService) {
        this.lineItemService = lineItemService;
    }

    @GetMapping
    public ResponseEntity<List<LineItem>> getLineItems() {
        List<LineItem> lineItems = lineItemService.findAll();
        return ResponseEntity.ok(lineItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineItem> getLineItem(@PathVariable Long id) {
        LineItem lineItem = lineItemService.findById(id);
        return ResponseEntity.ok(lineItem);
    }

    @PostMapping
    public ResponseEntity<LineItem> createLineItem(@RequestBody LineItem lineItem,
                                                   @RequestParam Long billId) {

        LineItem newLineItem = lineItemService.createLineItem(lineItem, billId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newLineItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LineItem> updateLineItem(@PathVariable Long id, @RequestBody LineItem lineItem) {
        LineItem currentLineItem = lineItemService.updateLineItem(id, lineItem);
        return ResponseEntity.ok(currentLineItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLineItem(@PathVariable Long id) {
        lineItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/{billId}")
    public ResponseEntity<List<LineItem>> getLineItemsByBillId(@PathVariable Long billId) {
        List<LineItem> lineItems = lineItemService.findByBillId(billId);
        return ResponseEntity.ok(lineItems);
    }




}
