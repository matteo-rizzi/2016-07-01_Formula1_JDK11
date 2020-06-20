package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.formulaone.model.Adiacenza;
import it.polito.tdp.formulaone.model.Circuit;
import it.polito.tdp.formulaone.model.Constructor;
import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Season;

public class FormulaOneDAO {

	public List<Integer> getAllYearsOfRace() {

		String sql = "SELECT year FROM races ORDER BY year";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Integer> list = new ArrayList<>();
			while (rs.next()) {
				list.add(rs.getInt("year"));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<Season> getAllSeasons() {

		String sql = "SELECT year, url FROM seasons ORDER BY year";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Season> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Circuit> getAllCircuits() {

		String sql = "SELECT circuitId, name FROM circuits ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Circuit> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Circuit(rs.getInt("circuitId"), rs.getString("name")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<Constructor> getAllConstructors() {

		String sql = "SELECT constructorId, name FROM constructors ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Constructor> constructors = new ArrayList<>();
			while (rs.next()) {
				constructors.add(new Constructor(rs.getInt("constructorId"), rs.getString("name")));
			}

			conn.close();
			return constructors;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public void getAllDrivers(Map<Integer, Driver> idMap) {
		String sql = "SELECT * FROM drivers";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if (!idMap.containsKey(rs.getInt("driverId"))) {
					Driver d = new Driver(rs.getInt("driverId"), rs.getString("driverRef"), rs.getInt("number"),
							rs.getString("code"), rs.getString("forename"), rs.getString("surname"),
							null, rs.getString("nationality"), rs.getString("url"));
					idMap.put(rs.getInt("driverId"), d);
				}
			}

			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}

	}

	public List<Driver> getDriversByYear(int anno, Map<Integer, Driver> idMap) {
		String sql = "SELECT DISTINCT driverId FROM races, results WHERE results.raceId = races.raceId AND races.year = ? AND results.position <> 0";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);

			ResultSet rs = st.executeQuery();

			List<Driver> list = new ArrayList<>();
			while (rs.next()) {
				list.add(idMap.get(rs.getInt("driverId")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

	public List<Adiacenza> getAdiacenze(int anno, Map<Integer, Driver> idMap) {
		String sql = "SELECT r1.driverId AS primo, r2.driverId AS secondo, COUNT(r.raceId) AS peso FROM races AS r, results AS r1, results AS r2 WHERE r.raceId = r1.raceId AND r1.raceId = r2.raceId AND r1.driverId <> r2.driverId AND r.year = ? AND r1.position <> 0 AND r2.position <> 0 AND r1.position < r2.position GROUP BY r1.driverId, r2.driverId";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);

			ResultSet rs = st.executeQuery();

			List<Adiacenza> list = new ArrayList<>();
			while (rs.next()) {
				Adiacenza a = new Adiacenza(idMap.get(rs.getInt("primo")), idMap.get(rs.getInt("secondo")),
						rs.getInt("peso"));
				list.add(a);
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}

}
