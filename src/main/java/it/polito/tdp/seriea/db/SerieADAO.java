package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Match;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match>elencoMatchPerSquadra(Team squadra,Map<Integer, Season>stagioniIdMap, Map<String, Team>teamIdMap){
		String sql = 	"SELECT m.match_id as id,m.Season as stag,m.`Div` as divisione,m.`DATE` as giorno, "+ 
						"m.HomeTeam as sqCasa,m.AwayTeam as sqOspite,m.FTHG as fthg,m.FTAG as ftag,m.FTR as ftr "+
						"FROM matches m " + 
						"WHERE m.HomeTeam= ? OR m.AwayTeam= ?";
		List<Match> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setString(1, squadra.getTeam());
			st.setString(2, squadra.getTeam());
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Season s=stagioniIdMap.get(res.getInt("stag"));
				Team home=teamIdMap.get(res.getString("sqCasa"));
				Team away=teamIdMap.get(res.getString("sqOspite"));
				
				Match m=new Match(res.getInt("id"),s,res.getString("divisione"),res.getDate("giorno").toLocalDate(),home, away, 
						res.getInt("fthg"),res.getInt("ftag"),res.getString("ftr"));
				
				result.add(m);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}

