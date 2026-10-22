package com.studentmanagement.service;

import com.studentmanagement.domain.HocSinh;
import com.studentmanagement.repository.HocSinhRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HocSinhService {
    private final HocSinhRepository repository;

    public HocSinhService(HocSinhRepository repository) {
        this.repository = repository;
    }

    public List<HocSinh> layDanhSach() {
        return repository.findAll();
    }

    public Optional<HocSinh> timTheoMa(Long maHocSinh) {
        return repository.findById(maHocSinh);
    }

    public HocSinh luu(HocSinh hocSinh) {
        return repository.save(hocSinh);
    }

    public void xoa(Long maHocSinh) {
        repository.deleteById(maHocSinh);
    }
}
