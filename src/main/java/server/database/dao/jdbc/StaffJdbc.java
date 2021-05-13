package server.database.dao.jdbc;

import server.database.dao.ConnectionFactory;
import server.database.dao.DaoException;
import server.database.dao.StaffRepository;
import server.database.domain.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StaffJdbc implements StaffRepository {
    private static final String CREATE_STAFF = "insert into software_company.staff(name,surname,salary,work_position) values(?,?,?,?);";

    private static final String DELETE_STAFF = "delete from software_company.staff " +
            "where id = ?;";

    private static final String UPDATE_STAFF = "update software_company.staff set " +
            "name = ?," +
            "surname = ?," +
            "salary = ?," +
            "work_position = ?" +
            "where id = ?;";

    private static final String FIND_STAFF = "select * from software_company.staff;";

    private static final String FIND_STAFF_RELATED_TO_SOFTWARE = "select id," +
            "       name," +
            "       surname," +
            "       salary," +
            "       work_position " +
            "from software_company.staff_software " +
            "         inner join software_company.staff s on s.id = staff_software.staff_id " +
            "where staff_software.software_id = ?;";


    @Override
    public void create(Staff staff) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(CREATE_STAFF);
            statement.setString(1, staff.getName());
            statement.setString(2, staff.getSurname());
            statement.setLong(3, staff.getSalary());
            statement.setString(4, staff.getWorkPosition());
            if(statement.execute()){
                throw new DaoException("Staff was not created");
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
    public void update(Long aLong, Staff staff) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(UPDATE_STAFF);
            statement.setString(1, staff.getName());
            statement.setString(2, staff.getSurname());
            statement.setString(3, staff.getWorkPosition());
            statement.setLong(4, staff.getSalary());
            statement.setLong(5, aLong);
            if(statement.execute()){
                throw new DaoException("Staff was not updated");
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
            statement = connection.prepareStatement(DELETE_STAFF);
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
    public Optional<List<Staff>> findAll() throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Staff> orderList;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(FIND_STAFF);
            orderList = readDataFromResultSet(statement.executeQuery());
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
        return Optional.of(orderList);
    }

    @Override
    public Optional<List<Staff>> showStaffRelatedToSoftware(Long projectId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Staff> orderList;
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(FIND_STAFF_RELATED_TO_SOFTWARE);
            statement.setLong(1,projectId);
            orderList = readDataFromResultSet(statement.executeQuery());
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
        return Optional.of(orderList);
    }

    private List<Staff> readDataFromResultSet(ResultSet resultSet) throws DaoException {
        List<Staff> orderList = new ArrayList<>();
        try {
            while(resultSet.next()){
                orderList.add(new Staff(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getLong("salary"),
                        resultSet.getString("work_position")
                ));
            }
        } catch (SQLException e) {
            throw new DaoException("Reading from result set is failed",e);
        }
        return orderList;
    }
}
