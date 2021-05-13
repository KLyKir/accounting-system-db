package server.database.repository;

import server.database.domain.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends Repository<Staff, Long>{
    Optional<List<Staff>> showStaffRelatedToSoftware(Long projectId) throws RepositoryException;
}
