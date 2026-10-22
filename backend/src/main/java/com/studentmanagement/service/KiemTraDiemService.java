package com.studentmanagement.service;

import org.springframework.stereotype.Service;

@Service
public class KiemTraDiemService {
    
    /**
     * Dịch vụ quy tắc kiểm tra điểm hợp lệ (từ 0 đến 10)
     */
    public boolean kiemTraDiemHopLe(Double diem) {
        if (diem == null) return false;
        return diem >= 0.0 && diem <= 10.0;
    }
}
