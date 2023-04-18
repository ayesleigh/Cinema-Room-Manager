package cinema;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cinema {
    static final Scanner scanner = new Scanner(System.in);
    static String[][] screenRoomSeats;
    static int[][] seatsPrices;
    static int purchasedTickets;
    static int currentIncome;
    static int totalIncome;
    static int normalTicketPrice = 10;
    static int backHalfTicketPrice = 8;
    static int rows;
    static int seats;
    static int totalNumberOfSeats;


    public static void main(String[] args) {
        initialize();
        buildScreenRoom();
        putPricesOnSeats();
        showMenu();

        scanner.close();
    }

    static void initialize() {
        try {
            System.out.println();
            System.out.println("Enter the number of rows:");
            rows = scanner.nextInt();
            System.out.println("Enter the number of seats in each row:");
            seats = scanner.nextInt();
            if (rows <= 0 || seats <= 0) {
                rows = 0;
                seats = 0;
                System.out.println("Please, enter numbers that are more than " +
                        "zero!");
                initialize();
            }
            totalNumberOfSeats = rows * seats;
        } catch (InputMismatchException e) {
            scanner.next();
            System.out.println();
            System.out.println("Wrong input!");
            initialize();
        }
    }

    static void buyTicket() {
        int rowNumber = 0;
        int seatNumber = 0;
        try {
            System.out.println();
            System.out.println("Enter a row number:");
            rowNumber = scanner.nextInt();
            System.out.println("Enter a seat number in that row:");
            seatNumber = scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.next();
            System.out.println();
            System.out.println("Wrong input!");
            buyTicket();
        }

        int rows = screenRoomSeats.length - 1;
        int seats = screenRoomSeats[0].length - 1;

        if (rowNumber < 1 || rowNumber > rows || seatNumber < 1 || seatNumber > seats) {
            System.out.println();
            System.out.println("Wrong input!");
            buyTicket();
        } else if (screenRoomSeats[rowNumber][seatNumber].equals("B")) {
            System.out.println();
            System.out.println("That ticket has already been purchased!");
            buyTicket();
        } else {
            int ticketPrice = seatsPrices[rowNumber][seatNumber];
            System.out.printf("Ticket price: $%s%n", ticketPrice);
            screenRoomSeats[rowNumber][seatNumber] = "B";
            purchasedTickets++;
            currentIncome += ticketPrice;
            showMenu();
        }
    }

    static void showStatistics() {
        double purchasedTicketsPercentage =
                (double) purchasedTickets / totalNumberOfSeats * 100;
        System.out.println();
        System.out.printf("Number of purchased tickets: %d\n",
                purchasedTickets);
        System.out.printf("Percentage: %.2f%%\n", purchasedTicketsPercentage);
        System.out.printf("Current income: $%d\n", currentIncome);
        System.out.printf("Total income: $%d\n", totalIncome);
        showMenu();
    }

    static void showMenu() {
        int choice = 0;
        try {
            System.out.println();
            System.out.println("""
                               1. Show the seats
                               2. Buy a ticket
                               3. Statistics
                               0. Exit""");
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.next();
            System.out.println();
            System.out.println("Wrong input!");
            showMenu();
        }

        switch (choice) {
            case 1 -> showSeats();
            case 2 -> buyTicket();
            case 3 -> showStatistics();
            case 0 -> {}
            default -> {
                System.out.println("Not a valid choice. Please try again.");
                showMenu();
            }
        }
    }

    static void showSeats() {
        System.out.println();
        System.out.println("Cinema:");
        for (String[] rows : screenRoomSeats) {
            for (String seat : rows) {
                System.out.print(seat + " ");
            }
            System.out.println();
        }

        showMenu();
    }

    static void showPrices() {
        System.out.println();
        System.out.println("Prices:");
        System.out.println(Arrays.deepToString(seatsPrices)
                .replace("],","\n").replace(",","\t| ")
                .replaceAll("[\\[\\]]", " "));

        showMenu();
    }

    static void buildScreenRoom() {
        screenRoomSeats = new String[rows + 1][seats + 1];

        for (int i = 0; i < screenRoomSeats.length; i++) {
            for (int j = 0; j < screenRoomSeats[i].length; j++) {
                if (i == 0 && j == 0) {
                    screenRoomSeats[i][j] = " ";
                } else if (i == 0) {
                    screenRoomSeats[i][j] = String.valueOf(j);
                } else {
                    screenRoomSeats[i][j] = j == 0 ? String.valueOf(i) : "S";
                }
            }
        }
    }

    static void putPricesOnSeats() {
        seatsPrices = new int[rows + 1][seats + 1];

        for (int i = 0; i < screenRoomSeats.length; i++) {
            for (int j = 0; j < screenRoomSeats[i].length; j++) {
                if (i == 0 && j == 0) {
                    seatsPrices[i][j] = 0;
                } else if (i == 0) {
                    seatsPrices[i][j] = j;
                } else {
                    int price = calculatePriceOfSeat(i);
                    if (j != 0) {
                        totalIncome += price;
                    }
                    seatsPrices[i][j] = j == 0 ? i : price;
                }
            }
        }
    }

    static int calculatePriceOfSeat(int currentRow) {
        int priceOfSeat;
        if (totalNumberOfSeats > 60) {
            int frontHalf = rows / 2;
            priceOfSeat = currentRow <= frontHalf ? normalTicketPrice : backHalfTicketPrice;
        } else {
            priceOfSeat = normalTicketPrice;
        }

        return priceOfSeat;
    }
}