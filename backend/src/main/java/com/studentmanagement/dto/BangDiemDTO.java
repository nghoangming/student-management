package com.studentmanagement.dto;

public class BangDiemDTO {
    private Long maHocSinh;
    private Long maMonHoc;
    private Double diem15p;
    private Double diem1tiet;
    private Double diemCuoiKy;

    // Getters and Setters
    public Long getMaHocSinh() { return maHocSinh; }
    public void setMaHocSinh(Long maHocSinh) { this.maHocSinh = maHocSinh; }
    public Long getMaMonHoc() { return maMonHoc; }
    public void setMaMonHoc(Long maMonHoc) { this.maMonHoc = maMonHoc; }
    public Double getDiem15p() { return diem15p; }
    public void setDiem15p(Double diem15p) { this.diem15p = diem15p; }
    public Double getDiem1tiet() { return diem1tiet; }
    public void setDiem1tiet(Double diem1tiet) { this.diem1tiet = diem1tiet; }
    public Double getDiemCuoiKy() { return diemCuoiKy; }
    public void setDiemCuoiKy(Double diemCuoiKy) { this.diemCuoiKy = diemCuoiKy; }
}
