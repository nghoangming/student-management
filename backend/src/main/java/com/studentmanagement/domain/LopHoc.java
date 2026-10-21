package com.studentmanagement.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "LopHoc")
public class LopHoc {
    @Id
    private Long maLop;
    private String tenLop;
    private Integer siSo;
    private String trangThai;

    public LopHoc() {}

    public Long getMaLop() { return maLop; }
    public void setMaLop(Long maLop) { this.maLop = maLop; }
    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }
    public Integer getSiSo() { return siSo; }
    public void setSiSo(Integer siSo) { this.siSo = siSo; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
