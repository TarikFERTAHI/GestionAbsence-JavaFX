package Metier;

import DAO.SignletonConnexionDB;
import Entities.Etudiant;
import Entities.Filiere;
import Entities.ListeAbsence;
import Entities.Professeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IMetierImpl implements IMetier{
    @Override
    public boolean checkInfoUser(String login, String password) {
        Connection connection = SignletonConnexionDB.getConnection();
        boolean check = false;
        try{
            String sql ="select * from professeur where login=? and pass=?";
            PreparedStatement ps =connection.prepareStatement(sql);
            ps.setString(1,login);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                check = true;
            }else{
                check = false;
            }
        }catch(Exception e){
            System.out.println("exeption here!!");
        }
        System.out.println(check);
        return check;
    }

    @Override
    public void addEtudiant(Etudiant etudiant) {
        Connection connection = SignletonConnexionDB.getConnection();
        try{
            String sql = "insert into etudiant(idFiliere,nomEtudiant,prenomEtudiant,teleEtudiant,emailEtudiant) values (?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,etudiant.getFiliere().getIdFiliere());
            ps.setString(2,etudiant.getNomEtudiant());
            ps.setString(3,etudiant.getPrenomEtudiant());
            ps.setString(4,etudiant.getTeleEtudiant());
            ps.setString(5,etudiant.getEmailEtudiant());
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEtudiant(Etudiant etudiant) {
        try {
            PreparedStatement pst = SignletonConnexionDB.getConnection().prepareStatement("delete from etudiant where idEtudiant = ? ");
            pst.setInt(1,etudiant.getIdEtudiant());
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEtudiant(int idEtudiant, Etudiant etudiant) {
        Connection connection = SignletonConnexionDB.getConnection();
        try {
            PreparedStatement pstm=connection.prepareStatement("update Etudiant set nomEtudiant=?,prenomEtudiant=?,teleEtudiant=?,emailEtudiant=? where idEtudiant=?");
            pstm.setString(1,etudiant.getNomEtudiant());
            pstm.setString(2,etudiant.getPrenomEtudiant());
            pstm.setString(3,etudiant.getTeleEtudiant());
            pstm.setString(4,etudiant.getEmailEtudiant());
            pstm.setInt(5,idEtudiant);
            pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void createAbsence(ListeAbsence listeAbsence, Professeur professeur, Etudiant etudiant) {

        try {
            PreparedStatement pst = SignletonConnexionDB.getConnection().prepareStatement("insert into listeabsence (dateAbs, heureDeb, heureFin, module)  values (?,?,?,?)");
            Date date=Date.valueOf(listeAbsence.getDateAbs());
            pst.setDate(1,date);
            pst.setString(2,listeAbsence.getHeureDeb());
            pst.setString(3,listeAbsence.getHeureFin());
            pst.setString(4,listeAbsence.getModule());
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            PreparedStatement pst1 = SignletonConnexionDB.getConnection().prepareStatement("insert into absenter(idAbs,idEtudiant) values (?,?)");
            pst1.setInt(1,listeAbsence.getIdAbs());
            pst1.setInt(2,etudiant.getIdEtudiant());
            pst1.execute();
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            PreparedStatement pst2 = SignletonConnexionDB.getConnection().prepareStatement("insert into remplir(idProf,idAbs) values (?,?)");
            pst2.setInt(1,professeur.getIdProf());
            pst2.setInt(2,listeAbsence.getIdAbs());
            pst2.execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Etudiant> getAllEtudiants() {
        Connection connection = SignletonConnexionDB.getConnection();
        List<Etudiant> listEtudiants = new ArrayList<>();

        try{
            String sql = "select * from etudiant";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs =ps.executeQuery();
            while(rs.next()){
                String sql1 = "select * from filiere where idFiliere = ?";
                PreparedStatement ps1 = connection.prepareStatement(sql1);
                ps1.setInt(1,rs.getInt("idFiliere"));
                ResultSet rs1 = ps1.executeQuery();
                Filiere f = null;
                if(rs1.next()){
                    f=new Filiere(rs1.getInt("idFiliere"),rs1.getString(2));
                }
                    Etudiant etudiant = new Etudiant(rs.getInt("idEtudiant"),f,rs.getString("nomEtudiant"),rs.getString("prenomEtudiant"),rs.getString("teleEtudiant"),rs.getString("emailEtudiant"));
                    listEtudiants.add(etudiant);
            }
            return listEtudiants;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public List<ListeAbsence> getAllListAbsence(){
        Connection connection = SignletonConnexionDB.getConnection();
        List<ListeAbsence> listeAbsences = new ArrayList<>();
        try{
            String sql = "select * from listeabsence";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Date date=rs.getDate("dateAbs");
                ListeAbsence listeAbsence = new ListeAbsence(rs.getInt("idAbs"),date.toLocalDate(),rs.getString("heureDeb"),rs.getString("heureFin"),rs.getString("module"));
                listeAbsences.add(listeAbsence);
            }
            return listeAbsences;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
