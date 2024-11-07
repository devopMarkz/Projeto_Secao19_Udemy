package com.udemy.projeto;

import com.udemy.projeto.exception.DbException;
import com.udemy.projeto.model.dao.DAOFactory;
import com.udemy.projeto.model.dao.DepartmentDAO;
import com.udemy.projeto.model.dao.SellerDAO;
import com.udemy.projeto.model.entities.Department;
import com.udemy.projeto.model.entities.Seller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Application {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) {

        // testSellerDAO();

        testDepartmentDAO();


    }

    public static void testDepartmentDAO() {

        DepartmentDAO departmentDAO = DAOFactory.createDepartmentDAO();

        System.out.println("------------ Insert ------------");
        Department department = new Department(null, "Contratos");
        departmentDAO.insert(department);

        char resposta = new Scanner(System.in).next().charAt(0);

        System.out.println("\n------------ DeleteById ------------");
        departmentDAO.deleteById(department.getId());

        resposta = new Scanner(System.in).next().charAt(0);

        System.out.println("\n------------ FindById ------------");
        Department department1 = departmentDAO.findById(2);
        System.out.println(department1);

        resposta = new Scanner(System.in).next().charAt(0);

        System.out.println("\n------------ Update ------------");
        department1.setName("Nada a ver");
        departmentDAO.update(department1);

        resposta = new Scanner(System.in).next().charAt(0);

        System.out.println("\n------------ FindAll ------------");
        List<Department> departmentList = departmentDAO.findAll();
        departmentList.forEach(System.out::println);

    }

    public static void testSellerDAO() {
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

            System.out.println("\n----------- Teste Insert -----------");

            Seller seller1 = new Seller.Builder()
                    .name("Vitor Nascimento")
                    .email("vitor@gmail.com")
                    .birthDate(LocalDateTime.parse("04/12/2003 12:35:47", fmt))
                    .baseSalary(2500.43)
                    .department(new Department(2, null))
                    .build();

            sellerDAO.insert(seller1);

            System.out.println("\n----------- Teste Update -----------");
            Seller seller2 = sellerDAO.findById(13);
            seller2.setName("Marcos André");
            seller2.setEmail("marcos@gmail.com");
            seller2.setBirthDate(LocalDateTime.parse("25/09/2002 14:35:23", fmt));
            sellerDAO.update(seller2);
            System.out.println("Update complete!");

            System.out.println("\n----------- Teste Delete -----------");
            sellerDAO.deleteById(13);

        } catch (DbException e) {
            System.out.println(e.getMessage());
        }
    }
}