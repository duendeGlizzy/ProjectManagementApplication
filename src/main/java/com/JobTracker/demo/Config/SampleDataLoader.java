package com.JobTracker.demo.Config;

import com.JobTracker.demo.ENum.*;
import com.JobTracker.demo.Entity.*;
import com.JobTracker.demo.Repository.ClientRepository;
import com.JobTracker.demo.Repository.VendorRepository;
import com.JobTracker.demo.Service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class SampleDataLoader {

    // 1. Define a standard CommandLineRunner bean that delegates execution to our transactional component
    @Bean
    public CommandLineRunner loadData(TransactionalDataInitializer initializer) {
        return args -> initializer.runDataInjection();
    }

    // 2. Separate component class to manage transactional boundaries correctly during execution context
    @SuppressWarnings("ReassignedVariable")
    @Component
    public static class TransactionalDataInitializer {

        private final ClientRepository clientRepository;
        private final PrimeContractorService primeContractorService;
        private final VendorRepository vendorRepository;
        private final SubContractorService subContractorService;
        private final JobService jobService;
        private final TaskService taskService;
        private final BillService billService;
        private final PaymentService paymentService;

        public TransactionalDataInitializer(
                ClientRepository clientRepository,
                PrimeContractorService primeContractorService,
                VendorRepository vendorRepository,
                SubContractorService subContractorService,
                JobService jobService,
                TaskService taskService,
                BillService billService,
                PaymentService paymentService) {
            this.clientRepository = clientRepository;
            this.primeContractorService = primeContractorService;
            this.vendorRepository = vendorRepository;
            this.subContractorService = subContractorService;
            this.jobService = jobService;
            this.taskService = taskService;
            this.billService = billService;
            this.paymentService = paymentService;
        }

        public void runDataInjection() {
            if (clientRepository.count() > 0) {
                System.out.println(">> Database already contains data. Skipping sample data injection.");
                return;
            }

            System.out.println(">> Injecting comprehensive historical mock data for financial analytics testing...");

            int currentYear = LocalDate.now().getYear();

            // 1. Create Base Stakeholders
            Client client1 = new Client();
            client1.setFirstName("John");
            client1.setLastName("Doe");
            client1.setPhoneNumber("555-0100");
            client1 = clientRepository.save(client1);

            PrimeContractor prime1 = new PrimeContractor();
            prime1.setCompanyName("Apex Contracting Group");
            prime1.setPhoneNumber("555-0150");
            prime1 = primeContractorService.save(prime1);

            SubContractor sub1 = new SubContractor();
            sub1.setCompanyName("Elite Electrical Systems");
            sub1.setPhoneNumber("555-0199");
            sub1 = subContractorService.save(sub1);

            Vendor vendor1 = new Vendor();
            vendor1.setCompanyName("BuildFast Supply Co.");
            vendor1.setPhoneNumber("555-0111");
            vendor1.setAddress("100 Industrial Pkwy");
            vendor1 = vendorRepository.save(vendor1);

            Vendor vendor2 = new Vendor();
            vendor2.setCompanyName("Pro Lumber & Hardware");
            vendor2.setPhoneNumber("555-0222");
            vendor2.setAddress("450 Timberline Rd");
            vendor2 = vendorRepository.save(vendor2);

            // 2. Create Job
            Job job1 = new Job();
            job1.setDescription("Residential & Commercial Workspace Renovation");
            job1.setJobType(JobType.DELEADING);
            job1.setStatus(JobStatus.STARTED);
            job1.setStartDate(LocalDateTime.of(currentYear, 1, 10, 8, 0));
            job1 = jobService.createNewJob(job1, client1.getClientId(), prime1.getPrimeContractorId());

            // 3. Create Tasks
            Task taskA = new Task();
            taskA.setDescription("Structural Wood Framing");
            taskA.setStatus(TaskStatus.COMPLETED);
            taskA = taskService.createTask(taskA, null, job1.getJobId());

            Task taskB = new Task();
            taskB.setDescription("Main Electrical Panel Grid Install");
            taskB.setStatus(TaskStatus.IN_PROGRESS);
            taskB.setIsSubContracted(true);
            taskB = taskService.createTask(taskB, sub1.getSubContractorId(), job1.getJobId());

            Task taskC = new Task();
            taskC.setDescription("HVAC Duct Routing and Rough-in");
            taskC.setStatus(TaskStatus.COMPLETED);
            taskC = taskService.createTask(taskC, null, job1.getJobId());

            // ⭐ THE FIX: Force Hibernate cache state to flush explicitly to the database tables
            // This guarantees subsequent service requests find the records cleanly.

            // ==========================================
            // FINANCIAL TRANSACTIONS LOAD (YEAR-TO-DATE)
            // ==========================================

            // --- JANUARY ---
            Payment pJan = new Payment();
            pJan.setCheckAmount(new BigDecimal("12500.00"));
            pJan.setDateReceived(LocalDate.of(currentYear, 1, 15));
            pJan.setPaymentMethod(PaymentMethod.CHECK);
            pJan.setReferenceNumber("ACH-JAN-101");
            paymentService.createPayment(pJan, job1.getJobId());

            Bill bJan = new Bill();
            bJan.setDescription("Initial Lumber Order for Framing Phase");
            bJan.setIssueDate(LocalDate.of(currentYear, 1, 18));
            bJan.setDueDate(LocalDate.of(currentYear, 2, 18));
            bJan.setStatus(BillStatus.PAYED);

            LineItem li1 = new LineItem();
            li1.setDescription("Premium 2x4 Framing Studs");
            li1.setQuantity(120);
            li1.setUnitPrice(new BigDecimal("5.75"));
            li1.setTaxCategory(TaxCategory.MATERIALS_SUPPLIES);

            LineItem li2 = new LineItem();
            li2.setDescription("Structural Sheathing Panels");
            li2.setQuantity(30);
            li2.setUnitPrice(new BigDecimal("22.50"));
            li2.setTaxCategory(TaxCategory.MATERIALS_SUPPLIES);

            bJan.addLineItem(li1);
            bJan.addLineItem(li2);
            bJan = billService.createBill(bJan, vendor2.getVendorId(), job1.getJobId());

            Payment pJanExp = new Payment();
            pJanExp.setCheckAmount(bJan.getTotalAmount());
            pJanExp.setDateReceived(LocalDate.of(currentYear, 1, 28));
            pJanExp.setPaymentMethod(PaymentMethod.CHECK);
            pJanExp.setReferenceNumber("CHK-88102");
            paymentService.createPayment(pJanExp, job1.getJobId());

            // --- FEBRUARY ---
            Bill bFeb = new Bill();
            bFeb.setDescription("Electrical Grid Routing & Fixture Prep Bill");
            bFeb.setIssueDate(LocalDate.of(currentYear, 2, 20));
            bFeb.setDueDate(LocalDate.of(currentYear, 3, 20));
            bFeb.setStatus(BillStatus.PAYED);

            LineItem li3 = new LineItem();
            li3.setDescription("Subcontracted Master Electrician Hourly Fee");
            li3.setQuantity(40);
            li3.setUnitPrice(new BigDecimal("85.00"));
            li3.setTaxCategory(TaxCategory.SUBCONTRACTOR_LABOR);

            LineItem li4 = new LineItem();
            li4.setDescription("Romex 12/2 Wire Spools (250ft)");
            li4.setQuantity(4);
            li4.setUnitPrice(new BigDecimal("115.00"));
            li4.setTaxCategory(TaxCategory.MATERIALS_SUPPLIES);

            bFeb.addLineItem(li3);
            bFeb.addLineItem(li4);
            bFeb = billService.createBill(bFeb, vendor1.getVendorId(), job1.getJobId());

            Payment pFebExp = new Payment();
            pFebExp.setCheckAmount(bFeb.getTotalAmount());
            pFebExp.setDateReceived(LocalDate.of(currentYear, 2, 27));
            pFebExp.setPaymentMethod(PaymentMethod.CHECK);
            pFebExp.setReferenceNumber("ACH-FEB-902");
            paymentService.createPayment(pFebExp, job1.getJobId());

            // --- MARCH ---
            Payment pMar = new Payment();
            pMar.setCheckAmount(new BigDecimal("35000.00"));
            pMar.setDateReceived(LocalDate.of(currentYear, 3, 5));
            pMar.setPaymentMethod(PaymentMethod.CHECK);
            pMar.setReferenceNumber("WIRE-MAR-007");
            paymentService.createPayment(pMar, job1.getJobId());

            Bill bMar = new Bill();
            bMar.setDescription("HVAC Commercial Ductwork Systems and Permitting");
            bMar.setIssueDate(LocalDate.of(currentYear, 3, 12));
            bMar.setDueDate(LocalDate.of(currentYear, 4, 12));
            bMar.setStatus(BillStatus.PAYED);

            LineItem li5 = new LineItem();
            li5.setDescription("Central Air Intake Ventilation System");
            li5.setQuantity(2);
            li5.setUnitPrice(new BigDecimal("4200.00"));
            li5.setTaxCategory(TaxCategory.MATERIALS_SUPPLIES);

            LineItem li6 = new LineItem();
            li6.setDescription("Municipal HVAC Environmental Safety Inspection Permit");
            li6.setQuantity(1);
            li6.setUnitPrice(new BigDecimal("350.00"));
            li6.setTaxCategory(TaxCategory.OFFICE_EXPENSE);

            bMar.addLineItem(li5);
            bMar.addLineItem(li6);
            bMar = billService.createBill(bMar, vendor1.getVendorId(), job1.getJobId());

            Payment pMarExp = new Payment();
            pMarExp.setCheckAmount(bMar.getTotalAmount());
            pMarExp.setDateReceived(LocalDate.of(currentYear, 3, 25));
            pMarExp.setPaymentMethod(PaymentMethod.CREDIT);
            pMarExp.setReferenceNumber("ACH-MAR-450");
            paymentService.createPayment(pMarExp, job1.getJobId());

            // --- RECENT PENDING MATRICES ---
            Bill bRecentUnpaid = new Bill();
            bRecentUnpaid.setDescription("Supplemental Finishing Supplies (Unpaid Testing Context)");
            bRecentUnpaid.setIssueDate(LocalDate.now().minusDays(10));
            bRecentUnpaid.setDueDate(LocalDate.now().plusDays(20));
            bRecentUnpaid.setStatus(BillStatus.RECEIVED);

            LineItem li7 = new LineItem();
            li7.setDescription("Interior Drywall Finish Compound Trim");
            li7.setQuantity(15);
            li7.setUnitPrice(new BigDecimal("32.40"));
            li7.setTaxCategory(TaxCategory.MATERIALS_SUPPLIES);

            bRecentUnpaid.addLineItem(li7);
            billService.createBill(bRecentUnpaid, vendor2.getVendorId(), job1.getJobId());

            System.out.println(">> Financial report data load injection complete. Verify analytics dashboard views.");
        }
    }
}