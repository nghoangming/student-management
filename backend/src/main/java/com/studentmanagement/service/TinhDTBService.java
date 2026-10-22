package com.studentmanagement.service;

import com.studentmanagement.domain.BangDiem;
import com.studentmanagement.domain.ChiTietDiem;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TinhDTBService {
    /**
     * Tính điểm trung bình môn
     * ĐTB của ChiTietDiem là trung bình cộng điểm thành phần.
     * ĐTB môn của BangDiem là trung bình cộng các ChiTietDiem.
     */
    public void tinhDiemTrungBinh(BangDiem bangDiem) {
        if (bangDiem == null || bangDiem.getChiTietDiemList() == null || bangDiem.getChiTietDiemList().isEmpty()) {
            bangDiem.setDiemTrungBinhMon(0.0);
            return;
        }

        List<ChiTietDiem> chiTietDiemList = bangDiem.getChiTietDiemList();
        double tongDiem = 0.0;
        int soDiem = chiTietDiemList.size();

        for (ChiTietDiem ctd : chiTietDiemList) {
            tongDiem += (ctd.getDiem() != null) ? ctd.getDiem() : 0.0;
        }

        double dtb = tongDiem / soDiem;
        bangDiem.setDiemTrungBinhMon((double) Math.round(dtb * 100) / 100);
    }
}
