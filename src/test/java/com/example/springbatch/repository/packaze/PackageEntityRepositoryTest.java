package com.example.springbatch.repository.packaze;

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
class PackageEntityRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @Test
    @DisplayName("packageSeq save 테스트 : seq값 자동 지정")
    public void test_save() throws Exception {
        //given
        PackageEntity packageEntity = new PackageEntity();
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

        PackageEntity packageEntityEntity1 = new PackageEntity();
        packageEntityEntity1.setPackageName("학생 전용 3개월");
        packageEntityEntity1.setPeriod(90);
        packageRepository.save(packageEntityEntity1);

        PackageEntity packageEntityEntity2 = new PackageEntity();
        packageEntityEntity2.setPackageName("학생 전용 6개월");
        packageEntityEntity2.setPeriod(180);
        packageRepository.save(packageEntityEntity2);

        //when
        final List<PackageEntity> packageEntityEntities = packageRepository.findByCreatedAtAfter(dateTime, PageRequest.of(0, 1, Sort.by("packageSeq").descending()));

        //then
        assertEquals(1, packageEntityEntities.size());
        assertEquals(packageEntityEntity2.getPackageSeq(), packageEntityEntities.get(0).getPackageSeq());
    }

    @Test
    @DisplayName("update 테스트")
    public void test_updateCountAndPeriod() throws Exception {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디프로필 이벤트 4개월");
        packageEntity.setPeriod(90);
        packageRepository.save(packageEntity);

        //when
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        final PackageEntity updatedPackageEntityEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();

        //then
        assertEquals(1, updatedCount);
        assertEquals(30, updatedPackageEntityEntity.getCount());
        assertEquals(120, updatedPackageEntityEntity.getPeriod());
    }

    @Test
    @DisplayName("삭제 테스트")
    public void test_delete() throws Exception {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("삭제할 이용권");
        packageEntity.setCount(1);
        PackageEntity newPackageEntityEntity = packageRepository.save(packageEntity);

        //when
        packageRepository.deleteById(newPackageEntityEntity.getPackageSeq());

        //then
        assertTrue(packageRepository.findById(newPackageEntityEntity.getPackageSeq()).isEmpty());
    }
}