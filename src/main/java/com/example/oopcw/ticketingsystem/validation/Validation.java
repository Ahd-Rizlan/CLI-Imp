package com.example.oopcw.ticketingsystem.validation;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Validation {

    public int getValidation(Scanner input, String prompt) {
        int value;
        while (true) {
            System.out.println(prompt);
            //prompt user gives
            input.nextLine();
            try {
                value = Integer.parseInt(input.nextLine());
                if (!input.nextLine().matches("\\d+")) {
                    System.out.println("Please enter a numbers only ");
                    continue;
                }

                if (value < 0) {
                    System.out.println("Please enter a positive number");
                } else {
                    return value;
                }
            } catch (InputMismatchException | NumberFormatException e) {

                System.out.println("Invalid input, Enter Numbers only ");
            }
        }


    }

}
