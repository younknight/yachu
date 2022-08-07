package game.yachu.repository;

import game.yachu.repository.dto.Record;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RecordRepositoryTest {

    @Autowired
    private RecordRepository recordRepository;

    @Test
    void save() {
        Record record1 = new Record("Lee", 200);
        Record record2 = new Record("Choi", 120);
        Record record3 = new Record("Kim", 150);

        recordRepository.save(record1);
        recordRepository.save(record2);
        recordRepository.save(record3);

        List<Record> findRecords = recordRepository.findTop10();
        assertThat(findRecords.get(0).getNickname()).isEqualTo(record1.getNickname());
        assertThat(findRecords.get(0).getScore()).isEqualTo(record1.getScore());
        assertThat(findRecords.get(1).getNickname()).isEqualTo(record3.getNickname());
        assertThat(findRecords.get(1).getScore()).isEqualTo(record3.getScore());
        assertThat(findRecords.get(2).getNickname()).isEqualTo(record2.getNickname());
        assertThat(findRecords.get(2).getScore()).isEqualTo(record2.getScore());
    }

    @Test
    void find_limit() {
        List<Record> records = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Record record = new Record("record" + i, i);
            recordRepository.save(record);
            records.add(record);
        }

        List<Record> findRecords = recordRepository.findTop10();
        for (int i = 0; i < 10; i++) {
            assertThat(findRecords.get(i).getNickname()).isEqualTo(records.get(records.size() - i - 1).getNickname());
            assertThat(findRecords.get(i).getScore()).isEqualTo(records.get(records.size() - i - 1).getScore());
        }
    }
}