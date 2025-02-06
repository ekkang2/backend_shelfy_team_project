package com.shelfy.service;

import com.shelfy.dto.RecordStateDTO;
import com.shelfy.dto.ResponseDTO;
import com.shelfy.dto.request.CreateRecordReqDTO;
import com.shelfy.mapper.RecordMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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


    /**
     * 2025.02.06 박연화
     * 1. bookid, userid -> state 테이블 기록이 있는지?
     * 2-1. 데이터가 없으면 record insert
     * 2-2. 데이터가 있으면
     *      stateType이 reqData.stateType과 일치?
     *      2-2-1. throw "이미 동일한 상태의 기록이 있습니다." -> 이후 덮어쓰시겠습니까? 또다른 api요청으로 연계
     *      2-2-2. reqData.stateType에 따라 각 테이블에 동일한 stateId를 가진 데이터가 있는지?
     *              2-2-2-1.
     *
     *
     * 1. bookId, userId가 동일한 recordState 기록이 있는지 조회
     * 2-1. 있다면 -> req의 타입(카테고리)과 조회한 recordState의 타입이 일치하면 throw
     * 2-2. 있다면 -> 타입이 일치하지 않으면
     *          각 record 테이블에 같은 recordStateId를 가진 데이터가 있는지 조회
     *          2-2-1. 있으면 -> 해당 데이터 업데이트?
     *          2-2-2. 없으면 -> 새로운 데이터 insert
     *
     * @param reqDTO - 독서기록 데이터
     * @return responseDTO
     */
    public ResponseDTO createRecordState(CreateRecordReqDTO reqDTO) {
        RecordStateDTO recordState = recordMapper.selectByBookId(reqDTO);
        log.info("createRecordState 서비스 bookid 동일한 데이터 조회 " + recordState);
        int result = 0;//이거 여기 맞나?

        if (recordState != null) {
            if (recordState.rStateType != reqDTO.stateType) {
                // 레코드 테이블에 stateid가 동일한 컬럼이 있는지 조회?
//                if(stateid가 동일한 컬럼이 있다?){
//                    // 그 record를 update
//                }else{
//                    // 새로운 record를 insert
//                }

                // 레코드 테이블에 update
                reqDTO.setStateId(recordState.rStateId);
                result = createRecord(reqDTO);
            } else {
                throw new IllegalStateException("이미 동일한 상태의 기록이 존재합니다.");
            }
        } else {
            recordMapper.insertRecordState(reqDTO);
            // record 테이블에 insert
            result = createRecord(reqDTO);
        }



        if (result > 0) {
            return ResponseDTO.success(reqDTO);
        } else {
            return ResponseDTO.fail("기록 추가에 실패했습니다.");
        }
    }

    public int createRecord(CreateRecordReqDTO dto) {

        int result = 0;
        switch (dto.stateType) {
            case 1:
                result = recordMapper.insertDone(dto);
                break;
            case 2:
                result = recordMapper.insertDoing(dto);
                break;
            case 3:
                result = recordMapper.insertWish(dto);
                break;
            case 4:
                result = recordMapper.insertStop(dto);
                break;
            default:
                throw new IllegalArgumentException("잘못된 독서기록 타입입니다.");
        }
        log.info("createRecord 서비스 " + result);

        return result;
    }

//
//    public int updateRecord(CreateRecordReqDTO recordReqDTO) {
//
//        int result = 0;
//        switch (recordReqDTO.stateType) {
//            case 1:
//                result = recordMapper.insertDone(recordReqDTO);
//                break;
//            case 2:
//                result = recordMapper.insertDoing(recordReqDTO);
//                break;
//            case 3:
//                result = recordMapper.insertWish(recordReqDTO);
//                break;
//            case 4:
//                result = recordMapper.insertStop(recordReqDTO);
//                break;
//            default:
//                throw new IllegalArgumentException("잘못된 독서기록 타입입니다.");
//        }
//        log.info("updateRecord 서비스 " + result);
//
//        return result;
//    }


}
