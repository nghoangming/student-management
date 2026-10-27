package com.studentmanagement.service;

import com.studentmanagement.domain.HocSinh;
import com.studentmanagement.domain.LopHoc;
import com.studentmanagement.dto.BaoCaoTongKetDTO;
import com.studentmanagement.repository.HocSinhRepository;
import com.studentmanagement.repository.LopHocRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Tong hop so lieu co san trong mo hinh hien tai.
 * Bao cao diem/ket qua hoc tap chua the tinh vi repository chua co entity BangDiem.
 */
@Service
public class BaoCaoService {
    private final HocSinhRepository hocSinhRepository;
    private final LopHocRepository lopHocRepository;

    public BaoCaoService(HocSinhRepository hocSinhRepository,
                         LopHocRepository lopHocRepository) {
        this.hocSinhRepository = hocSinhRepository;
        this.lopHocRepository = lopHocRepository;
    }

    public BaoCaoTongKetDTO taoBaoCaoTongKet() {
        List<HocSinh> hocSinhs = hocSinhRepository.findAll();
        List<LopHoc> lopHocs = lopHocRepository.findAll();

        Map<Long, String> tenLopTheoMa = lopHocs.stream()
                .filter(lop -> lop.getMaLop() != null)
                .collect(Collectors.toMap(
                        LopHoc::getMaLop,
                        lop -> lop.getTenLop() == null ? "" : lop.getTenLop(),
                        (first, ignored) -> first));

        Map<Long, Long> soHocSinhTheoLop = hocSinhs.stream()
                .map(HocSinh::getMaLop)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<BaoCaoTongKetDTO.ThongKeLopDTO> thongKe = lopHocs.stream()
                .map(lop -> new BaoCaoTongKetDTO.ThongKeLopDTO(
                        lop.getMaLop(),
                        lop.getTenLop(),
                        soHocSinhTheoLop.getOrDefault(lop.getMaLop(), 0L)))
                .toList();

        // Hoc sinh chua gan lop duoc tinh trong tong so hoc sinh, nhung khong thuoc thong ke lop.
        return new BaoCaoTongKetDTO(hocSinhs.size(), lopHocs.size(), thongKe);
    }
}
