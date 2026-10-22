package com.studentmanagement.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MonHoc")
public class MonHoc {
    @Id
    private Long maMonHoc;
    private String tenMonHoc;
    private String moTa;
    private String trangThai;

    public MonHoc() {}

    public Long getMaMonHoc() { return maMonHoc; }
    public void setMaMonHoc(Long maMonHoc) { this.maMonHoc = maMonHoc; }
    public String getTenMonHoc() { return tenMonHoc; }
    public void setTenMonHoc(String tenMonHoc) { this.tenMonHoc = tenMonHoc; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
