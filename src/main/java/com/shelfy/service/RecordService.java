package com.shelfy.service;

import com.shelfy.dto.request.CreateRecordReqDTO;
import com.shelfy.mapper.RecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
     날짜 : 2025/02/04
     이름 : 박연화
     내용 : RecordService 생성

*/
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class RecordService {

    private final RecordMapper recordMapper;

    public ResponseEntity insertRecord(CreateRecordReqDTO dto) {
        int optRecord = recordMapper.selectByBookIdAndStateType(dto);
        if(optRecord > 0){
            throw new IllegalStateException("이미 동일한 상태의 기록이 존재합니다.");
        }

        recordMapper.insertRecordState(dto);

        int result = 0;
        switch (dto.stateType){
            case 1 :
                result = recordMapper.insertDone(dto);
                break;
            case 2 :
                result = recordMapper.insertDoing(dto);
                break;
            case 3 :
                result = recordMapper.insertWish(dto);
                break;
            case 4 :
                result = recordMapper.insertStop(dto);
                break;
            default: throw new IllegalArgumentException("잘못된 상태 타입입니다.");
        }
        log.info("insertRecord 서비스 "+result);
        return ResponseEntity.ok()
                .body(result > 0 ? "책 기록이 성공적으로 추가되었습니다." : "책 기록 추가에 실패했습니다.");
    }
}
