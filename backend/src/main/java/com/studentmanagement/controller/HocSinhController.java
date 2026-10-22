package com.studentmanagement.controller;

import com.studentmanagement.domain.HocSinh;
import com.studentmanagement.service.HocSinhService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hoc-sinh")
public class HocSinhController {
    private final HocSinhService service;

    public HocSinhController(HocSinhService service) {
        this.service = service;
    }

    @GetMapping
    public List<HocSinh> layDanhSach() {
        return service.layDanhSach();
    }

    @GetMapping("/{maHocSinh}")
    public Object timTheoMa(@PathVariable Long maHocSinh) {
        return service.timTheoMa(maHocSinh).orElse(null);
    }

    @PostMapping
    public HocSinh tao(@RequestBody HocSinh hocSinh) {
        return service.luu(hocSinh);
    }

    @PutMapping("/{maHocSinh}")
    public HocSinh capNhat(@PathVariable Long maHocSinh, @RequestBody HocSinh hocSinh) {
        hocSinh.setMaHocSinh(maHocSinh);
        return service.luu(hocSinh);
    }

    @DeleteMapping("/{maHocSinh}")
    public void xoa(@PathVariable Long maHocSinh) {
        service.xoa(maHocSinh);
    }
}
