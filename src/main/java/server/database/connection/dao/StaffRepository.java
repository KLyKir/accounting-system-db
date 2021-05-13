package server.database.connection.dao;

import server.database.connection.domain.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends Repository<Staff, Long>{
    Optional<List<Staff>> showStaffRelatedToSoftware(Long projectId) throws DaoException;
}
