package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

/**
 * Ta prosta aplikacja bazodanowa to aplikacja konsolowa Java, która łączy się z bazą danych MySQL
 * i umożliwia użytkownikowi wykonywanie operacji CRUD (Create, Read, Update, Delete)
 * na tabeli "users".
 * Aplikacja oferuje następujące funkcje:
 * 1. Dodanie nowego użytkownika.
 * 2. Usunięcie istniejącego użytkownika.
 * 3. Aktualizacja danych istniejącego użytkownika.
 * 4. Wyświetlenie listy wszystkich użytkowników.
 * 5. Zakończenie działania aplikacji.
 * Autor pracy: Bartosz Kubiczek
 */

public class App {
	private static final String URL = "jdbc:mysql://localhost:3306/DataBaseApp?useUnicode=true&characterEncoding=UTF-8";
	private static final String USER = "root";
	private static final String PASSWORD = "root";

	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
			System.out.println("Polaczono z baza danych.");

			Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
			while (true) {
				System.out.println("Wybierz operacje:");
				System.out.println("1. Dodaj uzytkownika");
				System.out.println("2. Usun uzytkownika");
				System.out.println("3. Zmien dane uzytkownika");
				System.out.println("4. Wyswietl uzytkownikow");
				System.out.println("5. Wyjdz");

				int choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {
					case 1:
						addUser(scanner, conn);
						break;
					case 2:
						deleteUser(scanner, conn);
						break;
					case 3:
						updateUser(scanner, conn);
						break;
					case 4:
						displayUsers(conn);
						break;
					case 5:
						System.out.println("Wyjscie z programu.");
						return;
					default:
						System.out.println("Nieprawidlowy wybor.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void addUser(Scanner scanner, Connection conn) {
		try {
			System.out.print("Podaj imie: ");
			String name = scanner.nextLine();
			System.out.print("Podaj email: ");
			String email = scanner.nextLine();

			String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, name);
				pstmt.setString(2, email);
				pstmt.executeUpdate();
				System.out.println("Uzytkownik dodany.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void deleteUser(Scanner scanner, Connection conn) {
		try {
			System.out.print("Podaj ID uzytkownika do usuniecia: ");
			int id = scanner.nextInt();
			scanner.nextLine();  // consume newline

			String sql = "DELETE FROM users WHERE id = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, id);
				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Uzytkownik usuniety.");
				} else {
					System.out.println("Uzytkownik o podanym ID nie istnieje.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void updateUser(Scanner scanner, Connection conn) {
		try {
			System.out.print("Podaj ID uzytkownika do zmiany: ");
			int id = scanner.nextInt();
			scanner.nextLine();  // consume newline
			System.out.print("Podaj nowe imie: ");
			String name = scanner.nextLine();
			System.out.print("Podaj nowy email: ");
			String email = scanner.nextLine();

			String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, name);
				pstmt.setString(2, email);
				pstmt.setInt(3, id);
				int rowsAffected = pstmt.executeUpdate();
				if (rowsAffected > 0) {
					System.out.println("Dane uzytkownika zmienione.");
				} else {
					System.out.println("Uzytkownik o podanym ID nie istnieje.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void displayUsers(Connection conn) {
		try (Statement stmt = conn.createStatement()) {
			String sql = "SELECT id, name, email FROM users";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = new String(rs.getBytes("name"), StandardCharsets.UTF_8);
				String email = rs.getString("email");
				System.out.println("ID: " + id + ", Imie: " + name + ", Email: " + email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
