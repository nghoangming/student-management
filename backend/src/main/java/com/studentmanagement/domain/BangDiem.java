package com.example.studentmanagement.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "BangDiem")
public class BangDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "maHocSinh")
    private Long maHocSinh;

    @Column(name = "maMonHoc")
    private Long maMonHoc;

    @Column(name = "maHocKy")
    private Long maHocKy;

    @Column(name = "diemTrungBinhMon")
    private Double diemTrungBinhMon;

    @OneToMany(mappedBy = "bangDiem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChiTietDiem> chiTietDiemList;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMaHocSinh() { return maHocSinh; }
    public void setMaHocSinh(Long maHocSinh) { this.maHocSinh = maHocSinh; }
    public Long getMaMonHoc() { return maMonHoc; }
    public void setMaMonHoc(Long maMonHoc) { this.maMonHoc = maMonHoc; }
    public Long getMaHocKy() { return maHocKy; }
    public void setMaHocKy(Long maHocKy) { this.maHocKy = maHocKy; }
    public Double getDiemTrungBinhMon() { return diemTrungBinhMon; }
    public void setDiemTrungBinhMon(Double diemTrungBinhMon) { this.diemTrungBinhMon = diemTrungBinhMon; }
    public List<ChiTietDiem> getChiTietDiemList() { return chiTietDiemList; }
    public void setChiTietDiemList(List<ChiTietDiem> chiTietDiemList) { this.chiTietDiemList = chiTietDiemList; }
}
