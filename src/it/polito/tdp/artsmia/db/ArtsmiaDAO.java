package it.polito.tdp.artsmia.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.artsmia.model.Adiacenza;
import it.polito.tdp.artsmia.model.ArtObject;

public class ArtsmiaDAO {

	public List<ArtObject> listObjects(Map<Integer, ArtObject> idMapOpere) {
		
		String sql = "SELECT * from objects";
		List<ArtObject> result = new ArrayList<ArtObject>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				if(idMapOpere.get(res.getInt("object_id")) == null) {
					ArtObject artObj = new ArtObject(res.getInt("object_id"), res.getString("classification"), res.getString("continent"), 
						res.getString("country"), res.getInt("curator_approved"), res.getString("dated"), res.getString("department"), 
						res.getString("medium"), res.getString("nationality"), res.getString("object_name"), res.getInt("restricted"), 
						res.getString("rights_type"), res.getString("role"), res.getString("room"), res.getString("style"), res.getString("title"));
				
					idMapOpere.put(res.getInt("object_id"), artObj);
					result.add(artObj);
				}
				result.add(idMapOpere.get(res.getInt("object_id")));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Adiacenza> getCoEsposti() {
		String sql ="SELECT e1.object_id AS id1, e2.object_id AS id2, COUNT(*) AS cnt " + 
				"FROM exhibition_objects AS e1 , exhibition_objects AS e2 " + 
				"WHERE e1.object_id <> e2.object_id " + 
				"AND e1.exhibition_id = e2.exhibition_id " + 
				"GROUP BY id1, id2 ";
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(new Adiacenza(res.getInt("id1"), res.getInt("id2"), res.getInt("cnt")));
				
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
