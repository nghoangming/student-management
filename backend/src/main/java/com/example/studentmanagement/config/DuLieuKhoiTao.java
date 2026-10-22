package com.example.studentmanagement.config;

import com.example.studentmanagement.domain.TaiKhoan;
import com.example.studentmanagement.domain.ThamSoHeThong;
import com.example.studentmanagement.repository.TaiKhoanRepository;
import com.example.studentmanagement.repository.ThamSoHeThongRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DuLieuKhoiTao {
    @Bean
    CommandLineRunner khoiTaoDuLieu(TaiKhoanRepository taiKhoanRepository,
            ThamSoHeThongRepository thamSoRepository, PasswordEncoder passwordEncoder,
            @Value("${app.admin.username:admin}") String adminUsername,
            @Value("${app.admin.password:}") String adminPassword) {
        return args -> {
            if (taiKhoanRepository.count() == 0) {
                if (adminPassword.isBlank()) {
                    throw new IllegalStateException("Cần đặt biến môi trường APP_ADMIN_PASSWORD trước khi chạy lần đầu.");
                }
                TaiKhoan admin = new TaiKhoan();
                admin.setTenDangNhap(adminUsername);
                admin.setHoTen("Quản trị viên");
                admin.setVaiTro("ADMIN");
                admin.setMatKhau(passwordEncoder.encode(adminPassword));
                taiKhoanRepository.save(admin);
            }
            if (thamSoRepository.count() == 0) {
                themThamSo(thamSoRepository, "TUOI_TOI_THIEU", "Tuổi tối thiểu", "6",
                        "Tuổi nhỏ nhất được tiếp nhận vào trường.");
                themThamSo(thamSoRepository, "TUOI_TOI_DA", "Tuổi tối đa", "18",
                        "Tuổi lớn nhất được tiếp nhận vào trường.");
                themThamSo(thamSoRepository, "SI_SO_TOI_DA", "Sĩ số tối đa", "45",
                        "Sĩ số tối đa của một lớp học.");
                themThamSo(thamSoRepository, "DIEM_DAT", "Điểm đạt", "5",
                        "Điểm trung bình môn tối thiểu để đạt.");
            }
        };
    }

    private void themThamSo(ThamSoHeThongRepository repository,
            String ma, String ten, String giaTri, String moTa) {
        ThamSoHeThong thamSo = new ThamSoHeThong();
        thamSo.setMaThamSo(ma);
        thamSo.setTenThamSo(ten);
        thamSo.setGiaTri(giaTri);
        thamSo.setMoTa(moTa);
        repository.save(thamSo);
    }
}
