package com.studentmanagement.dto;

import java.util.List;

/**
 * Du lieu tong hop cho man hinh bao cao.
 * Pham vi hien tai dua tren cac bang HocSinh va LopHoc co trong repository.
 */
public class BaoCaoTongKetDTO {
    private final long tongSoHocSinh;
    private final long tongSoLop;
    private final List<ThongKeLopDTO> thongKeTheoLop;

    public BaoCaoTongKetDTO(long tongSoHocSinh, long tongSoLop,
                            List<ThongKeLopDTO> thongKeTheoLop) {
        this.tongSoHocSinh = tongSoHocSinh;
        this.tongSoLop = tongSoLop;
        this.thongKeTheoLop = List.copyOf(thongKeTheoLop);
    }

    public long getTongSoHocSinh() { return tongSoHocSinh; }
    public long getTongSoLop() { return tongSoLop; }
    public List<ThongKeLopDTO> getThongKeTheoLop() { return thongKeTheoLop; }

    public static class ThongKeLopDTO {
        private final Long maLop;
        private final String tenLop;
        private final long soHocSinh;

        public ThongKeLopDTO(Long maLop, String tenLop, long soHocSinh) {
            this.maLop = maLop;
            this.tenLop = tenLop;
            this.soHocSinh = soHocSinh;
        }

        public Long getMaLop() { return maLop; }
        public String getTenLop() { return tenLop; }
        public long getSoHocSinh() { return soHocSinh; }
    }
}
