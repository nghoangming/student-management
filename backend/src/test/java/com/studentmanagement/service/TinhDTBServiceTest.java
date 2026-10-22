package com.studentmanagement.service;

import com.studentmanagement.domain.BangDiem;
import com.studentmanagement.domain.ChiTietDiem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TinhDTBServiceTest {

    @InjectMocks
    private TinhDTBService tinhDTBService;

    private BangDiem bangDiem;

    @BeforeEach
    void setUp() {
        bangDiem = new BangDiem();
        bangDiem.setMaHocSinh(1L);
        bangDiem.setMaMonHoc(1L);
    }

    @Test
    void testTinhDiemTrungBinh_DanhSachDiemHopLe() {
        // Arrange
        List<ChiTietDiem> chiTietList = new ArrayList<>();
        
        ChiTietDiem ctd1 = new ChiTietDiem();
        ctd1.setDiem(8.0);
        chiTietList.add(ctd1);
        
        ChiTietDiem ctd2 = new ChiTietDiem();
        ctd2.setDiem(7.0);
        chiTietList.add(ctd2);
        
        ChiTietDiem ctd3 = new ChiTietDiem();
        ctd3.setDiem(9.0);
        chiTietList.add(ctd3);

        bangDiem.setChiTietDiemList(chiTietList);

        // Act
        tinhDTBService.tinhDiemTrungBinh(bangDiem);

        // Assert: (8+7+9)/3 = 8.0
        assertEquals(8.0, bangDiem.getDiemTrungBinhMon());
    }

    @Test
    void testTinhDiemTrungBinh_DanhSachDiemRong() {
        // Arrange
        bangDiem.setChiTietDiemList(new ArrayList<>());

        // Act
        tinhDTBService.tinhDiemTrungBinh(bangDiem);

        // Assert: Không có điểm thì ĐTB phải là 0.0
        assertEquals(0.0, bangDiem.getDiemTrungBinhMon());
    }
    
    @Test
    void testTinhDiemTrungBinh_DanhSachDiemNull() {
        // Arrange
        bangDiem.setChiTietDiemList(null);

        // Act
        tinhDTBService.tinhDiemTrungBinh(bangDiem);

        // Assert: Danh sách null thì ĐTB phải là 0.0
        assertEquals(0.0, bangDiem.getDiemTrungBinhMon());
    }
}
