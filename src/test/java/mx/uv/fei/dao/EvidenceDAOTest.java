package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.EvidenceDAO;
import mx.uv.fei.logic.Evidence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class EvidenceDAOTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testAddProjectEvidenceSucces() throws SQLException {
        Evidence evidence = new Evidence();
        EvidenceDAO evidenceDAO = new EvidenceDAO();

        evidence.setEvidenceTitle("Evidencia1");
        evidence.setEvidenceDescription("descripcion1");
        evidence.setProfessorId(1);
        evidence.setAdvancementId(1);
        evidence.setProjectId(1);
        evidence.setStudentId("ZS21013865");

        int expectedResult = 1;
        int result = evidenceDAO.addEvidence(evidence);
    }

    @Test
    void updateProjectEvidenceGradeById() {
    }

    @Test
    void getProjectEvidenceByStudentId() {
    }
}