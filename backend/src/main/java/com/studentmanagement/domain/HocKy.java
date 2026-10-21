package com.studentmanagement.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "HocKy")
public class HocKy {
    @Id
    private Long maHocKy;
    private String tenHocKy;
    private Integer namHoc;
    private String trangThai;

    public HocKy() {}

    public Long getMaHocKy() { return maHocKy; }
    public void setMaHocKy(Long maHocKy) { this.maHocKy = maHocKy; }
    public String getTenHocKy() { return tenHocKy; }
    public void setTenHocKy(String tenHocKy) { this.tenHocKy = tenHocKy; }
    public Integer getNamHoc() { return namHoc; }
    public void setNamHoc(Integer namHoc) { this.namHoc = namHoc; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
