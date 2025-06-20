
package com.example.Baja.Repo;

import com.example.Baja.Entity.OnlineClassLinkEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlineClassLinkRepo extends JpaRepository<OnlineClassLinkEntity, Long> {
    // We'll rely on findAll() and then filter/get the first element in the service/controller
    // if we are strictly managing a single active link.
    Optional<OnlineClassLinkEntity> findTopByOrderByIdDesc();
    

}