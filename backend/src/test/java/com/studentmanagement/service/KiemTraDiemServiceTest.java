package com.studentmanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KiemTraDiemServiceTest {

    private KiemTraDiemService kiemTraDiemService;

    @BeforeEach
    void setUp() {
        kiemTraDiemService = new KiemTraDiemService();
    }

    @Test
    void testKiemTraDiemHopLe_DiemTrongKhoang_TraVeTrue() {
        assertTrue(kiemTraDiemService.kiemTraDiemHopLe(5.5));
        assertTrue(kiemTraDiemService.kiemTraDiemHopLe(0.0));
        assertTrue(kiemTraDiemService.kiemTraDiemHopLe(10.0));
    }

    @Test
    void testKiemTraDiemHopLe_DiemNgoaiKhoang_TraVeFalse() {
        assertFalse(kiemTraDiemService.kiemTraDiemHopLe(-1.0));
        assertFalse(kiemTraDiemService.kiemTraDiemHopLe(10.5));
    }

    @Test
    void testKiemTraDiemHopLe_DiemNull_TraVeFalse() {
        assertFalse(kiemTraDiemService.kiemTraDiemHopLe(null));
    }
}
