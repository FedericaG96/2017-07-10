package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;

public class DBConnect {

	private static String jdbcURL = "jdbc:mysql://localhost/artsmia?useTimezone=true&serverTimezone=UTC&user=root&password=Federi22!";

	static private Connection connection = null;

	public static Connection getConnection() {

		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection(jdbcURL);
			}
			return connection;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException("Cannot get a connection " + jdbcURL, e);
		}

	}

}
