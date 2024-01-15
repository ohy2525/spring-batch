package com.example.springbatch.job.pass;

import com.example.springbatch.repository.pass.*;
import com.example.springbatch.repository.user.UserGroupMappingEntity;
import com.example.springbatch.repository.user.UserGroupMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AddPassesTasklet implements Tasklet {

    private final PassRepository passRepository;
    private final BulkPassRepository bulkPassRepository;
    private final UserGroupMappingRepository userGroupMappingRepository;

    public AddPassesTasklet(PassRepository passRepository, BulkPassRepository bulkPassRepository, UserGroupMappingRepository userGroupMappingRepository) {
        this.passRepository = passRepository;
        this.bulkPassRepository = bulkPassRepository;
        this.userGroupMappingRepository = userGroupMappingRepository;
    }

    /**
     * 이용권 시작 일시 1일 전 user group 내 각 사용자에게 이용권을 추가해 준다.
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
        final List<BulkPassEntity> bulkPassEntityList = bulkPassRepository.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startedAt);

        int count = 0;
        for (BulkPassEntity bulkPassEntity : bulkPassEntityList) {
            //userGroup 에 속한 userId 조회
            final List<String> userIds = userGroupMappingRepository.findByUserGroupId(bulkPassEntity.getUserGroupId())
                    .stream().map(UserGroupMappingEntity::getUserId).toList();

            // 각 userId로 이용권 추가
            count += addPasses(bulkPassEntity, userIds);
            // pass 추가 이후 상태를 COMPLETED로 업데이트
            bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
        }

        log.info("AddPassesTasklet - execute: 이용권 {}건 추가 완료, startedAt={}", count, startedAt);

        //CONTINUABLE : 처리를 계속할 수 있음, Spring Batch에게 해당 Tasklet을 다시 실행하도록 정의
        //FINISHED : 처리가 완료되었음. 처리의 성공 여부에 관계없이 Tasklet의 처리를 완료하고 다음 처리를 진행
        return RepeatStatus.FINISHED;
    }

    private int addPasses(BulkPassEntity bulkPassEntity, List<String> userIds) {
        List<PassEntity> passEntities = new ArrayList<>();
        for (String userId : userIds) {
            PassEntity passEntity = PassModelMapper.INSTANCE.toPassEntity(bulkPassEntity, userId);
            passEntities.add(passEntity);
        }

        return passRepository.saveAll(passEntities).size();
    }
}
