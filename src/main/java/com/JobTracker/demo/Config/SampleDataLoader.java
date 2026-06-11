package com.JobTracker.demo.Config;

import com.JobTracker.demo.ENum.JobStatus;
import com.JobTracker.demo.ENum.TaskStatus;
import com.JobTracker.demo.Entity.*;
import com.JobTracker.demo.Repository.ClientRepository;
import com.JobTracker.demo.Repository.VendorRepository;
import com.JobTracker.demo.Service.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class SampleDataLoader {

    @Bean
    @Transactional
    public CommandLineRunner loadData(
            ClientRepository clientRepository,
            PrimeContractorService primeContractorService,
            VendorRepository vendorRepository,
            SubContractorService subContractorService,
            JobService jobService,
            TaskService taskService,
            BillService billService,
            LineItemService lineItemService,
            PaymentService paymentService){

        return args -> {
            // 1. Check if database is already populated to avoid duplicate inserts
            if (clientRepository.count() > 0) {
                System.out.println(">> Database already contains data. Skipping sample data injection.");
                return;
            }

            System.out.println(">> Initiating sample data seed script...");

            // 2. Create and Save Clients
            Client client1 = new Client();
            client1.setFirstName("John");
            client1.setLastName("Smith");
            client1.setPhoneNumber("555-0192");
            client1.setAddress("123 Maple Ave, New Bedford");
            client1 = clientRepository.save(client1);

            Client client2 = new Client();
            client2.setFirstName("Sarah");
            client2.setLastName("Connor");
            client2.setPhoneNumber("555-0143");
            client2.setAddress("742 Evergreen Terr, Boston");
            client2 = clientRepository.save(client2);

            // 3. Create and Save Prime Contractors (using Service for validations)
            PrimeContractor prime1 = new PrimeContractor();
            prime1.setCompanyName("Apex Construction Group");
            prime1.setAddress("456 Industrial Blvd, Worcester");
            prime1.setPhoneNumber("555-9001");
            prime1 = primeContractorService.save(prime1);

            // 4. Create and Save Vendors
            Vendor vendor1 = new Vendor();
            vendor1.setCompanyName("BuildAll Material Supply");
            vendor1.setAddress("88 Brickyard Rd, Fall River");
            vendor1.setPhoneNumber("555-4433");
            // Assuming vendor has a baseline repository save method
            vendor1 = vendorRepository.save(vendor1);

            // 5. Create and Save SubContractors (using Service)
            SubContractor sub1 = new SubContractor();
            sub1.setCompanyName("VoltTech Electrical Services");
            sub1.setPhoneNumber("555-8899");
            sub1.setPrice(new BigDecimal("1500.00"));
            sub1 = subContractorService.save(sub1);

            // 6. Create and Save a Job (linked to Client1 and Prime1)
            Job job1 = new Job();
            job1.setDescription("Residential Kitchen Remodel & Panel Upgrade");
            job1.setStatus(JobStatus.STARTED);
            // Assuming your createNewJob or save methods take these parameters or handle binding
            job1 = jobService.createNewJob(job1, client1.getClientId(), prime1.getPrimeContractorId());

            // 7. Create Tasks for Job1 (using TaskService)
            // Task A: Framing & Drywall (Self-performed / Material-heavy)
            Task taskA = new Task();
            taskA.setDescription("Drywall installation and framing extensions");
            taskA.setStatus(TaskStatus.IN_PROGRESS);
            taskA.setTotalPrice(new BigDecimal("5000.00")); // Contract Price
            taskA.setIsSubContracted(false);
            taskA.setPayRoll(new BigDecimal("1200.00")); // Internal Labor allocation
            taskA = taskService.createTask(taskA, null, job1.getJobId());

            // Task B: Electrical Rough-In (Subcontracted out to VoltTech)
            Task taskB = new Task();
            taskB.setDescription("Main service panel upgrade to 200A and wiring");
            taskB.setStatus(TaskStatus.In_Queue);
            taskB.setTotalPrice(new BigDecimal("3500.00")); // Contract Price
            taskB.setIsSubContracted(true);
            taskB = taskService.createTask(taskB, sub1.getSubContractorId(), job1.getJobId());

            // 8. Create a Vendor Bill for materials used on Task A (Framing)
            Bill bill1 = new Bill();
            bill1.setDescription("Lumber, drywall sheets, and fastening screws");
            bill1.setIssueDate(LocalDateTime.now().minusDays(5));
            bill1.setDueDate(LocalDateTime.now().plusDays(25));

            // Generate Line Items to populate the bill input
            LineItem item1 = new LineItem();
            item1.setDescription("Premium 2x4 Studs");
            item1.setQuantity(50);
            item1.setUnitPrice(new BigDecimal("6.50")); // Dynamic subtotal logic handles this multiplication!

            LineItem item2 = new LineItem();
            item2.setDescription("Drywall Sheets 4x8");
            item2.setQuantity(20);
            item2.setUnitPrice(new BigDecimal("18.00"));

            bill1.addLineItem(item1);
            bill1.addLineItem(item2);

            // Save the bill through BillService to calculate totals and establish constraints
            bill1 = billService.createBill(bill1, vendor1.getVendorId(), taskA.getTaskId());

            // 9. Process a partial or complete payment transaction against the newly generated Bill
            Payment payment1 = new Payment();
            payment1.setCheckAmount(bill1.getTotalAmount()); // Match exact amount to clear the bill
            payment1.setDateReceived(LocalDateTime.now());
            // Assuming payment fields require a reference or a clean processing route
            payment1 = paymentService.processPayment(payment1, bill1.getBillId());

            System.out.println(">> Sample data tracking structures seeded successfully!");
        };
    }


}
