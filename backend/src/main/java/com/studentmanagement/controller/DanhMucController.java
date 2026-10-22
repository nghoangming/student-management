package com.studentmanagement.controller;

import com.studentmanagement.domain.HocKy;
import com.studentmanagement.domain.MonHoc;
import com.studentmanagement.service.DanhMucService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danh-muc")
public class DanhMucController {
    private final DanhMucService service;

    public DanhMucController(DanhMucService service) {
        this.service = service;
    }

    @GetMapping("/mon-hoc")
    public List<MonHoc> layMonHoc() {
        return service.layDanhSachMonHoc();
    }

    @PostMapping("/mon-hoc")
    public MonHoc taoMonHoc(@RequestBody MonHoc monHoc) {
        return service.luuMonHoc(monHoc);
    }

    @GetMapping("/hoc-ky")
    public List<HocKy> layHocKy() {
        return service.layDanhSachHocKy();
    }

    @PostMapping("/hoc-ky")
    public HocKy taoHocKy(@RequestBody HocKy hocKy) {
        return service.luuHocKy(hocKy);
    }
}
