package com.shelfy.service;

import com.shelfy.document.BookDocument;
import com.shelfy.dto.record.*;
import com.shelfy.dto.ResponseDTO;
import com.shelfy.dto.ResponsePageDTO;
import com.shelfy.mapper.RecordMapper;
import com.shelfy.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final BookRepository bookRepository;

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
        // 1. bookId, userId로 state 테이블 조회
        RecordStateDTO recordState = recordMapper.selectStateByBookIdAndUserId(reqDTO);
        log.info("기존 record state 조회: " + recordState);
        int recordSuccess = 0;

        // 2. 기존 recordState가 있는 경우
        if (recordState != null) {
            log.info("기존 recordState 존재");

            // 2-1. 동일한 상태(stateType)인 경우 - 예외 처리
            if (recordState.rStateType == reqDTO.stateType) {
                log.warn("동일한 상태의 기록 존재");
                throw new IllegalStateException("동일한 상태의 기록이 이미 존재합니다.");
            }

            // 2-2. 다른 상태의 recordState인 경우
            reqDTO.setStateId(recordState.rStateId);
            RecordDataDTO existingRecord = findRecordByState(reqDTO);
            log.info("다른 상태 record 확인: " + existingRecord);

            if (existingRecord != null) {
                // 동일한 stateId를 가진 record가 이미 존재하는 경우 -> 에러 처리
                log.warn("다른 상태의 기록이 이미 존재합니다.");
                return ResponseDTO.builder()
                        .success(false)
                        .status(200)
                        .response(existingRecord)
                        .errorMessage("이전 상태의 기록이 존재합니다.")
                        .build();
            }

            // ✅ 2-3. 새로운 상태가 doing(2)일 경우, 기존 doing record 삭제
            if (recordState.rStateType == 2) {
                log.info("기존 doing record 삭제");
                recordMapper.deleteDoing(reqDTO.stateId);
            }

            // 2-4. 상태 업데이트 후 새로운 record 생성
            log.info("새로운 상태 반영 후 record 생성");
            recordState.setRStateType(reqDTO.stateType);
            recordMapper.updateRecordStateType(recordState);
            recordSuccess = createRecord(reqDTO);

            return ResponseDTO.success(recordSuccess);
        }

        // 3. recordState가 없는 경우 -> 새로운 state & record 생성
        log.info("새로운 recordState 생성");
        recordMapper.insertRecordState(reqDTO);
        recordSuccess = createRecord(reqDTO);

        return ResponseDTO.success(recordSuccess);
    }


    /**
     * 250207 박연화
     * 각 타입별 테이블에 같은 stateId를 가진 record가 있는지 조회하는 메서드
     *
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
     *
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
     * 독서기록 리스트 출력을 위한 메서드
     * 1. 타입별 독서기록 조회
     * 2. 독서기록 리스트에서 bookId 값을 꺼내 몽고디비에서 book 데이터 조회
     * 3. 페이징 처리
     *
     * @param userId, type, page, size
     * @return 페이징 처리한 독서기록 리스트
     */
    public ResponseDTO readRecordsBytypeFor10(int userId, int type, int page, int size) {

        int offset = page * size; // 몇 번째 데이터부터 가져올지 결정

        List<RecordRespDTO> recordList = new ArrayList<>();

        // 타입별 데이터 조회
        switch (type) {
            case 1:
                recordList = recordMapper.selectDoneRecordsByUserId(userId, size, offset);
                log.info("유저아이디 확인 : " + recordList.toString());
                break;
            case 2:
                recordList = recordMapper.selectDoingRecordsByUserId(userId, size, offset);
                break;
            case 3:
                recordList = recordMapper.selectWishRecordsByUserId(userId, size, offset);
                break;
            case 4:
                recordList = recordMapper.selectStopRecordsByUserId(userId, size, offset);
                break;
            default:
                throw new IllegalArgumentException("잘못된 독서기록 타입입니다.");
        }

        List<RecordRespDTO> recordBookList = matchBook(recordList);

        // 페이징 처리를 위한 총 데이터 갯수 조회
        int totalRecords = recordMapper.countRecordsByUserIdAndType(userId, type);

        int totalPages = (int) Math.ceil((double) totalRecords / size);
        boolean isFirst = page == 0;
        boolean isLast = (page + 1) >= totalPages;

        ResponsePageDTO<RecordRespDTO> pageList = new ResponsePageDTO<>(
                isFirst,
                isLast,
                page,
                size,
                totalPages,
                recordBookList
        );

        return ResponseDTO.success(pageList);
    }

    /**
     * 250211 박연화
     * 레코드 리스트에서 bookId를 가지고 몽고디비에 레코드 리스트와 매칭되는 책 데이터를 추가하여 리스트로 반환
     *
     * @param recordList
     * @return book데이터 추가한 recordRespDTO 리스트
     */
    private List<RecordRespDTO> matchBook(List<RecordRespDTO> recordList) {
        List<RecordRespDTO> bookRecordList = recordList.stream().map(record -> {
            Optional<BookDocument> optBook = bookRepository.findById(record.getBookId());
            if (optBook.isPresent()) {
                BookDocument book = optBook.get();
                record.setBookImage(book.getBookImage());
                record.setBookPage(book.getBookPage());
                record.setBookTitle(book.getBookTitle());
                record.setBookAuthor(book.getBookAuthor());
                record.setBookPublisher(book.getBookPublisher());
                record.setBookDesc(book.getBookDesc());
                record.setBookIsbn(book.getBookIsbn());
                record.isMyBook = false;
            }
            return record;
        }).toList();

        return bookRecordList;
    }

    /**
     * 250213 박연화
     * 유저별 독서기록 전체 조회
     *
     * @param userId
     * @return 1. 전체 독서기록 조회, 마이북과 연결되어있다면 마이북 데이터도 함께 조회
     * 2. 마이북 데이터가 있으면, isMyBook true로 업데이트
     * 3. 몽고디비에 있는 북 데이터 조회
     */
    public ResponseDTO readRecords(int userId) {
        List<RecordRespDTO> allRecords = recordMapper.selectStateByUserId(userId);
        List<RecordRespDTO> updatedRecords = allRecords.stream()
                .map(record -> {
                    if (!record.getBookId().isEmpty()) {
                        record.isMyBook = true;
                    }
                    return record;
                })
                .collect(Collectors.toList());
        List<RecordRespDTO> bookAndRecords = matchBook(updatedRecords);
        return ResponseDTO.success(bookAndRecords);
    }


    @Transactional
    public ResponseDTO deleteRecord(int stateId) {
        int count = 0;
        count += recordMapper.deleteDone(stateId);
        count += recordMapper.deleteDoing(stateId);
        count += recordMapper.deleteWish(stateId);
        count += recordMapper.deleteStop(stateId);
        count += recordMapper.deleteState(stateId);
        log.info("deleteRecord 서비스 : " + count);
        return ResponseDTO.success(count);
    }

    public ResponseDTO updateRecordByType(int stateId, int type, UpdateRecordDTO dto) {
        switch (type) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            default:
                break;
        }
        return null;
    }
}
