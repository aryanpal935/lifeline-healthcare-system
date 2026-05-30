
package com.lifeline.lifeline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lifeline.lifeline.entity.EmergencyContact;
import com.lifeline.lifeline.entity.User;

@Repository
public interface EmergencyContactRepository
        extends JpaRepository<EmergencyContact, Long> {

    List<EmergencyContact> findByUser(User user);

    long countByUser(User user);

    // @Param added — fixes query parameter binding issue
    @Modifying
    @Query("update EmergencyContact e set e.primary = false where e.user = :user")
    void clearPrimaryContacts(@Param("user") User user);

    // direct DB query — no Java stream needed
    EmergencyContact findByUserAndPrimaryTrue(User user);
}
