package com.udemy.projeto.model.dao.implementation;

import com.udemy.projeto.exception.DbException;
import com.udemy.projeto.model.dao.SellerDAO;
import com.udemy.projeto.model.entities.Department;
import com.udemy.projeto.model.entities.Seller;
import com.udemy.projeto.util.DB;

import java.sql.*;
import java.util.ArrayList;
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
                Department department = instantiateDepartment(rs);
                return instantiateSeller(rs, department);
            } else throw new SQLException();
        } catch (SQLException e) {
            throw new DbException("Erro ao tentar acessar dados do usuário de ID " + id + ". Caused By: Usuário inexistente ou " + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department department) throws SQLException{
            return new Seller.Builder()
                    .id(rs.getInt("seller.Id"))
                    .name(rs.getString("seller.Name"))
                    .email(rs.getString("seller.Email"))
                    .birthDate(rs.getTimestamp("seller.BirthDate").toLocalDateTime())
                    .baseSalary(rs.getDouble("seller.BaseSalary"))
                    .department(department)
                    .build();
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException{
            Department department = new Department();
            department.setId(rs.getInt("seller.DepartmentId"));
            department.setName(rs.getString("DepName"));
            return department;
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }

    @Override
    public List<Seller> findByDepartment(int id) {
        ResultSet rs = null;

        try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT seller.*, department.Name AS DepName " +
                        "FROM seller " +
                        "INNER JOIN department " +
                        "ON seller.DepartmentId = department.Id " +
                        "WHERE seller.DepartmentId = ? " +
                        "ORDER BY seller.Name"
        )
        ){
            List<Seller> sellers = new ArrayList<>();
            selectStmt.setInt(1, id);
            rs =  selectStmt.executeQuery();

            Department dep = null;
            while (rs.next()) {
                if(dep == null) dep = instantiateDepartment(rs);
                sellers.add(instantiateSeller(rs, dep));
            }

            return sellers;

        } catch (SQLException e) {
            throw new DbException("Erro ao tentar acessar dados do deparmento de ID " + id + ". Caused By: Usuário inexistente ou " + e.getMessage());
        } finally {
            DB.closeResultSet(rs);
        }
    }
}
