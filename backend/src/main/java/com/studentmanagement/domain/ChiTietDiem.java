package com.example.studentmanagement.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietDiem")
public class ChiTietDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bangDiem_id")
    private BangDiem bangDiem;

    @Column(name = "loaiDiem")
    private String loaiDiem; // VD: "15_PHUT", "1_TIET", "CUOI_KY"

    @Column(name = "diem")
    private Double diem;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BangDiem getBangDiem() { return bangDiem; }
    public void setBangDiem(BangDiem bangDiem) { this.bangDiem = bangDiem; }
    public String getLoaiDiem() { return loaiDiem; }
    public void setLoaiDiem(String loaiDiem) { this.loaiDiem = loaiDiem; }
    public Double getDiem() { return diem; }
    public void setDiem(Double diem) { this.diem = diem; }
}
