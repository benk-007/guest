/**
 * <p>Copyright (C) Calade Technologies, Inc - All Rights Reserved Unauthorized copying of this
 * file, via any medium is strictly prohibited Proprietary and confidential
 */
package com.smsmode.guest.service.impl;

import com.smsmode.guest.dao.service.SegmentDaoService;
import com.smsmode.guest.model.SegmentModel;
import com.smsmode.guest.service.DataLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * TODO: add your documentation
 *
 * @author hamzahabchi (contact: hamza.habchi@messaging-technologies.com)
 * <p>Created 22 Jul 2025</p>
 */
@Service
@RequiredArgsConstructor
public class DataLoaderServiceImpl implements DataLoaderService, CommandLineRunner {

    private final SegmentDaoService segmentDaoService;

    @Override
    public void run(String... args) throws Exception {
        this.createSegments();
    }


    @Override
    public void createSegments() {
        SegmentModel b2bSegment = new SegmentModel();
        b2bSegment.setName("B2B");
        b2bSegment.setEnabled(true);
        b2bSegment = segmentDaoService.save(b2bSegment);

        SegmentModel b2bOfflineSegment = new SegmentModel();
        b2bOfflineSegment.setName("Offline");
        b2bOfflineSegment.setEnabled(true);
        b2bOfflineSegment.setParent(b2bSegment);
        b2bOfflineSegment = segmentDaoService.save(b2bOfflineSegment);

        SegmentModel b2cSegment = new SegmentModel();
        b2cSegment.setName("B2C");
        b2cSegment.setEnabled(true);
        b2cSegment = segmentDaoService.save(b2cSegment);

        SegmentModel b2cFrontDesk = new SegmentModel();
        b2cFrontDesk.setName("FrontDesk");
        b2cFrontDesk.setEnabled(true);
        b2cFrontDesk.setParent(b2cSegment);
        b2cFrontDesk = segmentDaoService.save(b2cFrontDesk);

        SegmentModel b2cPhone = new SegmentModel();
        b2cPhone.setName("Phone");
        b2cPhone.setEnabled(true);
        b2cPhone.setParent(b2cSegment);
        b2cPhone = segmentDaoService.save(b2cPhone);

        SegmentModel b2cWebsite = new SegmentModel();
        b2cWebsite.setName("Website");
        b2cWebsite.setEnabled(true);
        b2cWebsite.setParent(b2cSegment);
        b2cWebsite = segmentDaoService.save(b2cWebsite);
    }
}
