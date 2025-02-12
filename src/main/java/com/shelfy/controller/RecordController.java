package com.shelfy.controller;

import com.shelfy.dto.ResponseDTO;
import com.shelfy.dto.record.RecordDTO;
import com.shelfy.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
     날짜 : 2025/02/04
     이름 : 박연화
     내용 : RecordController 생성 - book record 관련 컨트롤러

*/
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class RecordController {

    private final RecordService recordService;

    /*
        250204 - 박연화
        createRecord - 최초 기록 추가
        1. 책 번호, 유저아이디, 기록타입, 타입에 따른 데이터 dto로 받기
        2-1. r_state 테이블에 bookId, stateType이 동일한 기록이 있는지 검사
        2-2. 없으면 책, 유저, 기록타입은 r_state 테이블에 추가
        3. r_state_id 가지고 각 기록상태 테이블에 나머지 데이터 추가
        4. 데이터 추가가 성공적으로 완료되면 성공 상태 반환
     */
    @PostMapping("/record")
    public ResponseEntity createRecord(@RequestBody RecordDTO dto) {
        try {
            // TODO - userid 삭제
            dto.setUserId(10);
            log.info("createRecord 컨트롤러 "+dto.toString());
            ResponseDTO result = recordService.createRecordState(dto);
            return ResponseEntity.ok(result);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDTO.fail(e.getMessage()));
        }
    }

    /**
     * 250210 박연화
     * 1.
     * @param type, page
     * @return 페이징 처리한 타입별 레코드
     */
    @GetMapping("/records/{type}/{page}")
    public ResponseEntity readRecords(@PathVariable int type,
                                      @PathVariable int page) {
        try {
            //TODO - 유저아이디 박아놓은 거 나중에 지워야 함
            int userId = 10;
            int size = 10;
            ResponseDTO result = recordService.readRecords(userId, type, page, size);
            return ResponseEntity.ok(result);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseDTO.fail(e.getMessage()));
        }
    }
}
