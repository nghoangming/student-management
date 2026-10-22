package com.studentmanagement.service;

import com.studentmanagement.domain.HocSinh;
import com.studentmanagement.domain.LopHoc;
import com.studentmanagement.dto.BaoCaoTongKetDTO;
import com.studentmanagement.repository.HocSinhRepository;
import com.studentmanagement.repository.LopHocRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaoCaoServiceTest {
    @Mock private HocSinhRepository hocSinhRepository;
    @Mock private LopHocRepository lopHocRepository;
    @InjectMocks private BaoCaoService baoCaoService;

    @BeforeEach
    void setUp() {
        HocSinh a = hocSinh(1L, 10L);
        HocSinh b = hocSinh(2L, 10L);
        HocSinh c = hocSinh(3L, 20L);
        HocSinh chuaCoLop = hocSinh(4L, null);

        LopHoc lopA = lop(10L, "10A1");
        LopHoc lopB = lop(20L, "10A2");
        LopHoc lopC = lop(30L, "10A3");

        when(hocSinhRepository.findAll()).thenReturn(List.of(a, b, c, chuaCoLop));
        when(lopHocRepository.findAll()).thenReturn(List.of(lopA, lopB, lopC));
    }

    @Test
    void taoBaoCaoTongKet_demTongSoHocSinhVaLop() {
        BaoCaoTongKetDTO result = baoCaoService.taoBaoCaoTongKet();

        assertEquals(4, result.getTongSoHocSinh());
        assertEquals(3, result.getTongSoLop());
    }

    @Test
    void taoBaoCaoTongKet_demHocSinhTheoTungLopVaGiuLopRong() {
        BaoCaoTongKetDTO result = baoCaoService.taoBaoCaoTongKet();

        assertEquals(3, result.getThongKeTheoLop().size());
        assertEquals("10A1", result.getThongKeTheoLop().get(0).getTenLop());
        assertEquals(2, result.getThongKeTheoLop().get(0).getSoHocSinh());
        assertEquals(1, result.getThongKeTheoLop().get(1).getSoHocSinh());
        assertEquals(0, result.getThongKeTheoLop().get(2).getSoHocSinh());
    }

    @Test
    void taoBaoCaoTongKet_khongCoDuLieuTraVeDanhSachRong() {
        when(hocSinhRepository.findAll()).thenReturn(List.of());
        when(lopHocRepository.findAll()).thenReturn(List.of());

        BaoCaoTongKetDTO result = baoCaoService.taoBaoCaoTongKet();

        assertEquals(0, result.getTongSoHocSinh());
        assertEquals(0, result.getTongSoLop());
        assertEquals(0, result.getThongKeTheoLop().size());
    }

    private HocSinh hocSinh(Long ma, Long maLop) {
        HocSinh hocSinh = new HocSinh();
        hocSinh.setMaHocSinh(ma);
        hocSinh.setMaLop(maLop);
        return hocSinh;
    }

    private LopHoc lop(Long ma, String ten) {
        LopHoc lop = new LopHoc();
        lop.setMaLop(ma);
        lop.setTenLop(ten);
        return lop;
    }
}
