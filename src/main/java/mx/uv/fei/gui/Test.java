package mx.uv.fei.gui;

import mx.uv.fei.logic.ProjectPetitions;
import mx.uv.fei.logic.ProjectPetitionsDAO;
import mx.uv.fei.logic.Student;
import mx.uv.fei.logic.StudentDAO;

import java.sql.SQLException;

public class Test {
    public static void main(String args[]){
        Student student = new Student();
        StudentDAO studentDAO = new StudentDAO();

        student.setTuition("ZS21013865");
        student.setName("Bryam Danae");
        student.setLastName("Morales");
        student.setMothersLastName("García");
        student.setAcademicEmail("zs21013865@estdiantes.uv.mx");
        student.setNRC(74293);
        student.setUserID(2);
        try{
            studentDAO.insertStudent(student);
            studentDAO.getStudents();
        } catch (SQLException exception){
            exception.getErrorCode();
        }
        ProjectPetitions projectPetition = new ProjectPetitions();
        ProjectPetitionsDAO projectPetitionDAO = new ProjectPetitionsDAO();

        projectPetition.setProjectID(1);
        projectPetition.setStudentTuition("ZS21013865");
        projectPetition.setDescription("Ejemplo");
        try{
            projectPetitionDAO.createProjectPetition(projectPetition);
        } catch (SQLException exception){
            exception.getErrorCode();
        }
    }
}

