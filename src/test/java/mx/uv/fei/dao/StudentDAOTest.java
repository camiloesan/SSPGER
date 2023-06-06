package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.dao.implementations.ProjectRequestDAO;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.dao.implementations.StudentDAO;
import mx.uv.fei.logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentDAOTest {

    @BeforeEach
    void setUp() throws SQLException {
        UserDAO accessAccountDAO = new UserDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        
        var testAccessAccount = new AccessAccount();
        var testStudent = new Student();
        testAccessAccount.setUsername("casasLuisM");
        testAccessAccount.setUserPassword("LMCV");
        testAccessAccount.setUserType("Estudiante");
        testAccessAccount.setUserEmail("zs21013875@estudiantes.uv.mx");
        testStudent.setStudentID("S21013875");
        testStudent.setName("Luis Manuel");
        testStudent.setLastName("Casas Vazquez");
        accessAccountDAO.addStudentUserTransaction(testAccessAccount,testStudent);
        
        var verifiedProject = new Project();
        verifiedProject.setAcademicBodyId("UV-CA-127");
        verifiedProject.setInvestigationProjectName("Ingeniería de Software e Inteligencia Artificial");
        verifiedProject.setLGAC_Id(1);
        verifiedProject.setInvestigationLine("Ingeniería de Requisitos");
        verifiedProject.setApproximateDuration("12 meses");
        verifiedProject.setModalityId(1);
        verifiedProject.setReceptionWorkName("Análisis de técnicas de procesamiento de lenguaje natural para la " +
                "ingeniería de requisitos en idioma español.");
        verifiedProject.setRequisites("Lectura de documentos en inglés, trabajo autónomo, Interés por la investigación." +
                " Requerimientos de Software, Inteligencia Artificial aplicada a la Ingeniería de Software.");
        verifiedProject.setDirectorName("Dr. Angel Juan Sanchez Garcia");
        verifiedProject.setCodirectorName("Dr. Jorge Octavio Ocharan Hernandez");
        verifiedProject.setStudentsParticipating(1);
        verifiedProject.setInvestigationProjectDescription("Diferentes factores que impactan en el tiempo y confiabilidad" +
                " de un proyecto de Software pueden deberse al elemento humano, el cual puede ser solventado mediante " +
                "el desarrollo de mecanismos para realizar tareas de manera automática y tomando decisiones autónomas, " +
                "haciendo más eficiente la labor de un Ingeniero de Software y evitando el sesgo del humano. Por otro " +
                "lado, la Inteligencia Artificial, si bien cuenta con software libre y propietario en diferentes dominios" +
                " de aplicación, carece hoy en día de metodologías de desarrollo específicas que permitan la construcción" +
                " de sistemas de cómputo de calidad, y que se adapten al tipo de herramientas que favorezcan la experimentación" +
                " e investigación en el área de Inteligencia Artificial. Por lo tanto, surge la necesidad de colaborar entre " +
                "ambas disciplinas para fortalecer los resultados de ambas áreas de investigación, aportando las fortalezas" +
                " de cada una en la otra.\n" +
                "\n" +
                "Motivado por lo mencionado anteriormente, el presente proyecto busca desarrollar la colaboración entre la" +
                " Ingeniería de Software y la Inteligencia Artificial, para contribuir al desarrollo de ambas disciplinas," +
                " mediante la aplicación de técnicas de Inteligencia Artificial que aporten soluciones a problemas de " +
                "procesos y del producto de software, así como la aplicación de estrategias, métodos y procesos de desarrollo" +
                " de software que soporten a la investigación, desarrollo, y experimentación en el ámbito de la inteligencia" +
                " Artificial.");
        verifiedProject.setReceptionWorkDescription("El procesamiento del lenguaje natural para la ingeniería de requisitos" +
                " (NLP4RE, por sus siglas en inglés) es un área de investigación y desarrollo que busca aplicar técnicas," +
                " herramientas y recursos de procesamiento del lenguaje natural (NLP, por sus siglas en inglés) al proceso" +
                " de ingeniería de requisitos (RE) con el fin de asistir a los ingenieros de requisitos en la realización" +
                " de diferentes actividades propias del desarrollo de requisitos de software. Desde la década de 1980, " +
                "es bien conocido el importante rol que tiene el lenguaje natural en la ingeniería de requisitos pues es" +
                " éste el medio común en el que se expresan y especifican los requisitos de software en la industria hasta " +
                "el día de hoy.\n" +
                "\n" +
                "Por otro lado, han existido diferentes esfuerzos para utilizar técnicas, herramientas y recursos de NLP" +
                " los cuales se han visto potenciados por los avances que se tienen actualmente en el área. Estas " +
                "investigaciones se han centrado principalmente en el descubrimiento, análisis, modelado, verificación," +
                " validación y gestión de los requisitos de software y buscan que la aplicación de los avances del NLP " +
                "ahorren tiempo a los ingenieros de requisitos y aumenten su productividad.\n" +
                "\n" +
                "En cuanto al uso de técnicas de la Inteligencia Artificial para asistir a las actividades de la Ingeniería" +
                " de Software, actualmente en la FEI se han desarrollado diferentes proyectos que se han centrado en la " +
                "clasificación y priorización de requisitos de software con conjuntos de datos en el idioma inglés. Sin embargo," +
                " aún no se cuenta con un trabajo que se centre en utilizar técnicas, herramientas y recursos del NLP. " +
                "El objetivo de este trabajo es realizar una revisión crítica de las técnicas que se utilizan en el " +
                "procesamiento del español como lenguaje natural para apoyar las actividades de la ingeniería de requisitos." +
                " Se realizará un análisis de la literatura, obtenida de diferentes bases de datos, y una síntesis de los " +
                "hallazgos de tal forma que sea posible identificar tendencias y áreas de oportunidad para el desarrollo " +
                "de aplicaciones que asistan a los ingenieros de requisitos.");
        verifiedProject.setExpectedResults("- Revisión de la literatura (sistemática)\n" +
                "- Paquete de artefactos producto de la revisión\n" +
                "- Artículo publicado\n" +
                "- Monografía");
        verifiedProject.setRecommendedBibliography("Cruz, B. D., Jayaraman, B., Dwarakanath, A., & McMillan, C. (2017). " +
                "\"Detecting Vague Words & Phrases in Requirements Documents in a Multilingual Environment.\" 2017 IEEE " +
                "25th International Requirements Engineering Conference (RE), 233–242. https://doi.org/10.1109/RE.2017.24\n" +
                "\n" +
                "IEEE. (2011). \"Systems and software engineering -- Life cycle processes -- Requirements engineering.\" " +
                "En ISO/IEC/IEEE 29148:2011(E). https://doi.org/10.1109/IEEESTD.2011.6146379\n" +
                "\n" +
                "Leon-Paredes, G. A., Palomeque-Leon, W. F., Gallegos-Segovia, P. L., Vintimilla-Tapia, P. E., Bravo-Torres," +
                " J. F., Barbosa-Santillan, L. I., & Paredes-Pinos, M. M. (2019). \"Presumptive Detection of Cyberbullying" +
                " on Twitter through Natural Language Processing and Machine Learning in the Spanish Language.\" 2019 IEEE " +
                "CHILEAN Conference on Electrical, Electronics Engineering, Information and Communication Technologies " +
                "(CHILECON), 1–7. https://doi.org/10.1109/CHILECON47746.2019.8987684\n" +
                "\n" +
                "Lopez-Hernandez, D. A., Mezura-Montes, E., Ocharan-Hernandez, J. O., & Sanchez-Garcia, A. J. (2021). " +
                "\"Non-functional Requirements Classification using Artificial Neural Networks.\" 2021 IEEE International" +
                " Autumn Meeting on Power, Electronics and Computing (ROPEC), 1–6. https://doi.org/10.1109/ROPEC53248.2021.9668132\n" +
                "\n" +
                "Limaylla-Lunarejo, M.-I., Condori-Fernandez, N., & Luaces, M. R. (2022). \"Towards an automatic requirements" +
                " classification in a new Spanish dataset.\" 2022 IEEE 30th International Requirements Engineering Conference" +
                " (RE), 270–271. https://doi.org/10.1109/RE54965.2022.00039\n" +
                "\n" +
                "Perez-Verdejo, J. M., Sanchez-Garcia, A. J., & Ocharan-Hernandez, J. O. (2020). \"A Systematic Literature" +
                " Review on Machine Learning for Automated Requirements Classification.\" 2020 8th International Conference" +
                " in Software Engineering Research and Innovation (CONISOFT), 21–28. https://doi.org/10.1109/CONISOFT50191.2020.00014\n" +
                "\n" +
                "Pérez-Verdejo, J. M., Sánchez-García, Á. J., Ocharán-Hernández, J. O., Mezura-Montes, E., & Cortés-Verdín," +
                " K. (2021). \"Requirements and GitHub Issues: An Automated Approach for Quality Requirements Classification." +
                "\" Programming and Computer Software, 47(8), 704–721. https://doi.org/10.1134/S0361768821080193\n" +
                "\n" +
                "Wiegers, K. E., & Beatty, J. (2013). \"Software Requirements.\" En Microsoft Press (Third Edition, pp. 3–23)." +
                " Microsoft Press.\n" +
                "\n" +
                "Zhang, D., & Tsai, J. J. P. (2003). \"Machine Learning and Software Engineering.\" Software Quality Journal," +
                " 11(2), 87–119. https://doi.org/10.1023/A:1023760326768");
        projectDAO.addProject(verifiedProject);
        projectDAO.updateProjectState(projectDAO.getProjectIDByTitle("Análisis de técnicas de procesamiento de lenguaje natural para la " +
                "ingeniería de requisitos en idioma español."),"Verificado");
        
        ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setStudentId("S21013875");
        projectRequest.setProjectID(projectDAO.getProjectIDByTitle("Análisis de técnicas de procesamiento de lenguaje natural para la " +
                "ingeniería de requisitos en idioma español."));
        projectRequest.setDescription("Motivos de la solicitud");
        projectRequestDAO.createProjectRequest(projectRequest);
        projectRequestDAO.validateProjectRequest("Aceptado",projectRequestDAO.getProjectRequestIDByStudentID("S21013875"));
    }

    @AfterEach
    void tearDown() throws SQLException {
       UserDAO accessAccountDAO = new UserDAO();
       ProjectDAO projectDAO = new ProjectDAO();
       ProjectRequestDAO projectRequestDAO = new ProjectRequestDAO();
       
       
       accessAccountDAO.deleteUserByUsername("casasLuisM");
       projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Análisis de técnicas de procesamiento de lenguaje natural para la " +
               "ingeniería de requisitos en idioma español."));
       projectRequestDAO.deleteProjectRequest(projectRequestDAO.getProjectRequestIDByStudentID("S21013857"));
    }

    @Test
    void testGetNameByStudentIDSuccess() throws SQLException {
        System.out.println("Test getNameByStudentID");
        
        var studentDAO = new StudentDAO();
        String expectedResult = "Luis Manuel";
        String result = studentDAO.getNamebyStudentID("S21013875");
        
        System.out.println("Expected result:");
        System.out.println(expectedResult);
        System.out.println("Actual result:");
        System.out.println(result);
        
        assertEquals(expectedResult, result);
    }

    @Test
    void testGetNameByStudentIDNotFound() throws SQLException {
        System.out.println("Test getNameByStudentID student not found");
        var studentDAO = new StudentDAO();
        
        String result = studentDAO.getNamebyStudentID("S21013899");
        assertTrue(result == null || result.equals(""));
    }

    @Test
    void testGetStudentIDByUsernameSuccess() throws SQLException {
        System.out.println("Test getStudentIDByUsername");
        var studentDAO = new StudentDAO();
        String expectedResult = "S21013875";
        String result = studentDAO.getStudentIdByUsername("casasLuisM");
        
        System.out.println("Expected result:");
        System.out.println(expectedResult);
        System.out.println("Actual result:");
        System.out.println(result);
        assertEquals(expectedResult, result);
    }

    @Test
    void testGetStudentIDByUsernameNotFound() throws SQLException {
        System.out.println("Test getStudentIDByUsername student not found");
        var studentDAO = new StudentDAO();
        
        String result = studentDAO.getStudentIdByUsername("espejoCamiloS");
        assertTrue(result == null || result.equals(""));
    }

    @Test
    void testGetStudentsByProjectID() throws SQLException {
        System.out.println("Test getStudentsByProjectID");
        var studentDAO = new StudentDAO();
        var projectDAO = new ProjectDAO();
        
        List<Student> expectedList = new ArrayList<>();
        var student = new Student();
        student.setStudentID("S21013875");
        student.setFullName("Luis Manuel Casas Vazquez");
        expectedList.add(student);
        
        var actualList = new ArrayList<>(studentDAO.getStudentsByProjectID(projectDAO.getProjectIDByTitle("Análisis de técnicas de procesamiento de lenguaje natural para la " +
                "ingeniería de requisitos en idioma español.")));
        
        System.out.println("Expected list:");
        for (Student simpleStudent : expectedList){
            System.out.println("ID: " + simpleStudent.getStudentID());
            System.out.println("Full Name: " + simpleStudent.getFullName());
            System.out.println("-----------------");
        }
        System.out.println("Expected list:");
        for (Student simpleStudent : actualList){
            System.out.println("ID: " + simpleStudent.getStudentID());
            System.out.println("Full Name: " + simpleStudent.getFullName());
            System.out.println("-----------------");
        }
        assertEquals(expectedList,actualList);
    }
}