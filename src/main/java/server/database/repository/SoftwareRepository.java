package server.database.repository;

import server.database.domain.Software;
import server.database.domain.Staff;

import java.util.List;
import java.util.Optional;

public interface SoftwareRepository extends Repository<Software, Long>{
    void addStaffToProject(Staff staff, Software software) throws RepositoryException;
    void removeStaffFromProject(Staff staff) throws RepositoryException;
    Optional<List<Software>> showSoftwareRelatedToStaff(Long staffId) throws RepositoryException;
}
