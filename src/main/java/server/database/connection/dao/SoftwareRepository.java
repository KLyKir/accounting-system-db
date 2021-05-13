package server.database.connection.dao;

import server.database.connection.domain.Software;
import server.database.connection.domain.Staff;

import java.util.List;
import java.util.Optional;

public interface SoftwareRepository extends Repository<Software, Long>{
    void addStaffToProject(Staff staff, Software software) throws DaoException;
    void removeStaffFromProject(Staff staff) throws DaoException;
    Optional<List<Software>> showSoftwareRelatedToStaff(Long staffId) throws DaoException;
}
