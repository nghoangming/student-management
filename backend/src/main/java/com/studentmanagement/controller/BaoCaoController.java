package com.studentmanagement.controller;

import com.studentmanagement.dto.BaoCaoTongKetDTO;
import com.studentmanagement.service.BaoCaoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bao-cao")
public class BaoCaoController {
    private final BaoCaoService baoCaoService;

    public BaoCaoController(BaoCaoService baoCaoService) {
        this.baoCaoService = baoCaoService;
    }

    /** Bao cao tong ket so luong hoc sinh va phan bo hoc sinh theo lop. */
    @GetMapping("/tong-ket")
    public BaoCaoTongKetDTO layBaoCaoTongKet() {
        return baoCaoService.taoBaoCaoTongKet();
    }
}
