package com.shelfy.service;

import com.shelfy.dto.RecordDataDTO;
import com.shelfy.dto.RecordStateDTO;
import com.shelfy.dto.ResponseDTO;
import com.shelfy.dto.request.RecordDTO;
import com.shelfy.mapper.RecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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


    /**
     * 2025.02.06 박연화
     * 1. bookid, userid -> state 테이블 기록이 있는지?
     * 2-1. 데이터가 없으면 record insert
     * 2-2. 데이터가 있으면
     * stateType이 reqData.stateType과 일치?
     * 2-2-1. throw "이미 동일한 상태의 기록이 있습니다." -> 이후 덮어쓰시겠습니까? 또다른 api요청으로 연계
     * 2-2-2. reqData.stateType에 따라 각 테이블에 동일한 stateId를 가진 데이터가 있는지?
     * 2-2-2-1. throw "이미 동일한 상태의 기록이 있습니다." -> 이후 덮어쓰시겠습니까? 또다른 api요청으로 연계
     * 2-2-2-2. 새로운 record insert
     *
     * @param reqDTO - 독서기록 데이터
     * @return responseDTO
     */
    public ResponseDTO createRecordState(RecordDTO reqDTO) {
        RecordStateDTO recordState = recordMapper.selectStateByBookIdAndUserId(reqDTO);
        log.info("createRecordState 서비스 bookid 동일한 데이터 조회 " + recordState);
        int recordSuccess = 0;

        if (recordState != null) {
            log.info("데이터 있음" + recordState);

            if (recordState.rStateType != reqDTO.stateType) {
                reqDTO.setStateId(recordState.rStateId);
                RecordDataDTO recordData = findRecordByState(reqDTO); // 각 record 테이블에 같은 stateId를 가진 데이터가 있는지 조회
                log.info("각 record 테이블에 같은 stateId를 가진 데이터가 있는지 조회 " + recordData);

                if(recordData != null) {
                    log.info("active는 아닌데 같은 stateId를 가진 기록이 존재함");
                    return ResponseDTO.builder()
                            .success(false)
                            .status(200)
                            .response(recordData)
                            .errorMessage("이전 기록이 존재합니다.")
                            .build();
//                    throw new IllegalStateException("이전 기록이 존재합니다."); // 데이터를 덮어쓰시겠습니까?
                } else {
                    log.info("동일 stateId를 가진 기록이 없어서 record 생성");
                    recordState.setRStateType(reqDTO.stateType);
                    recordMapper.updateRecordStateType(recordState);
                    recordSuccess = createRecord(reqDTO);
                    return ResponseDTO.success(recordSuccess);
                }

            } else {
                log.info("동일한 상태의 기록이 존재합니다.");
                throw new IllegalStateException("동일한 상태의 기록이 존재합니다.");
            }
        } else {
            log.info("비어 있음" + recordState);
            recordMapper.insertRecordState(reqDTO); // 새로운 state 생성

            log.info("새로 입력한 데이터 : "+reqDTO);
            recordSuccess = createRecord(reqDTO); // 새로운 record 생성
            return ResponseDTO.success(recordSuccess);
        }
    }

    /**
     * 250207 박연화
     * 각 타입별 테이블에 같은 stateId를 가진 record가 있는지 조회하는 메서드
     * @param reqDTO
     * @return RecordDTO
     */
    private RecordDataDTO findRecordByState(RecordDTO reqDTO) {

        RecordDataDTO recordData;
        switch (reqDTO.stateType) {
            case 1:
                recordData = recordMapper.selectDoneByStateId(reqDTO);
                log.info("findRecordByState 1 done" + recordData);
                break;
            case 2:
                recordData = recordMapper.selectDoingByStateId(reqDTO);
                log.info("findRecordByState 2 doing" + recordData);
                break;
            case 3:
                recordData = recordMapper.selectWishByStateId(reqDTO);
                log.info("findRecordByState 3 wish" + recordData);
                break;
            case 4:
                recordData = recordMapper.selectStopByStateId(reqDTO);
                log.info("findRecordByState 4 stop" + recordData);
                break;
            default:
                throw new IllegalArgumentException("잘못된 독서기록 타입입니다.");
        }
        log.info("findRecordByState 서비스 - 조회 데이터 : " + recordData + " 타입: " + reqDTO.stateType);
        return recordData;
    }

    /**
     * 250206 박연화
     * 각 타입별 테이블에 새로운 레코드 생성
     * @param reqDTO
     * @return 레코드 생성 성공여부 (int)
     */
    public int createRecord(RecordDTO reqDTO) {

        int result = 0;
        reqDTO.setActive(1);

        switch (reqDTO.stateType) {
            case 1:
                result = recordMapper.insertDone(reqDTO);
                break;
            case 2:
                result = recordMapper.insertDoing(reqDTO);
                break;
            case 3:
                result = recordMapper.insertWish(reqDTO);
                break;
            case 4:
                result = recordMapper.insertStop(reqDTO);
                break;
            default:
                throw new IllegalArgumentException("잘못된 독서기록 타입입니다.");
        }
        log.info("createRecord 서비스 - 레코드 생성 성공여부 : " + result);

        return result;
    }

    /**
     * 250210 박연화
     * @param userId
     * @return responseDTO - stateList
     */
    public ResponseDTO readRecords(int userId, int type) {

        List<RecordDTO> recordList = new ArrayList<>();
        switch (type) {
            case 1:
                recordList = recordMapper.selectDoneByUserId(userId);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                throw new IllegalArgumentException("잘못된 독서기록 타입입니다.");
        }

        return null;
    }
}
