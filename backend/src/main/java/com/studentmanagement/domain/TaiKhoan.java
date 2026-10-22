package com.example.studentmanagement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;

@Entity
@Table(name = "TaiKhoan", uniqueConstraints = @UniqueConstraint(columnNames = "tenDangNhap"))
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenDangNhap", nullable = false, length = 50)
    private String tenDangNhap;

    @Column(name = "matKhau", nullable = false, length = 100)
    @JsonIgnore
    private String matKhau;

    @Column(name = "vaiTro", nullable = false, length = 30)
    private String vaiTro;

    @Column(name = "hoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(nullable = false)
    private boolean kichHoat = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime ngayTao;

    @Column(nullable = false)
    private LocalDateTime ngayCapNhat;

    @PrePersist
    void onCreate() {
        ngayTao = LocalDateTime.now();
        ngayCapNhat = ngayTao;
    }

    @PreUpdate
    void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public boolean isKichHoat() { return kichHoat; }
    public void setKichHoat(boolean kichHoat) { this.kichHoat = kichHoat; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public LocalDateTime getNgayCapNhat() { return ngayCapNhat; }
}
