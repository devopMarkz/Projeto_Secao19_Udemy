package com.udemy.projeto.model.dao.implementation;

import com.udemy.projeto.exception.DbException;
import com.udemy.projeto.model.dao.SellerDAO;
import com.udemy.projeto.model.entities.Department;
import com.udemy.projeto.model.entities.Seller;
import com.udemy.projeto.util.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDAOJDBC implements SellerDAO {

    private Connection connection;

    public SellerDAOJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        try (PreparedStatement insertStmt = connection.prepareStatement(
                "INSERT INTO seller " +
                        "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                        "VALUES " +
                        "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
        )){

            insertStmt.setString(1, seller.getName());
            insertStmt.setString(2, seller.getEmail());
            insertStmt.setTimestamp(3, Timestamp.valueOf(seller.getBirthDate()));
            insertStmt.setDouble(4, seller.getBaseSalary());
            insertStmt.setInt(5, seller.getDepartment().getId());

            int rowsAffected = insertStmt.executeUpdate();

            if(rowsAffected > 0) {
                ResultSet rs = insertStmt.getGeneratedKeys();
                if(rs.next()) {
                    int id = rs.getInt(1);
                    seller.setId(id);
                    System.out.println("Usuário de ID " + id + " inserido no banco de dados.");
                }
            } else throw new DbException("Erro inesperado. Nenhuma linha foi afetada.");

        } catch (SQLException e) {
            throw new DbException("Erro ao inserir os dados do funcionário " + seller + "; Caused By: " + e.getMessage());
        }
    }

    @Override
    public void update(Seller seller) {
        try (PreparedStatement updateStmt = connection.prepareStatement(
                "UPDATE seller " +
                        "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                        "WHERE Id = ?"
        )){

            updateStmt.setString(1, seller.getName());
            updateStmt.setString(2, seller.getEmail());
            updateStmt.setTimestamp(3, Timestamp.valueOf(seller.getBirthDate()));
            updateStmt.setDouble(4, seller.getBaseSalary());
            updateStmt.setInt(5, seller.getDepartment().getId());
            updateStmt.setInt(6, seller.getId());
            updateStmt.executeUpdate();

        } catch (SQLException e) {
            throw new DbException("Erro no update do funcionário de ID " + seller.getId() + ". Caused by: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        try (PreparedStatement deleteStmt = connection.prepareStatement(
                "DELETE FROM seller " +
                        "WHERE Id = ?"
        )){

            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();

        }catch (SQLException e) {
            throw new DbException("Erro ao deletar usuário de ID " + id + ". Caused by: " + e.getMessage());
        }
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

        ResultSet rs = null;

        try (PreparedStatement selectStmt = connection.prepareStatement(
                "SELECT seller.*, department.Name as DepName " +
                        "FROM seller " +
                        "INNER JOIN department " +
                        "ON seller.DepartmentId = department.Id " +
                        "ORDER BY seller.Name"
        )){

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> departmentMaps = new HashMap<>();
            rs = selectStmt.executeQuery();

            while (rs.next()){
                Department department = departmentMaps.get(rs.getInt("seller.DepartmentId"));

                if(department == null) {
                    department = instantiateDepartment(rs);
                    departmentMaps.put(rs.getInt("seller.DepartmentId"), department);
                }

                Seller seller = instantiateSeller(rs, department);
                sellers.add(seller);
            }

            return sellers;

        } catch (SQLException e) {
            DB.closeResultSet(rs);
            throw new DbException("Erro na busca de funcionários. Caused By: " + e.getMessage());
        }

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
