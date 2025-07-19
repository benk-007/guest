package com.smsmode.media.dao.service;

import com.smsmode.media.model.GuestModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface GuestDaoService {
    GuestModel save(GuestModel guestModel);

    Page<GuestModel> findAllBy(Specification<GuestModel> specification, Pageable pageable);

    GuestModel findOneBy(Specification<GuestModel> specification);

    boolean existsById(String guestId);

    void deleteById(String guestId);

    void delete(GuestModel guest);

}