package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	
	public List<Food> getVertici(Map<Integer,Food> idMap, int porzioni) {
		String sql = "SELECT food_code "
				+ "FROM `portion` "
				+ "GROUP BY food_code "
				+ "HAVING COUNT(*) >= ?";
		List<Food> result = new ArrayList<Food>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, porzioni);
			ResultSet rs = st.executeQuery();
			
			while(rs.next())
				result.add(idMap.get(rs.getInt("food_code")));
			
			conn.close();
			return result;
		} catch(SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> getArchi(Map<Integer,Food> idMap, int porzioni) {
		String sql = "SELECT mediaGrassi1.food_code AS food1, mediaGrassi2.food_code AS food2, (mediaGrassi1.average1-mediaGrassi2.average2) AS diff "
				+ "FROM (SELECT food_code, AVG(saturated_fats) AS average1 "
				+ "FROM `portion` "
				+ "GROUP BY food_code "
				+ "HAVING COUNT(*) >= ?) AS mediaGrassi1, "
				+ "(SELECT food_code, AVG(saturated_fats) AS average2 "
				+ "FROM `portion` "
				+ "GROUP BY food_code "
				+ "HAVING COUNT(*) >= ?) AS mediaGrassi2 "
				+ "WHERE mediaGrassi1.food_code < mediaGrassi2.food_code";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, porzioni);
			st.setInt(2, porzioni);
			ResultSet rs = st.executeQuery();
			
			while(rs.next())
				result.add(new Adiacenza(idMap.get(rs.getInt("food1")), 
						idMap.get(rs.getInt("food2")), rs.getDouble("diff")));
			
			conn.close();
			return result;
		} catch(SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
}
