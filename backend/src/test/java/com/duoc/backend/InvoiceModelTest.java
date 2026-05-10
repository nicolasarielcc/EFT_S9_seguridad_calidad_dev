package com.duoc.backend;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class InvoiceModelTest {

    @Test
    void getterAndSetterForIdShouldWork() {
        Invoice invoice = new Invoice();
        invoice.setId(10);
        assertEquals(10, invoice.getId());
    }

    @Test
    void setTotalShouldPersistValue() {
        Invoice invoice = new Invoice();
        invoice.setTotal(new BigDecimal("500.00"));
        assertEquals(0, invoice.getTotal().compareTo(new BigDecimal("500.00")));
    }

    @Test
    void setNotesShouldPersistValue() {
        Invoice invoice = new Invoice();
        invoice.setNotes("Control en 10 días");
        assertEquals("Control en 10 días", invoice.getNotes());
    }

    @Test
    void setVatAmountShouldPersistValue() {
        Invoice invoice = new Invoice();
        invoice.setVatAmount(new BigDecimal("95.00"));
        assertEquals(0, invoice.getVatAmount().compareTo(new BigDecimal("95.00")));
    }

    @Test
    void setSubtotalShouldPersistValue() {
        Invoice invoice = new Invoice();
        invoice.setSubtotal(new BigDecimal("200.00"));
        assertEquals(0, invoice.getSubtotal().compareTo(new BigDecimal("200.00")));
    }

    @Test
    void setIssueDateShouldPersistValue() {
        Invoice invoice = new Invoice();
        LocalDate date = LocalDate.of(2026, 5, 1);
        invoice.setIssueDate(date);
        assertEquals(date, invoice.getIssueDate());
    }

    @Test
    void calculateTotalsShouldComputeCorrectlyWithNullVatRate() {
        Invoice invoice = new Invoice();
        InvoiceLineItem item = new InvoiceLineItem(InvoiceLineItemType.SERVICE, "Consulta", 2, new BigDecimal("100"));
        invoice.setItems(List.of(item));
        // vatRate is null — should default to ZERO
        invoice.calculateTotals();

        assertEquals(0, invoice.getSubtotal().compareTo(new BigDecimal("200")));
        assertEquals(0, invoice.getVatAmount().compareTo(BigDecimal.ZERO));
        assertEquals(0, invoice.getTotal().compareTo(new BigDecimal("200")));
    }

    @Test
    void notesGetterShouldReturnNullByDefault() {
        Invoice invoice = new Invoice();
        assertNull(invoice.getNotes());
    }
}
