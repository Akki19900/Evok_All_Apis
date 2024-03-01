package com.npst.evok.api.evok_apis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.npst.evok.api.evok_apis.entity.TxnStatusEntity;

public interface TxnStatusRepository extends JpaRepository<TxnStatusEntity, Integer> {

}
