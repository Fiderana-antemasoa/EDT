package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Connexion {
	private static final String URL = "jdbc:mysql://localhost:3306/gestion_emploi_du_temps";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	public static Connection getConnection() {
		Connection connexion = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connexion = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("✅ Connexion réussie !");
		} catch (ClassNotFoundException e) {
			System.err.println("❌ Driver JDBC introuvable : " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("❌ Erreur SQL : " + e.getMessage());

		}
		return connexion;
	}

	
}
