package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import util.ConnectionPool;


public class FeedDAO {

		public boolean insert(String jsonstr) throws NamingException, SQLException, ParseException{
			Connection conn = ConnectionPool.get();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				synchronized(this){
					// phase 1. add "no" property -----------------------------
				String sql = "SELECT no FROM feed ORDER BY no DESC LIMIT 1";
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();
				
				int max = (!rs.next())? 0: rs.getInt("no");
				stmt.close();rs.close();
				
				JSONObject jsonobj = (JSONObject)(new JSONParser()).parse(jsonstr);
				jsonobj.put("no", max+1);
				
				
				JSONParser parser = new JSONParser();
				
				// phase 2. add "user" property ------------------------------
				String uid=jsonobj.get("id").toString();
				sql="SELECT jsonstr FROM user WHERE id=?";
				stmt=conn.prepareStatement(sql);
				stmt.setString(1, uid);
				rs = stmt.executeQuery();
				
				if(rs.next()) {
					String usrstr = rs.getString("jsonstr");
					JSONObject usrobj =(JSONObject)parser.parse(usrstr);
					usrobj.remove("password");
					usrobj.remove("ts");
					jsonobj.put("user",usrobj);
				}
		
				stmt.close();rs.close();
				
				sql = "INSERT INTO feed(no,id,jsonstr) VALUES(?,?,?)";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1,max+1);
				stmt.setString(2,uid);
				stmt.setString(3,jsonobj.toJSONString());
				
				int count = stmt.executeUpdate();
				return(count==1)? true:false;
				}
			
			} finally {
				if(rs!=null) rs.close();
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
			}
		}
		
		public String getList() throws NamingException, SQLException{
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				
				String sql = "SELECT * FROM feed ORDER BY no DESC";
				conn = ConnectionPool.get();
				stmt=conn.prepareStatement(sql);
				rs= stmt.executeQuery();
				/*
				ArrayList<FeedObj> feeds = new ArrayList<FeedObj>();
				while(rs.next()) {
					feeds.add(new FeedObj(rs.getString("id"),rs.getString("content"),rs.getString("ts"),rs.getString("images")));
				}
				return feeds;
				*/
				
				String str = "[";
				int cnt = 0;
				while(rs.next()) {
					if(cnt++>0) str +=", ";
					str +=rs.getString("jsonstr");
				}
				return str+="]";
				
			}finally {
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
				if(rs!=null) rs.close();
			}
		}
		
		public String getGroup(String frids,String maxNo) throws NamingException, SQLException {
			Connection conn = ConnectionPool.get();
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
			String sql = "SELECT jsonstr FROM feed WHERE id IN("+frids+")";
			if (maxNo != null) {
			sql += " AND no < " + maxNo;
			}
			sql += " ORDER BY no DESC LIMIT 3";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();
			    
				String str = "[";
				int cnt = 0;
				while(rs.next()) {
					if(cnt++>0) str +=", ";
					str +=rs.getString("jsonstr");
				}
				return str+="]";
				
			}finally {
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
				if(rs!=null) rs.close();
			}
		}
		public String found(String uid) throws NamingException, SQLException{
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				
				String sql = "SELECT jsonstr FROM feed where id=?";
				conn = ConnectionPool.get();
				stmt=conn.prepareStatement(sql);
				stmt.setString(1,uid);
				rs= stmt.executeQuery();
				
				String str = "[";
				int cnt = 0;
				while(rs.next()) {
					if(cnt++>0) str +=", ";
					str +=rs.getString("jsonstr");
				}
				return str+="]";
				
			}finally {
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
				if(rs!=null) rs.close();
			}
		}
		public boolean cancel(int num, String did) throws NamingException, SQLException {
			Connection conn = ConnectionPool.get();
			PreparedStatement stmt = null;
			try {
				String sql = "DELETE FROM feed WHERE no=? AND id=?";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, num);	
				stmt.setString(2, did);
				int count = stmt.executeUpdate();
				return (count==1)?true:false;
			} finally {
				if(stmt!=null)stmt.close(); 
				if(conn!=null)conn.close();
			}
		}
		public boolean change(String jsonstr) throws NamingException, SQLException, ParseException{
			Connection conn = ConnectionPool.get();
			PreparedStatement stmt = null;
			
			try {
				synchronized(this){
					System.out.println(jsonstr);
					JSONParser parser = new JSONParser();
					JSONObject jsonobj = (JSONObject)parser.parse(jsonstr);
					System.out.println(jsonobj);
				
					String sql = "UPDATE feed SET jsonstr=? WHERE no=?";
					stmt = conn.prepareStatement(sql);
					stmt.setString(1, jsonstr);
					JSONObject obj = (JSONObject)(new JSONParser()).parse(jsonstr);
					System.out.println(obj);
					stmt.setString(2,obj.get("no").toString());
					
					int count = stmt.executeUpdate();
					return(count==1)? true:false;
				}
			
			} finally {
				if(stmt!=null) stmt.close();
				if(conn!=null) conn.close();
			}
		}
		
		
}
