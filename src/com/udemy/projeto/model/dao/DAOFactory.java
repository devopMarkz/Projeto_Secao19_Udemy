package com.udemy.projeto.model.dao;

import com.udemy.projeto.model.dao.implementation.SellerDAOJDBC;

public class DAOFactory {

    public static SellerDAO createSellerDAO() {
        return new SellerDAOJDBC();
    }

}
