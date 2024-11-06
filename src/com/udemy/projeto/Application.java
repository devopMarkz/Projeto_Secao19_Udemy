package com.udemy.projeto;

import com.udemy.projeto.model.dao.DAOFactory;
import com.udemy.projeto.model.dao.SellerDAO;
import com.udemy.projeto.model.entities.Seller;

public class Application {
    public static void main(String[] args) {

        SellerDAO sellerDAO = DAOFactory.createSellerDAO();

        Seller seller = sellerDAO.findById(2);

        System.out.println(seller);

    }
}