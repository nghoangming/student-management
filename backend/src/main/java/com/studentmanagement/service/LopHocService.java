package com.studentmanagement.service;

import com.studentmanagement.domain.LopHoc;
import com.studentmanagement.repository.LopHocRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LopHocService {
    private final LopHocRepository repository;

    public LopHocService(LopHocRepository repository) {
        this.repository = repository;
    }

    public List<LopHoc> layDanhSach() {
        return repository.findAll();
    }

    public LopHoc luu(LopHoc lopHoc) {
        return repository.save(lopHoc);
    }
}
