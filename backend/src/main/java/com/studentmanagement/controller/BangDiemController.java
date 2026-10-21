package com.example.studentmanagement.controller;

import com.example.studentmanagement.domain.BangDiem;
import com.example.studentmanagement.service.KiemTraDiemService;
import com.example.studentmanagement.service.TinhDTBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/diem")
public class BangDiemController {

    @Autowired
    private KiemTraDiemService kiemTraDiemService;

    @Autowired
    private TinhDTBService tinhDTBService;

    @GetMapping("/nhap")
    public String hienThiFormNhapDiem(Model model) {
        model.addAttribute("bangDiem", new BangDiem());
        return "nhap-diem";
    }

    @PostMapping("/luu")
    public String luuDiem(BangDiem bangDiem) {
        // Logic lưu điểm và tính ĐTB
        tinhDTBService.tinhDiemTrungBinh(bangDiem);
        // Save to repository (mocked in this skeleton)
        return "redirect:/diem/nhap?success";
    }
}
