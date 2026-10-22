package com.studentmanagement.repository;

import com.studentmanagement.domain.BangDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BangDiemRepository extends JpaRepository<BangDiem, Long> {
    List<BangDiem> findByMaHocSinh(Long maHocSinh);
    List<BangDiem> findByMaHocSinhAndMaHocKy(Long maHocSinh, Long maHocKy);
}
