package server.database.repository;

import server.database.domain.Invoice;
import server.database.domain.Staff;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends Repository<Invoice, Long>{
    void addStaffToProject(Staff staff, Invoice software) throws RepositoryException;
    void removeStaffFromProject(Staff staff) throws RepositoryException;
    Optional<List<Invoice>> showSoftwareRelatedToStaff(Long staffId) throws RepositoryException;
}
