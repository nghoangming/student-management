package com.example.studentmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ket-qua")
public class KetQuaController {

    @GetMapping("/tra-cuu")
    public String traCuuKetQua(@RequestParam(required = false) Long maHocSinh, Model model) {
        if (maHocSinh != null) {
            // Mock truy xuất kết quả từ DB
            model.addAttribute("message", "Đã tìm thấy kết quả cho học sinh: " + maHocSinh);
        }
        return "ket-qua";
    }
}
