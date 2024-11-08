package com.udemy.projeto.model.dao;

import com.udemy.projeto.model.dao.implementation.DepartmentDAOJDBC;
import com.udemy.projeto.model.dao.implementation.SellerDAOJDBC;
import com.udemy.projeto.util.DB;

public class DAOFactory {

    public static SellerDAO createSellerDAO() {
        return new SellerDAOJDBC(DB.getConnection());
    }

    public static DepartmentDAO createDepartmentDAO() {
        return new DepartmentDAOJDBC(DB.getConnection());
    }

}
