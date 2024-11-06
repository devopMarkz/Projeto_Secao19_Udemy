package com.udemy.projeto.model.dao.implementation;

import com.udemy.projeto.exception.DbException;
import com.udemy.projeto.model.dao.SellerDAO;
import com.udemy.projeto.model.entities.Department;
import com.udemy.projeto.model.entities.Seller;
import com.udemy.projeto.util.DB;

import java.sql.*;
import java.util.List;

public class SellerDAOJDBC implements SellerDAO {

    private Connection connection;

    public SellerDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Seller findById(int id) {

        ResultSet rs = null;

        try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT seller.*, department.Name AS DepName " +
                        "FROM seller " +
                        "INNER JOIN department " +
                        "ON seller.DepartmentId = department.Id " +
                        "WHERE seller.Id = ?"
        )
        ){
            selectStmt.setInt(1, id);
            rs = selectStmt.executeQuery();

            if(rs.next()) {
                Department department = new Department(rs.getInt("seller.DepartmentId"), rs.getString("DepName"));
                return new Seller.Builder()
                        .id(rs.getInt("seller.Id"))
                        .name(rs.getString("seller.Name"))
                        .email(rs.getString("seller.Email"))
                        .birthDate(rs.getTimestamp("seller.BirthDate").toLocalDateTime())
                        .baseSalary(rs.getDouble("seller.BaseSalary"))
                        .department(department)
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Erro ao tentar acessar dados do usuário de ID " + id + ". Caused By: " + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }
}
