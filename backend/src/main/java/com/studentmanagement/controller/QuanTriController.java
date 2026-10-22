package com.studentmanagement.controller;

import com.studentmanagement.domain.TaiKhoan;
import com.studentmanagement.domain.ThamSoHeThong;
import com.studentmanagement.repository.TaiKhoanRepository;
import com.studentmanagement.service.TaiKhoanService;
import com.studentmanagement.service.ThamSoHeThongService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/quan-tri")
public class QuanTriController {
    private final TaiKhoanService taiKhoanService;
    private final TaiKhoanRepository taiKhoanRepository;
    private final ThamSoHeThongService thamSoService;

    public QuanTriController(TaiKhoanService taiKhoanService,
            TaiKhoanRepository taiKhoanRepository, ThamSoHeThongService thamSoService) {
        this.taiKhoanService = taiKhoanService;
        this.taiKhoanRepository = taiKhoanRepository;
        this.thamSoService = thamSoService;
    }

    @GetMapping("/tai-khoan")
    public String taiKhoan(Model model, Authentication authentication) {
        model.addAttribute("taiKhoans", taiKhoanService.danhSach());
        model.addAttribute("tenDangNhapHienTai", authentication.getName());
        return "quan-tri/tai-khoan";
    }

    @PostMapping("/tai-khoan")
    public String taoTaiKhoan(@RequestParam String tenDangNhap, @RequestParam String hoTen,
            @RequestParam String vaiTro, @RequestParam String matKhau, RedirectAttributes flash) {
        try {
            taiKhoanService.tao(tenDangNhap, hoTen, vaiTro, matKhau);
            flash.addFlashAttribute("thongBao", "Đã tạo tài khoản.");
        } catch (IllegalArgumentException ex) {
            flash.addFlashAttribute("loi", ex.getMessage());
        }
        return "redirect:/quan-tri/tai-khoan";
    }

    @PostMapping("/tai-khoan/{id}")
    public String capNhatTaiKhoan(@PathVariable Long id, @RequestParam String hoTen,
            @RequestParam String vaiTro, @RequestParam(defaultValue = "false") boolean kichHoat,
            RedirectAttributes flash) {
        try {
            taiKhoanService.capNhat(id, hoTen, vaiTro, kichHoat);
            flash.addFlashAttribute("thongBao", "Đã cập nhật tài khoản.");
        } catch (IllegalArgumentException ex) {
            flash.addFlashAttribute("loi", ex.getMessage());
        }
        return "redirect:/quan-tri/tai-khoan";
    }

    @PostMapping("/tai-khoan/{id}/mat-khau")
    public String datLaiMatKhau(@PathVariable Long id, @RequestParam String matKhauMoi,
            RedirectAttributes flash) {
        try {
            taiKhoanService.datLaiMatKhau(id, matKhauMoi);
            flash.addFlashAttribute("thongBao", "Đã đặt lại mật khẩu.");
        } catch (IllegalArgumentException ex) {
            flash.addFlashAttribute("loi", ex.getMessage());
        }
        return "redirect:/quan-tri/tai-khoan";
    }

    @PostMapping("/tai-khoan/{id}/xoa")
    public String xoaTaiKhoan(@PathVariable Long id, Authentication authentication, RedirectAttributes flash) {
        try {
            TaiKhoan account = taiKhoanRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));
            if (account.getTenDangNhap().equals(authentication.getName())) {
                flash.addFlashAttribute("loi", "Không thể xóa tài khoản đang đăng nhập.");
                return "redirect:/quan-tri/tai-khoan";
            }
            if ("ADMIN".equals(account.getVaiTro()) && account.isKichHoat()
                    && taiKhoanRepository.findAll().stream()
                            .filter(TaiKhoan::isKichHoat)
                            .filter(row -> "ADMIN".equals(row.getVaiTro())).count() <= 1) {
                flash.addFlashAttribute("loi", "Hệ thống cần ít nhất một tài khoản quản trị đang hoạt động.");
                return "redirect:/quan-tri/tai-khoan";
            }
            taiKhoanRepository.delete(account);
            flash.addFlashAttribute("thongBao", "Đã xóa tài khoản.");
        } catch (IllegalArgumentException ex) {
            flash.addFlashAttribute("loi", ex.getMessage());
        }
        return "redirect:/quan-tri/tai-khoan";
    }

    @GetMapping("/tham-so")
    public String thamSo(Model model) {
        model.addAttribute("thamSos", thamSoService.danhSach());
        return "quan-tri/tham-so";
    }

    @PostMapping("/tham-so")
    public String luuThamSo(@RequestParam String maThamSo, @RequestParam String tenThamSo,
            @RequestParam String giaTri, @RequestParam(required = false) String moTa,
            RedirectAttributes flash) {
        try {
            thamSoService.luu(maThamSo, tenThamSo, giaTri, moTa);
            flash.addFlashAttribute("thongBao", "Đã lưu tham số.");
        } catch (IllegalArgumentException ex) {
            flash.addFlashAttribute("loi", ex.getMessage());
        }
        return "redirect:/quan-tri/tham-so";
    }

    @PostMapping("/tham-so/{ma}/xoa")
    public String xoaThamSo(@PathVariable String ma, RedirectAttributes flash) {
        try {
            thamSoService.xoa(ma);
            flash.addFlashAttribute("thongBao", "Đã xóa tham số.");
        } catch (IllegalArgumentException ex) {
            flash.addFlashAttribute("loi", ex.getMessage());
        }
        return "redirect:/quan-tri/tham-so";
    }
}
