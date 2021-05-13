package server.database.repository.jdbc;

import server.database.repository.ConnectionFactory;
import server.database.repository.DaoException;
import server.database.repository.SoftwareRepository;
import server.database.domain.Software;
import server.database.domain.Staff;
import server.database.domain.enums.SoftwareType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoftwareJdbc implements SoftwareRepository {
    private static final String CREATE_SOFTWARE = "insert into software_company.software(name,software_type) values(?,?);";

    private static final String DELETE_SOFTWARE = "delete from software_company.software " +
            "where id = ?;";

    private static final String UPDATE_SOFTWARE = "update software_company.software set " +
            "name = ?," +
            "software_type = ?," +
            "where id = ?;";

    private static final String FIND_SOFTWARE = "select * from software_company.software;";
    private static final String ADD_STAFF_TO_SOFTWARE = "insert into software_company.staff_software(staff_id, software_id) values (?,?);";
    private static final String REMOVE_STAFF_FROM_SOFTWARE = "delete from software_company.staff_software where staff_id = ?;";
    private static final String FIND_SOFTWARE_RELATED_TO_STAFF = "select id," +
            "       name," +
            "       software_type " +
            "from software_company.staff_software " +
            "inner join software_company.software s on s.id = staff_software.software_id " +
            "where staff_software.staff_id = ?;";

    @Override
    public void create(Software software) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(CREATE_SOFTWARE);
            statement.setString(1, software.getName());
            statement.setString(2, software.getSoftwareType().name());
            if(statement.execute()){
                throw new DaoException("Software was not created");
            }
        } catch (SQLException | DaoException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
    }

    @Override
    public void update(Long aLong, Software software) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(UPDATE_SOFTWARE);
            statement.setString(1, software.getName());
            statement.setString(2, software.getSoftwareType().name());
            statement.setLong(5, aLong);
            if(statement.execute()){
                throw new DaoException("Order was not updated");
            }
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
    }

    @Override
    public void delete(Long aLong) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(DELETE_SOFTWARE);
            statement.setLong(1, aLong);
            if(statement.execute()){
                throw new DaoException("Staff was not deleted");
            }
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
    }

    @Override
    public Optional<List<Software>> findAll() throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Software> softwares;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(FIND_SOFTWARE);
            softwares = readDataFromResultSet(statement.executeQuery());
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
        return Optional.of(softwares);
    }

    @Override
    public void addStaffToProject(Staff staff, Software software) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(ADD_STAFF_TO_SOFTWARE);
            statement.setLong(1, staff.getId());
            statement.setLong(2, software.getId());
            if(statement.execute()){
                throw new DaoException("Staff was not added to software");
            }
        } catch (SQLException | DaoException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
    }

    @Override
    public void removeStaffFromProject(Staff staff) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(REMOVE_STAFF_FROM_SOFTWARE);
            statement.setLong(1, staff.getId());
            if(statement.execute()){
                throw new DaoException("Staff was not removed from software");
            }
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
    }

    @Override
    public Optional<List<Software>> showSoftwareRelatedToStaff(Long staffId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Software> softwares;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(FIND_SOFTWARE_RELATED_TO_STAFF);
            statement.setLong(1,staffId);
            softwares = readDataFromResultSet(statement.executeQuery());
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
        return Optional.of(softwares);
    }

    private List<Software> readDataFromResultSet(ResultSet resultSet) throws DaoException {
        List<Software> softwareList = new ArrayList<>();
        try {
            while(resultSet.next()){
                softwareList.add(new Software(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        SoftwareType.valueOf(resultSet.getString("software_type"))
                ));
            }
        } catch (SQLException e) {
            throw new DaoException("Reading from result set is failed",e);
        }
        return softwareList;
    }
}
