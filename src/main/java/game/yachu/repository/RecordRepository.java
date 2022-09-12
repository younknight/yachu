package game.yachu.repository;

import game.yachu.repository.dto.Record;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RecordRepository {

    private final DataSource dataSource;

    public void save(Record record) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final String sql = "insert into records(nickname, score, datetime) values(?, ?, ?)";
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, RETURN_GENERATED_KEYS);
            pstmt.setString(1, record.getNickname());
            pstmt.setInt(2, record.getScore());
            pstmt.setTimestamp(3, Timestamp.valueOf(record.getDateTime()));
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                Long id = rs.getLong(1);
                record.setId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    public List<Record> findTop10() {
        List<Record> records = new ArrayList<>(10);

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final String sql = "select * from records order by score desc limit 10";
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong(1);
                String nickname = rs.getString(2);
                int score = rs.getInt(3);
                LocalDateTime datetime = rs.getTimestamp(4).toLocalDateTime();
                Record record = new Record(id, nickname, score, datetime);
                records.add(record);
            }
            return records;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(pstmt);
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}
