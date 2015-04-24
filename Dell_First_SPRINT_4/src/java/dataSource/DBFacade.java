/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataSource;

import Entity.Partner;
import Entity.Project;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;

/**
 *
 * @author Sindt
 */
public class DBFacade {

    private UnitOfWork uow;

    private static DBFacade instance;
    private Connection con;
    private Mapper map;

    private DBFacade() {
        con = DBConnection.getInstance().getConnection();
    }

    public static DBFacade getInstance() {
        if (instance == null) {
            instance = new DBFacade();
        }
        return instance;
    }
    //Bruges kun til at kalde metoderne fra UOW, eller Mapperen

    public void addPartner(Partner p) {
        uow.addPartner(p);
    }

    public void startNewBuisnessTransaction() {
        uow = new UnitOfWork();
    }

    public boolean commitBusinessTransaction() {
        boolean status = false;
        if (uow != null) {
            try {
                status = uow.commit(con);
            } catch (Exception e) {
                System.out.println("Fail in DBFacade - commitBusinessTransaction");
                System.err.println(e);
            }
            uow = null;
        }
        return status;

    }

    public void addProject(Project currentProject) {
        uow.addProject(currentProject);
    }

    public Map<Integer, Partner> getAllPartners() {
        try {
            return uow.getAllPartners(con);
        } catch (Exception e) {
            System.out.println("Fail in DBFacade");
            System.out.println(e.fillInStackTrace());
            return null;
        }

    }

    public ArrayList<Project> getAllProjects() {
        try {
            map = new Mapper();
            return map.getAllProjects(con);
        } catch (Exception e) {
            System.out.println("Fail in DBFacade");
            System.out.println(e.fillInStackTrace());
            return null;
        }
    }

    public void addToApprovalList(int id) {
        uow.addToApprovalIdList(id);
    }

    public boolean commitForApprovals() {
        try {
            return uow.commitForApprovals(con);
        } catch (SQLException ex) {
            Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public ArrayList<Project> getSentProjects() {
        try {
            return uow.getSentProjects(con);
        } catch (Exception e) {
            System.out.println("Fail in DBFacade");
            System.out.println(e.fillInStackTrace());
            return null;
        }
    }

    public boolean acceptProjectRequest(int id) {
        try {
            return uow.acceptProjectRequest(con, id);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean declineProjectRequest(int id) {
        try {
            return uow.declineProjectRequest(con, id);
        } catch (Exception e) {
            return false;
        }
    }

    public Project goToProject(int id) {

        try {
            return uow.goToProject(con, id);
        } catch (SQLException ex) {
            Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void addFile(int id, Path target) {
        uow.addFile(con, id, target);
    }

    public void addFile(Part filePart) {
        uow.addFile(con, filePart);
    }
}
