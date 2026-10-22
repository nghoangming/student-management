package com.example.studentmanagement.service;

import com.example.studentmanagement.domain.TaiKhoan;
import com.example.studentmanagement.repository.TaiKhoanRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaiKhoanService {
    private static final List<String> VAI_TRO_HOP_LE = List.of("ADMIN", "GIAO_VIEN", "CAN_BO");

    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;

    public TaiKhoanService(TaiKhoanRepository taiKhoanRepository, PasswordEncoder passwordEncoder) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<TaiKhoan> danhSach() {
        return taiKhoanRepository.findAll();
    }

    @Transactional
    public TaiKhoan tao(String tenDangNhap, String hoTen, String vaiTro, String matKhau) {
        String username = tenDangNhap.trim();
        if (username.isEmpty() || hoTen.isBlank() || matKhau == null || matKhau.length() < 8) {
            throw new IllegalArgumentException("Tên, họ tên và mật khẩu hợp lệ là bắt buộc (mật khẩu từ 8 ký tự).");
        }
        if (taiKhoanRepository.existsByTenDangNhap(username)) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");
        }
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(username);
        taiKhoan.setHoTen(hoTen.trim());
        taiKhoan.setVaiTro(chuanHoaVaiTro(vaiTro));
        taiKhoan.setMatKhau(passwordEncoder.encode(matKhau));
        return taiKhoanRepository.save(taiKhoan);
    }

    @Transactional
    public void capNhat(Long id, String hoTen, String vaiTro, boolean kichHoat) {
        TaiKhoan taiKhoan = timTaiKhoan(id);
        if (hoTen == null || hoTen.isBlank()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        String vaiTroMoi = chuanHoaVaiTro(vaiTro);
        if ("ADMIN".equals(taiKhoan.getVaiTro())
                && (!kichHoat || !"ADMIN".equals(vaiTroMoi))
                && taiKhoanRepository.findAll().stream()
                        .filter(TaiKhoan::isKichHoat)
                        .filter(account -> "ADMIN".equals(account.getVaiTro()))
                        .count() <= 1) {
            throw new IllegalArgumentException("Hệ thống cần ít nhất một tài khoản quản trị đang hoạt động.");
        }
        taiKhoan.setHoTen(hoTen.trim());
        taiKhoan.setVaiTro(vaiTroMoi);
        taiKhoan.setKichHoat(kichHoat);
        taiKhoanRepository.save(taiKhoan);
    }

    @Transactional
    public void datLaiMatKhau(Long id, String matKhauMoi) {
        if (matKhauMoi == null || matKhauMoi.length() < 8) {
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 8 ký tự.");
        }
        TaiKhoan taiKhoan = timTaiKhoan(id);
        taiKhoan.setMatKhau(passwordEncoder.encode(matKhauMoi));
        taiKhoanRepository.save(taiKhoan);
    }

    @Transactional
    public void doiMatKhau(String tenDangNhap, String matKhauCu, String matKhauMoi) {
        if (matKhauMoi == null || matKhauMoi.length() < 8) {
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 8 ký tự.");
        }
        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));
        if (!passwordEncoder.matches(matKhauCu, taiKhoan.getMatKhau())) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không chính xác.");
        }
        taiKhoan.setMatKhau(passwordEncoder.encode(matKhauMoi));
        taiKhoanRepository.save(taiKhoan);
    }

    private TaiKhoan timTaiKhoan(Long id) {
        return taiKhoanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));
    }

    private String chuanHoaVaiTro(String vaiTro) {
        String role = vaiTro == null ? "" : vaiTro.trim().toUpperCase(Locale.ROOT);
        if (!VAI_TRO_HOP_LE.contains(role)) {
            throw new IllegalArgumentException("Vai trò không hợp lệ.");
        }
        return role;
    }
}
