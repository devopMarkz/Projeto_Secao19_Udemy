package com.udemy.projeto;

import com.udemy.projeto.exception.DbException;
import com.udemy.projeto.model.dao.DAOFactory;
import com.udemy.projeto.model.dao.SellerDAO;
import com.udemy.projeto.model.entities.Seller;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        try {
            SellerDAO sellerDAO = DAOFactory.createSellerDAO();

            System.out.print("Digite o ID do usuário que você deseja buscar: ");
            int id = new Scanner(System.in).nextInt();

            Seller seller = sellerDAO.findById(id);

            System.out.println(seller);

        } catch (DbException e) {
            System.out.println(e.getMessage());
        }

    }
}