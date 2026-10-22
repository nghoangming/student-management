package com.studentmanagement.controller;

import com.studentmanagement.domain.LopHoc;
import com.studentmanagement.service.LopHocService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lop-hoc")
public class LopHocController {
    private final LopHocService service;

    public LopHocController(LopHocService service) {
        this.service = service;
    }

    @GetMapping
    public List<LopHoc> layDanhSach() {
        return service.layDanhSach();
    }

    @PostMapping
    public LopHoc tao(@RequestBody LopHoc lopHoc) {
        return service.luu(lopHoc);
    }
}
