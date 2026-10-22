package com.studentmanagement.service;

import com.studentmanagement.domain.HocKy;
import com.studentmanagement.domain.MonHoc;
import com.studentmanagement.repository.HocKyRepository;
import com.studentmanagement.repository.MonHocRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanhMucService {
    private final MonHocRepository monHocRepository;
    private final HocKyRepository hocKyRepository;

    public DanhMucService(MonHocRepository monHocRepository, HocKyRepository hocKyRepository) {
        this.monHocRepository = monHocRepository;
        this.hocKyRepository = hocKyRepository;
    }

    public List<MonHoc> layDanhSachMonHoc() {
        return monHocRepository.findAll();
    }

    public List<HocKy> layDanhSachHocKy() {
        return hocKyRepository.findAll();
    }

    public MonHoc luuMonHoc(MonHoc monHoc) {
        return monHocRepository.save(monHoc);
    }

    public HocKy luuHocKy(HocKy hocKy) {
        return hocKyRepository.save(hocKy);
    }
}
