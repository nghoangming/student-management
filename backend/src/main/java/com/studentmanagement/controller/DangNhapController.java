package com.studentmanagement.controller;

import com.studentmanagement.service.TaiKhoanService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DangNhapController {
    private final TaiKhoanService taiKhoanService;

    public DangNhapController(TaiKhoanService taiKhoanService) {
        this.taiKhoanService = taiKhoanService;
    }

    @GetMapping("/dang-nhap")
    public String dangNhap() {
        return "dang-nhap";
    }

    @GetMapping("/")
    public String trangChu(Authentication authentication) {
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/quan-tri/tai-khoan";
        }
        return "redirect:/doi-mat-khau";
    }

    @GetMapping("/doi-mat-khau")
    public String formDoiMatKhau() {
        return "doi-mat-khau";
    }

    @PostMapping("/doi-mat-khau")
    public String doiMatKhau(Authentication authentication,
            @RequestParam String matKhauCu, @RequestParam String matKhauMoi,
            @RequestParam String xacNhanMatKhau, RedirectAttributes redirectAttributes) {
        if (!matKhauMoi.equals(xacNhanMatKhau)) {
            redirectAttributes.addFlashAttribute("loi", "Xác nhận mật khẩu mới không khớp.");
            return "redirect:/doi-mat-khau";
        }
        try {
            taiKhoanService.doiMatKhau(authentication.getName(), matKhauCu, matKhauMoi);
            redirectAttributes.addFlashAttribute("thongBao", "Đã đổi mật khẩu.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("loi", ex.getMessage());
        }
        return "redirect:/doi-mat-khau";
    }
}
