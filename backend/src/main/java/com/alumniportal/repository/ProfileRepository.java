package com.alumniportal.repository;

import com.alumniportal.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO profiles (user_id, graduation_year, company, designation, bio, profile_pic_url) " +
                   "VALUES (:userId, :gradYear, :company, :designation, :bio, :picUrl) " +
                   "ON DUPLICATE KEY UPDATE " +
                   "graduation_year = :gradYear, company = :company, designation = :designation, bio = :bio, profile_pic_url = :picUrl", 
           nativeQuery = true)
    void upsertProfile(@Param("userId") Long userId, 
                       @Param("gradYear") Integer gradYear, 
                       @Param("company") String company, 
                       @Param("designation") String designation, 
                       @Param("bio") String bio, 
                       @Param("picUrl") String picUrl);
}
