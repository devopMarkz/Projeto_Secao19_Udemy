package com.udemy.projeto.model.dao.implementation;

import com.udemy.projeto.exception.DbException;
import com.udemy.projeto.model.dao.DepartmentDAO;
import com.udemy.projeto.model.entities.Department;
import com.udemy.projeto.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOJDBC implements DepartmentDAO {

    private Connection connection;

    public DepartmentDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {
        try (PreparedStatement insertStmt = connection.prepareStatement(
                "INSERT INTO department " +
                        "(Name) " +
                        "VALUES " +
                        "(?)", Statement.RETURN_GENERATED_KEYS
        )){

            insertStmt.setString(1, department.getName());
            int rowsAffected = insertStmt.executeUpdate();
            if(rowsAffected > 0) {
                ResultSet rs = insertStmt.getGeneratedKeys();
                if(rs.next()) {
                    department.setId(rs.getInt(1));
                }
            } else {
                throw new DbException("Insert falhou. " + rowsAffected + " linhas afetadas.");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public void update(Department department) {
        try (PreparedStatement updateStmt = connection.prepareStatement(
                "UPDATE department " +
                        "SET Name = ? " +
                        "WHERE Id = ?"
        )){

            updateStmt.setString(1, department.getName());
            updateStmt.setInt(2, department.getId());
            updateStmt.executeUpdate();

        } catch (SQLException e) {
            throw new DbException("Update do usuário " + department + " falhou. Caused by: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        try (PreparedStatement deleteStmt = connection.prepareStatement(
                "DELETE FROM department " +
                        "WHERE Id = ?"
        )){

            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public Department findById(int id) {
        ResultSet rs = null;
        try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT * FROM department " +
                        "WHERE Id = ?"
        )){

            selectStmt.setInt(1, id);
            rs = selectStmt.executeQuery();

            if(rs.next()) {
                return new Department(rs.getInt("Id"), rs.getString("Name"));
            } else throw new DbException("Departamento não encontrado.");

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        ResultSet rs = null;
        try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT * FROM department"
        )){
            List<Department> departmentList = new ArrayList<>();
            rs = selectStmt.executeQuery();

            while (rs.next()) {
                departmentList.add(
                        new Department(rs.getInt("Id"), rs.getString("Name"))
                );
            }

            return departmentList;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
        }
    }
}
