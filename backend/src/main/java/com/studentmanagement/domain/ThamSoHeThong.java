package com.example.studentmanagement.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ThamSoHeThong")
public class ThamSoHeThong {
    @Id
    @Column(name = "maThamSo", length = 60)
    private String maThamSo;

    @Column(name = "tenThamSo", nullable = false, length = 120)
    private String tenThamSo;

    @Column(name = "giaTri", nullable = false, length = 500)
    private String giaTri;

    @Column(length = 500)
    private String moTa;

    @Column(nullable = false)
    private LocalDateTime ngayCapNhat = LocalDateTime.now();

    @PreUpdate
    void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }

    public String getMaThamSo() { return maThamSo; }
    public void setMaThamSo(String maThamSo) { this.maThamSo = maThamSo; }
    public String getTenThamSo() { return tenThamSo; }
    public void setTenThamSo(String tenThamSo) { this.tenThamSo = tenThamSo; }
    public String getGiaTri() { return giaTri; }
    public void setGiaTri(String giaTri) { this.giaTri = giaTri; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public LocalDateTime getNgayCapNhat() { return ngayCapNhat; }
}
