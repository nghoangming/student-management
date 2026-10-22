package com.studentmanagement.service;

import com.studentmanagement.domain.ThamSoHeThong;
import com.studentmanagement.repository.ThamSoHeThongRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ThamSoHeThongService {
    private final ThamSoHeThongRepository repository;

    public ThamSoHeThongService(ThamSoHeThongRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ThamSoHeThong> danhSach() {
        return repository.findAll();
    }

    @Transactional
    public void luu(String ma, String ten, String giaTri, String moTa) {
        if (ma == null || !ma.matches("[A-Za-z][A-Za-z0-9_.-]{1,59}")
                || ten == null || ten.isBlank() || giaTri == null || giaTri.isBlank()) {
            throw new IllegalArgumentException("Mã, tên và giá trị tham số phải hợp lệ.");
        }
        ThamSoHeThong thamSo = repository.findById(ma).orElseGet(ThamSoHeThong::new);
        thamSo.setMaThamSo(ma);
        thamSo.setTenThamSo(ten.trim());
        thamSo.setGiaTri(giaTri.trim());
        thamSo.setMoTa(moTa == null ? "" : moTa.trim());
        repository.save(thamSo);
    }

    @Transactional
    public void xoa(String ma) {
        if (!repository.existsById(ma)) {
            throw new IllegalArgumentException("Không tìm thấy tham số.");
        }
        repository.deleteById(ma);
    }
}
