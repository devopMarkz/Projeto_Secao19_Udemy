package com.udemy.projeto;

import com.udemy.projeto.exception.DbException;
import com.udemy.projeto.model.dao.DAOFactory;
import com.udemy.projeto.model.dao.SellerDAO;
import com.udemy.projeto.model.entities.Seller;

import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        try {
            System.out.println("----------- Teste FindById -----------");

            SellerDAO sellerDAO = DAOFactory.createSellerDAO();
            System.out.print("Digite o ID do usuário que você deseja buscar: ");
            int id = new Scanner(System.in).nextInt();
            Seller seller = sellerDAO.findById(id);
            System.out.println(seller);

            System.out.println("\n----------- Teste FindByDepartment -----------");

            System.out.print("Digite o ID do departamento que você deseja buscar funcionários: ");
            id = new Scanner(System.in).nextInt();
            List<Seller> sellers = sellerDAO.findByDepartment(id);
            sellers.forEach(System.out::println);

            System.out.println("\n----------- Teste FindAll -----------");

            List<Seller> sellers1 = sellerDAO.findAll();
            sellers1.forEach(System.out::println);

        } catch (DbException e) {
            System.out.println(e.getMessage());
        }

    }
}