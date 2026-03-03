package org.example.ovr.service;
import org.example.ovr.Entity.Bill;
import org.example.ovr.repository.BillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillServiceTest {

    @Mock
    private BillRepository billRepository;

    @InjectMocks
    private BillService billService;

    private Bill bill;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        bill = new Bill();
        setBillId(bill, 1L); // reflection to set ID
        bill.setReservationId(100L);
        bill.setRoomNumber(201);
        bill.setRoomPrice(100.0);
        bill.setNumberOfDays(3);
        bill.setCreatedDate(LocalDateTime.now());
    }

    private void setBillId(Bill bill, Long id) throws Exception {
        Field idField = Bill.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(bill, id);
    }

    @Test
    void testSaveBill() {
        when(billRepository.save(any(Bill.class))).thenReturn(bill);

        Bill saved = billService.saveBill(bill);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(billRepository).save(bill);
    }

    @Test
    void testGetAllBills() {
        when(billRepository.findAll()).thenReturn(Arrays.asList(bill));

        List<Bill> bills = billService.getAllBills();

        assertNotNull(bills);
        assertEquals(1, bills.size());
        verify(billRepository).findAll();
    }

    @Test
    void testGetBillsByReservationId() {
        when(billRepository.findByReservationId(100L)).thenReturn(Arrays.asList(bill));

        List<Bill> bills = billService.getBillsByReservationId(100L);

        assertNotNull(bills);
        assertEquals(1, bills.size());
        assertEquals(100L, bills.get(0).getReservationId());
        verify(billRepository).findByReservationId(100L);
    }

    @Test
    void testDeleteBill() {
        doNothing().when(billRepository).deleteById(1L);

        billService.deleteBill(1L);

        verify(billRepository).deleteById(1L);
    }

    @Test
    void testGenerateBillPdf() throws Exception {
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> {
            Bill b = invocation.getArgument(0);
            setBillId(b, 1L); // ensure ID is set for PDF file name
            return b;
        });

        Bill pdfBill = billService.generateBillPdf(bill);

        assertNotNull(pdfBill.getPdfPath());
        assertTrue(pdfBill.getPdfPath().contains("Bill_1.pdf"));
        assertNotNull(pdfBill.getPdfUrl());
        assertTrue(pdfBill.getPdfUrl().contains("http://localhost:8080/bills/"));
        verify(billRepository, times(1)).save(bill); // saved once after PDF generation
    }
}