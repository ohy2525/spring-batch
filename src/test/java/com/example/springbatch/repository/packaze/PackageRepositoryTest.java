package com.example.springbatch.repository.packaze;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class PackageRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @Test
    @DisplayName("packageSeq save 테스트 : seq값 자동 지정")
    public void test_save() throws Exception {
        //given
        Package packageEntity = new Package();
        packageEntity.setPackageName("바디 챌린지 PT 12주");
        packageEntity.setPeriod(84);

        //when
        packageRepository.save(packageEntity);

        //then
        assertNotNull(packageEntity.getPackageSeq());
    }

    @Test
    @DisplayName("createdAt 테스트 : 1분전 생성된 package 조회")
    public void test_findByCreatedAtAfter() throws Exception {
        //given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);

        Package packageEntity1 = new Package();
        packageEntity1.setPackageName("학생 전용 3개월");
        packageEntity1.setPeriod(90);
        packageRepository.save(packageEntity1);

        Package packageEntity2 = new Package();
        packageEntity2.setPackageName("학생 전용 6개월");
        packageEntity2.setPeriod(180);
        packageRepository.save(packageEntity2);

        //when
        final List<Package> packageEntities = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq").descending()));

        //then
        assertEquals(1, packageEntities.size());
        assertEquals(packageEntity2.getPackageSeq(), packageEntities.get(0).getPackageSeq());
    }

    @Test
    @DisplayName("update 테스트")
    public void test_updateCountAndPeriod() throws Exception {
        //given
        Package packageEntity = new Package();
        packageEntity.setPackageName("바디프로필 이벤트 4개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        //when
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        final Package updatedPackageEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        //then
        assertEquals(1, updatedCount);
        assertEquals(30, updatedPackageEntity.getCount());
        assertEquals(120, updatedPackageEntity.getPeriod());
    }

    @Test
    @DisplayName("삭제 테스트")
    public void test_delete() throws Exception {
        //given
        Package packageEntity = new Package();
        packageEntity.setPackageName("삭제할 이용권");
        packageEntity.setCount(1);
        Package newPackageEntity = packageRepository.save(packageEntity);

        //when
        packageRepository.deleteById(newPackageEntity.getPackageSeq());

        //then
        assertTrue(packageRepository.findById(newPackageEntity.getPackageSeq()).isEmpty());
    }
}