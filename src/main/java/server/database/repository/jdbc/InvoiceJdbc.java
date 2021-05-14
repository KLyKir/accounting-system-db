package server.database.repository.jdbc;

import server.database.domain.Invoice;
import server.database.repository.ConnectionFactory;
import server.database.repository.RepositoryException;
import server.database.repository.InvoiceRepository;
import server.database.domain.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceJdbc implements InvoiceRepository {
    private static final String CREATE_INVOICE = "insert into accounting_company.invoice(order,price) values(?,?);";

    private static final String DELETE_INVOICE = "delete from accounting_company.invoice " +
            "where id = ?;";

    private static final String UPDATE_INVOICE = "update accounting_company.invoice set " +
            "order_name = ?," +
            "price = ?" +
            "where id = ?;";

    private static final String FIND_INVOICE = "select * from accounting_company.invoice;";
    private static final String ADD_STAFF_TO_INVOICE = "insert into accounting_company.staff_invoice(staff_id, invoice_id) values (?,?);";
    private static final String REMOVE_STAFF_FROM_INVOICE = "delete from accounting_company.staff_invoice where staff_id = ?;";
    private static final String FIND_INVOICE_RELATED_TO_STAFF = "select id, " +
            "       order_name, " +
            "       price " +
            "from accounting_company.staff_invoice " +
            "         inner join accounting_company.invoice i on i.id = staff_invoice.invoice_id " +
            "where staff_invoice.staff_id = ?";

    @Override
    public void create(Invoice invoice) throws RepositoryException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(CREATE_INVOICE);
            statement.setString(1, invoice.getOrderName());
            statement.setLong(2, invoice.getPrice());
            if(statement.execute()){
                throw new RepositoryException("Invoice was not created");
            }
        } catch (SQLException | RepositoryException e){
            throw new RepositoryException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new RepositoryException("Cannot close connection",e);
            }
        }
    }

    @Override
    public void update(Long aLong, Invoice invoice) throws RepositoryException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(UPDATE_INVOICE);
            statement.setString(1, invoice.getOrderName());
            statement.setLong(2, invoice.getPrice());
            statement.setLong(3, aLong);
            if(statement.execute()){
                throw new RepositoryException("Invoice was not updated");
            }
        } catch (RepositoryException | SQLException e){
            throw new RepositoryException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new RepositoryException("Cannot close connection",e);
            }
        }
    }

    @Override
    public void delete(Long aLong) throws RepositoryException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(DELETE_INVOICE);
            statement.setLong(1, aLong);
            if(statement.execute()){
                throw new RepositoryException("Invoice was not deleted");
            }
        } catch (RepositoryException | SQLException e){
            throw new RepositoryException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new RepositoryException("Cannot close connection",e);
            }
        }
    }

    @Override
    public Optional<List<Invoice>> findAll() throws RepositoryException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Invoice> softwares;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(FIND_INVOICE);
            softwares = readDataFromResultSet(statement.executeQuery());
        } catch (RepositoryException | SQLException e){
            throw new RepositoryException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new RepositoryException("Cannot close connection",e);
            }
        }
        return Optional.of(softwares);
    }

    @Override
    public void addStaffToProject(Staff staff, Invoice invoice) throws RepositoryException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(ADD_STAFF_TO_INVOICE);
            statement.setLong(1, staff.getId());
            statement.setLong(2, invoice.getId());
            if(statement.execute()){
                throw new RepositoryException("Staff was not added to Invoice");
            }
        } catch (SQLException | RepositoryException e){
            throw new RepositoryException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new RepositoryException("Cannot close connection",e);
            }
        }
    }

    @Override
    public void removeStaffFromProject(Staff staff) throws RepositoryException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(REMOVE_STAFF_FROM_INVOICE);
            statement.setLong(1, staff.getId());
            if(statement.execute()){
                throw new RepositoryException("Staff was not removed from Invoice");
            }
        } catch (RepositoryException | SQLException e){
            throw new RepositoryException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new RepositoryException("Cannot close connection",e);
            }
        }
    }

    @Override
    public Optional<List<Invoice>> showSoftwareRelatedToStaff(Long staffId) throws RepositoryException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Invoice> softwares;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(FIND_INVOICE_RELATED_TO_STAFF);
            statement.setLong(1,staffId);
            softwares = readDataFromResultSet(statement.executeQuery());
        } catch (RepositoryException | SQLException e){
            throw new RepositoryException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new RepositoryException("Cannot close connection",e);
            }
        }
        return Optional.of(softwares);
    }

    private List<Invoice> readDataFromResultSet(ResultSet resultSet) throws RepositoryException {
        List<Invoice> softwareList = new ArrayList<>();
        try {
            while(resultSet.next()){
                softwareList.add(new Invoice(
                        resultSet.getLong("id"),
                        resultSet.getString("order"),
                        resultSet.getLong("price")
                ));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Reading from result set is failed",e);
        }
        return softwareList;
    }
}
