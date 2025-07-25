package com.smsmode.media.dao.service.impl;

import com.smsmode.media.dao.repository.GuestRepository;
import com.smsmode.media.dao.service.GuestDaoService;
import com.smsmode.media.exception.ResourceNotFoundException;
import com.smsmode.media.exception.enumeration.ResourceNotFoundExceptionTitleEnum;
import com.smsmode.media.model.GuestModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestDaoServiceImpl implements GuestDaoService {

    private final GuestRepository guestRepository;

    @Override
    public GuestModel save(GuestModel guestModel) {
        return guestRepository.save(guestModel);
    }

    @Override
    public Page<GuestModel> findAllBy(Specification<GuestModel> specification, Pageable pageable) {
        return guestRepository.findAll(specification, pageable);
    }

    @Override
    public GuestModel findOneBy(Specification<GuestModel> specification) {
        return guestRepository.findOne(specification).orElseThrow(
                () -> {
                    log.debug("Couldn't find any guest with the specified criteria");
                    return new ResourceNotFoundException(
                            ResourceNotFoundExceptionTitleEnum.GUEST_NOT_FOUND,
                            "No guest found with the specified criteria");
                });
    }

    @Override
    public boolean existsById(String guestId) {
        return guestRepository.existsById(guestId);
    }

    @Override
    public void deleteById(String guestId) {
        guestRepository.deleteById(guestId);
    }

    @Override
    public void delete(GuestModel guest) {
        guestRepository.delete(guest);
    }
}