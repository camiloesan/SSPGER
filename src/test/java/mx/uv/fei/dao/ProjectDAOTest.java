package mx.uv.fei.dao;

import mx.uv.fei.dao.implementations.ProfessorDAO;
import mx.uv.fei.dao.implementations.ProjectDAO;
import mx.uv.fei.dao.implementations.UserDAO;
import mx.uv.fei.logic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDAOTest {

    @BeforeEach
    void setUp() throws SQLException {
        var userDAO = new UserDAO();
        var projectDAO = new ProjectDAO();
        
        var testAccessAccount = new AccessAccount();
        var testProfessor = new Professor();
        testAccessAccount.setUsername("perezJuanC");
        testAccessAccount.setUserPassword("JCPA");
        testAccessAccount.setUserType("Profesor");
        testAccessAccount.setUserEmail("jperez@mail.com");
        testProfessor.setProfessorName("Juan Carlos");
        testProfessor.setProfessorLastName("Perez Arriaga");
        testProfessor.setProfessorDegree("MCC.");
        userDAO.addProfessorUserTransaction(testAccessAccount,testProfessor);
        
        var testAccessAccount2 = new AccessAccount();
        var testProfessor2 = new Professor();
        testAccessAccount2.setUsername("sanchezAngelJ");
        testAccessAccount2.setUserPassword("AJSG");
        testAccessAccount2.setUserType("Profesor");
        testAccessAccount2.setUserEmail("asanchez@mail.com");
        testProfessor2.setProfessorName("Angel Juan");
        testProfessor2.setProfessorLastName("Sanchez Garcia");
        testProfessor2.setProfessorDegree("Dr.");
        userDAO.addProfessorUserTransaction(testAccessAccount2,testProfessor2);
        
        var testAccessAccount3 = new AccessAccount();
        var testProfessor3 = new Professor();
        testAccessAccount3.setUsername("limonHectorX");
        testAccessAccount3.setUserPassword("HXLR");
        testAccessAccount3.setUserType("Profesor");
        testAccessAccount3.setUserEmail("hlimon@mail.com");
        testProfessor3.setProfessorName("Hector Xavier");
        testProfessor3.setProfessorLastName("Limon Riaño");
        testProfessor3.setProfessorDegree("Dr.");
        userDAO.addProfessorUserTransaction(testAccessAccount3, testProfessor3);
        
        var testAccessAccount4 = new AccessAccount();
        var testProfessor4 = new Professor();
        testAccessAccount4.setUsername("reyesItzelA");
        testAccessAccount4.setUserPassword("IARF");
        testAccessAccount4.setUserType("Profesor");
        testAccessAccount4.setUserEmail("ireyes@mail.com");
        testProfessor4.setProfessorName("Itzel Alessandra");
        testProfessor4.setProfessorLastName("Reyes Flores");
        testProfessor4.setProfessorDegree("MCC.");
        userDAO.addProfessorUserTransaction(testAccessAccount4, testProfessor4);
        
        var testAccessAccount5 = new AccessAccount();
        var testProfessor5 = new Professor();
        testAccessAccount5.setUsername("dominguezIsidroS");
        testAccessAccount5.setUserPassword("SDI");
        testAccessAccount5.setUserType("Profesor");
        testAccessAccount5.setUserEmail("sdominguez@mail.com");
        testProfessor5.setProfessorName("Saúl");
        testProfessor5.setProfessorLastName("Dominguez Isidro");
        testProfessor5.setProfessorDegree("Dr.");
        userDAO.addProfessorUserTransaction(testAccessAccount5, testProfessor5);
        
        var testAccessAccount6 = new AccessAccount();
        var testProfessor6 = new Professor();
        testAccessAccount6.setUsername("ocharanJorgeO");
        testAccessAccount6.setUserPassword("JOOH");
        testAccessAccount6.setUserType("Profesor");
        testAccessAccount6.setUserEmail("jocharan@mail.com");
        testProfessor6.setProfessorName("Jorge Octavio");
        testProfessor6.setProfessorLastName("Ocharan Hernandez");
        testProfessor6.setProfessorDegree("Dr.");
        userDAO.addProfessorUserTransaction(testAccessAccount6, testProfessor6);
        
        var testAccessAccount7 = new AccessAccount();
        var testProfessor7 = new Professor();
        testAccessAccount7.setUsername("cortesMariaK");
        testAccessAccount7.setUserPassword("MKCV");
        testAccessAccount7.setUserType("RepresentanteCA");
        testAccessAccount7.setUserEmail("cmaria@mail.com");
        testProfessor7.setProfessorName("Maria Karen");
        testProfessor7.setProfessorLastName("Cortes Verdin");
        testProfessor7.setProfessorDegree("Dra.");
        userDAO.addProfessorUserTransaction(testAccessAccount7, testProfessor7);
        
        var testAccessAccount8 = new AccessAccount();
        var testProfessor8 = new Professor();
        testAccessAccount8.setUsername("alonsoOscarR");
        testAccessAccount8.setUserPassword("OAR");
        testAccessAccount8.setUserType("Profesor");
        testAccessAccount8.setUserEmail("oalonso@mail.com");
        testProfessor8.setProfessorName("Oscar");
        testProfessor8.setProfessorLastName("Alonso Ramirez");
        testProfessor8.setProfessorDegree("Dr.");
        userDAO.addProfessorUserTransaction(testAccessAccount8,testProfessor8);
        
        var unverifiedProject1 = new Project();
        unverifiedProject1.setAcademicBodyId("UV-CA-127");
        unverifiedProject1.setInvestigationProjectName(
                "Hacia un Modelo de Campus Accesible: Facultad de Estadística e Informática");
        unverifiedProject1.setLGAC_Id(2);
        unverifiedProject1.setInvestigationLine("Se orienta al estudio de diversas propiedades, enfoques, métodos de " +
                "modelado y herramientas que conforman cada una de las diversas tecnologías aplicables al desarrollo " +
                "del software con vistas a su adaptación, mejora y sustitución en el medio nacional");
        unverifiedProject1.setApproximateDuration("12 meses");
        unverifiedProject1.setModalityId(5);
        unverifiedProject1.setReceptionWorkName("Recomendaciones de Accesibilidad para el Desarrollo de Software");
        unverifiedProject1.setRequisites(
                "Tecnologías para la construcción de software, Principios de Construcción de Software");
        unverifiedProject1.setDirectorName("MCC. Juan Carlos Perez Arriaga");
        unverifiedProject1.setCodirectorName("Dra. Maria Karen Cortes Verdin");
        unverifiedProject1.setStudentsParticipating(1);
        unverifiedProject1.setInvestigationProjectDescription(
                "Actualmente la democratización de la educación representa un" +
                " reto para cualquier Institución de Educación Superior, al mismo tiempo que el término " +
                        "'Institución Incluyente'" +
                " cobra mayor sentido como parte de esta democratización. Desde la perspectiva de inclusión, " +
                        "se pretende que " +
                "cualquier institución garantice el derecho a la educación respetando las diversas necesidades, " +
                        "capacidades " +
                "y características de cada uno de los estudiantes, al mismo tiempo que se " +
                        "orientan políticas y prácticas " +
                "educativas a mejorar el proceso de enseñanza-aprendizaje y a suprimir cualquier " +
                        "forma de discriminación " +
                "que surja al interior de las mismas.\n" +
                "\n" +
                "La Universidad Veracruzana como institución de educación superior pública, " +
                        "tiene el compromiso de brindar" +
                " acceso a la educación superior de calidad a todos los sectores de la sociedad, " +
                        "principalmente a la " +
                "sociedad Veracruzana, de tal modo que la universidad sea motor " +
                        "de desarrollo económico y crecimiento " +
                "sustentable en beneficio de la población del estado de Veracruz y de la nación.\n" +
                "\n" +
                "Desde hace algunos años al interior de la Universidad Veracruzana se " +
                        "operan programas transversales " +
                "fundamentados en la responsabilidad social y universitaria, y que promueven entre otras cosas, la " +
                "equidad de género, la inclusión y el respeto a los derechos humanos. " +
                        "Como parte de esta visión institucional, " +
                "en la Facultad de Estadística e Informática se han desarrollado " +
                        "estrategias tecnológicas orientadas a" +
                " atender necesidades de inclusión que contribuyan al ingreso, permanencia y egreso de personas con " +
                "discapacidad.\n" +
                "\n" +
                "Este proyecto se encuentra alineado con las actividades que se han venido " +
                        "realizando en la Facultad de " +
                "Estadística e Informática de la Universidad Veracruzana, " +
                        "particularmente en el Cuerpo Académico (CA) " +
                "'Ingeniería de Tecnología de Software', y que aplica a estudiantes de " +
                        "licenciatura como de posgrado.");
        unverifiedProject1.setReceptionWorkDescription("En la actualidad existen diversas guías, " +
                "técnicas y métodos que " +
                "contribuyen al desarrollo de software accesible. Sin embargo, a pesar de la " +
                "existencia de estas " +
                "prácticas, los defectos relacionados con la ausencia de características de " +
                "accesibilidad en productos " +
                "de software persisten. Por otra parte, debido a eventos generados en temas de " +
                "salud pública, el uso de" +
                " plataformas de software se incrementó en los años pasados. Uno de los problemas " +
                "reportados fue que las " +
                "plataformas no eran accesibles o que carecían de compatibilidad con tecnologías de " +
                "asistencia como " +
                "lectores en pantalla, magnificadores, asistentes digitales, entre otros elementos. " +
                "Debido a lo anterior " +
                "resulta pertinente trabajar en proyectos que tengan como objetivo contribuir al " +
                "desarrollo de software " +
                "accesible. El presente trabajo tiene como finalidad, realizar una recopilación de " +
                "las prácticas que " +
                "evidencien utilidad para el desarrollo de software accesible, al mismo tiempo " +
                "que se genera un documento" +
                " que sirva como guía en la selección de prácticas que orienten al " +
                "ingeniero de software en el proceso" +
                " de desarrollo.");
        unverifiedProject1.setExpectedResults("Documento que contenga:\n" +
                "- Reporte de la revisión sistemática de la literatura sobre las prácticas de accesibilidad en el " +
                "desarrollo de software que evidencien su aplicación en las distintas " +
                "fases del proceso de desarrollo\n" +
                "- Artículo para publicación en evento académico\n" +
                "- Documento que sirva como guía para el desarrollo de software a partir de " +
                "las prácticas identificadas");
        unverifiedProject1.setRecommendedBibliography("Luciana A.M. Zaina, Renata P.M. Fortes, " +
                "Vitor Casadei, Leornardo Seiji " +
                "Nozaki, Débora Maria Barroso Paiva, \"Preventing accessibility barriers: " +
                "Guidelines for using user " +
                "interface design patterns in mobile applications,\" Journal of Systems and " +
                "Software, Volume 186, 2022," +
                " 111213, ISSN 0164-1212, https://doi.org/10.1016/j.jss.2021.111213.\n" +
                "\n" +
                "N. M., P. Chawla and A. Rana, \"A Practitioner’s Approach to " +
                "Assess the WCAG 2.0 Website Accessibility " +
                "Challenges,\" 2019 Amity International Conference on Artificial Intelligence " +
                "(AICAI), 2019, pp. 958-966," +
                " doi: 10.1109/AICAI.2019.8701320.\n" +
                "\n" +
                "Claire Kearney-Volpe and Amy Hurst, \"Accessible Web Development: " +
                "Opportunities to Improve the Education" +
                " and Practice of web Development with a Screen Reader,\" ACM Trans. " +
                "Access. Comput. 14, 2, Article 8 " +
                "(June 2021), 32 pages, https://doi.org/10.1145/3458024.\n" +
                "\n" +
                "Débora Maria Barroso Paiva, André Pimenta Freire, Renata Pontin de " +
                "Mattos Fortes, \"Accessibility and " +
                "Software Engineering Processes: A Systematic Literature Review,\" Journal of " +
                "Systems and Software, Volume" +
                " 171, 2021, 110819, ISSN 0164-1212, https://doi.org/10.1016/j.jss.2020.110819.");
        projectDAO.addProject(unverifiedProject1);
        projectDAO.setDirectorIDtoProject(unverifiedProject1);
        projectDAO.setCodirectorIDtoProject(unverifiedProject1);
        
        var verifiedProject = new Project();
        verifiedProject.setAcademicBodyId("UV-CA-127");
        verifiedProject.setInvestigationProjectName("Ingeniería de Software e Inteligencia Artificial");
        verifiedProject.setLGAC_Id(1);
        verifiedProject.setInvestigationLine("Ingenieria de Requisitos");
        verifiedProject.setApproximateDuration("12 meses");
        verifiedProject.setModalityId(1);
        verifiedProject.setReceptionWorkName("Análisis de técnicas de procesamiento de lenguaje natural para la " +
                "ingeniería de requisitos en idioma español.");
        verifiedProject.setRequisites(
                "Lectura de documentos en inglés, trabajo autónomo, Interés por la investigación." +
                " Requerimientos de Software, Inteligencia Artificial aplicada a la Ingeniería de Software.");
        verifiedProject.setDirectorName("Dr. Angel Juan Sanchez Garcia");
        verifiedProject.setCodirectorName("Dr. Jorge Octavio Ocharan Hernandez");
        verifiedProject.setStudentsParticipating(1);
        verifiedProject.setInvestigationProjectDescription(
                "Diferentes factores que impactan en el tiempo y confiabilidad" +
                " de un proyecto de Software pueden deberse al elemento humano, el cual " +
                        "puede ser solventado mediante " +
                "el desarrollo de mecanismos para realizar tareas de manera automática y " +
                        "tomando decisiones autónomas, " +
                "haciendo más eficiente la labor de un Ingeniero de Software y evitando el " +
                        "sesgo del humano. Por otro " +
                "lado, la Inteligencia Artificial, si bien cuenta con software libre y " +
                        "propietario en diferentes dominios" +
                " de aplicación, carece hoy en día de metodologías de desarrollo específicas " +
                        "que permitan la construcción" +
                " de sistemas de cómputo de calidad, y que se adapten al tipo de herramientas " +
                        "que favorezcan la experimentación" +
                " e investigación en el área de Inteligencia Artificial. Por lo tanto, surge " +
                        "la necesidad de colaborar entre " +
                "ambas disciplinas para fortalecer los resultados de ambas áreas de " +
                        "investigación, aportando las fortalezas" +
                " de cada una en la otra.\n" +
                "\n" +
                "Motivado por lo mencionado anteriormente, el presente proyecto busca " +
                        "desarrollar la colaboración entre la" +
                " Ingeniería de Software y la Inteligencia Artificial, para contribuir al " +
                        "desarrollo de ambas disciplinas," +
                " mediante la aplicación de técnicas de Inteligencia Artificial que aporten " +
                        "soluciones a problemas de " +
                "procesos y del producto de software, así como la aplicación de estrategias, " +
                        "métodos y procesos de desarrollo" +
                " de software que soporten a la investigación, desarrollo, y experimentación " +
                        "en el ámbito de la inteligencia" +
                " Artificial.");
        verifiedProject.setReceptionWorkDescription("El procesamiento del lenguaje natural " +
                "para la ingeniería de requisitos" +
                " (NLP4RE, por sus siglas en inglés) es un área de investigación y desarrollo " +
                "que busca aplicar técnicas," +
                " herramientas y recursos de procesamiento del lenguaje natural " +
                "(NLP, por sus siglas en inglés) al proceso" +
                " de ingeniería de requisitos (RE) con el fin de asistir a los ingenieros de " +
                "requisitos en la realización" +
                " de diferentes actividades propias del desarrollo de requisitos de software. " +
                "Desde la década de 1980, " +
                "es bien conocido el importante rol que tiene el lenguaje natural en la ingeniería de " +
                "requisitos pues es" +
                " éste el medio común en el que se expresan y especifican los requisitos de " +
                "software en la industria hasta " +
                "el día de hoy.\n" +
                "\n" +
                "Por otro lado, han existido diferentes esfuerzos para utilizar técnicas, " +
                "herramientas y recursos de NLP" +
                " los cuales se han visto potenciados por los avances que se tienen " +
                "actualmente en el área. Estas " +
                "investigaciones se han centrado principalmente en el descubrimiento, " +
                "análisis, modelado, verificación," +
                " validación y gestión de los requisitos de software y buscan que la " +
                "aplicación de los avances del NLP " +
                "ahorren tiempo a los ingenieros de requisitos y aumenten su productividad.\n" +
                "\n" +
                "En cuanto al uso de técnicas de la Inteligencia Artificial para asistir a las " +
                "actividades de la Ingeniería" +
                " de Software, actualmente en la FEI se han desarrollado diferentes proyectos " +
                "que se han centrado en la " +
                "clasificación y priorización de requisitos de software con conjuntos de datos " +
                "en el idioma inglés. Sin embargo," +
                " aún no se cuenta con un trabajo que se centre en utilizar técnicas, " +
                "herramientas y recursos del NLP. " +
                "El objetivo de este trabajo es realizar una revisión crítica de las técnicas " +
                "que se utilizan en el " +
                "procesamiento del español como lenguaje natural para apoyar las " +
                "actividades de la ingeniería de requisitos." +
                " Se realizará un análisis de la literatura, obtenida de diferentes " +
                "bases de datos, y una síntesis de los " +
                "hallazgos de tal forma que sea posible identificar tendencias y " +
                "áreas de oportunidad para el desarrollo " +
                "de aplicaciones que asistan a los ingenieros de requisitos.");
        verifiedProject.setExpectedResults("- Revisión de la literatura (sistemática)\n" +
                "- Paquete de artefactos producto de la revisión\n" +
                "- Artículo publicado\n" +
                "- Monografía");
        verifiedProject.setRecommendedBibliography("Cruz, B. D., Jayaraman, B., Dwarakanath," +
                " A., & McMillan, C. (2017). " +
                "\"Detecting Vague Words & Phrases in Requirements Documents in a Multilingual " +
                "Environment.\" 2017 IEEE " +
                "25th International Requirements Engineering Conference (RE), 233–242. " +
                "https://doi.org/10.1109/RE.2017.24\n" +
                "\n" +
                "IEEE. (2011). \"Systems and software engineering -- Life cycle processes -- " +
                "Requirements engineering.\" " +
                "En ISO/IEC/IEEE 29148:2011(E). https://doi.org/10.1109/IEEESTD.2011.6146379\n" +
                "\n" +
                "Leon-Paredes, G. A., Palomeque-Leon, W. F., Gallegos-Segovia, P. L., " +
                "Vintimilla-Tapia, P. E., Bravo-Torres," +
                " J. F., Barbosa-Santillan, L. I., & Paredes-Pinos, M. M. (2019). " +
                "\"Presumptive Detection of Cyberbullying" +
                " on Twitter through Natural Language Processing and Machine Learning in " +
                "the Spanish Language.\" 2019 IEEE " +
                "CHILEAN Conference on Electrical, Electronics Engineering, Information " +
                "and Communication Technologies " +
                "(CHILECON), 1–7. https://doi.org/10.1109/CHILECON47746.2019.8987684\n" +
                "\n" +
                "Lopez-Hernandez, D. A., Mezura-Montes, E., Ocharan-Hernandez, J. O., & " +
                "Sanchez-Garcia, A. J. (2021). " +
                "\"Non-functional Requirements Classification using Artificial Neural Networks." +
                "\" 2021 IEEE International" +
                " Autumn Meeting on Power, Electronics and Computing (ROPEC), 1–6. " +
                "https://doi.org/10.1109/ROPEC53248.2021.9668132\n" +
                "\n" +
                "Limaylla-Lunarejo, M.-I., Condori-Fernandez, N., & Luaces, M. R. (2022). " +
                "\"Towards an automatic requirements" +
                " classification in a new Spanish dataset.\" 2022 IEEE 30th International " +
                "Requirements Engineering Conference" +
                " (RE), 270–271. https://doi.org/10.1109/RE54965.2022.00039\n" +
                "\n" +
                "Perez-Verdejo, J. M., Sanchez-Garcia, A. J., & Ocharan-Hernandez, J. O. " +
                "(2020). \"A Systematic Literature" +
                " Review on Machine Learning for Automated Requirements Classification.\" " +
                "2020 8th International Conference" +
                " in Software Engineering Research and Innovation (CONISOFT), 21–28. " +
                "https://doi.org/10.1109/CONISOFT50191.2020.00014\n" +
                "\n" +
                "Pérez-Verdejo, J. M., Sánchez-García, Á. J., Ocharán-Hernández, J. O., " +
                "Mezura-Montes, E., & Cortés-Verdín," +
                " K. (2021). \"Requirements and GitHub Issues: An Automated Approach for " +
                "Quality Requirements Classification." +
                "\" Programming and Computer Software, 47(8), 704–721. https://doi.org/10.1134/S0361768821080193\n" +
                "\n" +
                "Wiegers, K. E., & Beatty, J. (2013). \"Software Requirements.\" " +
                "En Microsoft Press (Third Edition, pp. 3–23)." +
                " Microsoft Press.\n" +
                "\n" +
                "Zhang, D., & Tsai, J. J. P. (2003). \"Machine Learning and Software Engineering.\" " +
                "Software Quality Journal," +
                " 11(2), 87–119. https://doi.org/10.1023/A:1023760326768");
        projectDAO.addProject(verifiedProject);
        projectDAO.setDirectorIDtoProject(verifiedProject);
        projectDAO.setCodirectorIDtoProject(verifiedProject);
        projectDAO.updateProjectState(projectDAO.getProjectIDByTitle(
                "Análisis de técnicas de procesamiento de lenguaje natural para la " +
                "ingeniería de requisitos en idioma español."),"Verificado");
        
        var unverifiedProject2 = new Project();
        unverifiedProject2.setAcademicBodyId("UV-CA-127");
        unverifiedProject2.setInvestigationProjectName(" ");
        unverifiedProject2.setLGAC_Id(2);
        unverifiedProject2.setInvestigationLine(" ");
        unverifiedProject2.setApproximateDuration("6 meses");
        unverifiedProject2.setModalityId(2);
        unverifiedProject2.setReceptionWorkName("Análisis de las tecnologías para el desarrollo de Development Bots");
        unverifiedProject2.setRequisites("Lectura de documentos en inglés, trabajo autónomo, " +
                "Interés por la investigación." +
                " Diseño de Software. Tecnologías para la Construcción, Desarrollo de Sistemas en Red y " +
                "Desarrollo de Aplicaciones.");
        unverifiedProject2.setDirectorName("Dr. Jorge Octavio Ocharan Hernandez");
        unverifiedProject2.setCodirectorName("Dr. Hector Xavier Limon Riaño");
        unverifiedProject2.setStudentsParticipating(1);
        unverifiedProject2.setInvestigationProjectDescription(" ");
        unverifiedProject2.setReceptionWorkDescription(
                "Los robots de software o bots son aplicaciones de software autónomo " +
                        "que se han utilizado para realizar " +
                "tareas repetitivas o simples. Existe una investigación importante en " +
                        "torno a los bots sociales y de chat" +
                " que interactúan con personas para ayudarles en sus tareas. Además, los bots " +
                        "se utilizan para automatizar" +
                " tareas realizadas por ingenieros de software y equipos de desarrollo en su " +
                        "trabajo diario. Investigaciones" +
                " recientes sugieren que los bots pueden ahorrar tiempo a los desarrolladores y" +
                        " aumentar significativamente" +
                " su productividad. En particular, se busca que los bots asistan en las diversas " +
                        "actividades de la ingeniería" +
                " de software.\n" +
                "En la FEI, actualmente hay un proyecto en curso que revisa la investigación " +
                        "sobre el uso de bots para " +
                "apoyar las actividades de desarrollo de software. Sin embargo, es necesario " +
                        "realizar un análisis del estado" +
                " actual de las diferentes tecnologías utilizadas en el desarrollo de este " +
                        "tipo de software. El objetivo de" +
                " este trabajo es llevar a cabo una revisión crítica de las tecnologías " +
                        "empleadas en el desarrollo de bots " +
                "para respaldar las actividades de desarrollo de software. Se realizará un " +
                        "análisis de la información obtenida" +
                " de diversas fuentes, tanto literatura académica como literatura gris, " +
                        "con el fin de identificar tendencias " +
                "y áreas de oportunidad para el desarrollo de este tipo de aplicaciones.");
        unverifiedProject2.setExpectedResults("- Reporte de la revisión multivocal de la literatura\n" +
                "- Paquete de artefactos producto de la revisión\n" +
                "- Borrador de artículo");
        unverifiedProject2.setRecommendedBibliography("Baudry, B., Chen, Z., Etemadi, K., Fu, H., Ginelli, D., " +
                "Kommrusch, S., Martinez, M., Monperrus, M., Ron, J., Ye, H., & Yu, Z. (2021). A Software " +
                "Repair Bot based on Continual Learning. IEEE Software, 0. https://doi.org/10.1109/MS.2021.3070743\n" +
                "\n" +
                "Beschastnikh, I., Lungu, M. F., & Zhuang, Y. (2017). Accelerating Software Engineering Research " +
                "Adoption with Analysis Bots. Proceedings of the 39th International " +
                "Conference on Software Engineering: " +
                "New Ideas and Emerging Results Track, 35–38. https://doi.org/10.1109/ICSE-NIER.2017.17\n" +
                "\n" +
                "Lebeuf, C., Zagalsky, A., Foucault, M., & Storey, M.-A. (2019). " +
                "Defining and Classifying Software Bots:" +
                "A Faceted Taxonomy. Proceedings of the 1st International Workshop on Bots in Software Engineering, " +
                "1–6. https://doi.org/10.1109/BotSE.2019.00008\n" +
                "\n" +
                "Matthies, C., Dobrigkeit, F., & Hesse, G. (2019). An Additional Set of " +
                "(Automated) Eyes: Chatbots for " +
                "Agile Retrospectives. Proceedings of the 1st International Workshop on " +
                "Bots in Software Engineering, " +
                "34–37. https://doi.org/10.1109/BotSE.2019.00017\n" +
                "\n" +
                "Peng, Z., Yoo, J., Xia, M., Kim, S., & Ma, X. (2018). Exploring How Software " +
                "Developers Work with Mention" +
                " Bot in GitHub. Proceedings of the Sixth International Symposium of Chinese CHI, 152–155. " +
                "https://doi.org/10.1145/3202667.3202694\n" +
                "\n" +
                "Storey, M.-A., & Zagalsky, A. (2016). Disrupting Developer Productivity " +
                "One Bot at a Time. Proceedings" +
                " of the 2016 24th ACM SIGSOFT International Symposium on Foundations " +
                "of Software Engineering, 928–931." +
                " https://doi.org/10.1145/2950290.2983989\n" +
                "\n" +
                "Wyrich, M., & Bogner, J. (2019). Towards an Autonomous Bot for Automatic Source Code Refactoring. " +
                "Proceedings of the 1st International Workshop on Bots in Software Engineering, 24–28. " +
                "https://doi.org/10.1109/BotSE.2019.00015");
        projectDAO.addProject(unverifiedProject2);
        projectDAO.setDirectorIDtoProject(unverifiedProject2);
        projectDAO.setCodirectorIDtoProject(unverifiedProject2);
        
        var projectDetails = new Project();
        projectDetails.setAcademicBodyId("UV-CA-127");
        projectDetails.setInvestigationProjectName(" ");
        projectDetails.setLGAC_Id(1);
        projectDetails.setInvestigationLine(
                "Estudio de los diversos métodos y enfoques para la gestión, modelado y " +
                "desarrollo de software, de manera que se obtenga software de calidad. " +
                        "Gestión de las diversas etapas del" +
                " proceso de desarrollo.");
        projectDetails.setApproximateDuration("12 meses");
        projectDetails.setModalityId(1);
        projectDetails.setReceptionWorkName("Prácticas de Ciberseguridad en Ingeniería de Software");
        projectDetails.setRequisites(
                "Tecnologías para la construcción de software, Principios de Construcción de Software," +
                " Diseño de Software, Procesos de Software, Administración de proyectos");
        projectDetails.setDirectorName("MCC. Juan Carlos Perez Arriaga");
        projectDetails.setCodirectorName("Dr. Hector Xavier Limon Riaño");
        projectDetails.setStudentsParticipating(1);
        projectDetails.setInvestigationProjectDescription("La ciberseguridad se ha vuelto un " +
                "aspecto muy relevante debido " +
                "al alto índice de brechas de seguridad reportadas en productos de software. " +
                "En recientes años el término" +
                " \"shift left security\" ha cobrado importancia, ya que pretende la incorporación " +
                "de prácticas de seguridad" +
                " en el desarrollo de software en etapas tempranas del proceso. Actualmente existen " +
                "algunos retos derivados " +
                "de considerar la seguridad en etapas tempranas en el proceso de desarrollo, " +
                "entre dichos retos destacan:" +
                " conocimiento de fallas de seguridad comunes, mejora de los procesos de " +
                "colaboración con equipos de seguridad," +
                " diseminación de actividades enfocadas a la higiene del código para prevenir " +
                "algún defecto que comprometa la" +
                " seguridad del producto, entre otros aspectos. El considerar actividades " +
                "de seguridad en el proceso de desarrollo" +
                " de software permite que se desarrollen productos menos propensos " +
                "a vulnerabilidades, propician que los " +
                "programadores generen conocimiento a partir de la identificación " +
                "de fallas conocidas, consolidad una cultura" +
                " de higiene de código, minimizar los costos asociados a fallas que pudiera detectarse a tiempo.");
        projectDetails.setReceptionWorkDescription("El presente trabajo tiene como " +
                "finalidad, realizar un mapeo sistemático" +
                " de la literatura sobre las prácticas de ciberseguridad identificadas " +
                "en el proceso de desarrollo de software," +
                " así como reportar elementos como: tipo de práctica, fase en la que se " +
                "lleva a cabo, evidencia de su utilidad," +
                " entre otros aspectos.");
        projectDetails.setExpectedResults("Documento que contenga:\n" +
                "- Reporte de la revisión sistemática de la literatura\n" +
                "- Artículo para publicación en evento académico");
        projectDetails.setRecommendedBibliography("J. Straub, \"Software Engineering: The First Line of Defense for " +
                "Cybersecurity,\" 2020 IEEE 11th International Conference on " +
                "Software Engineering and Service Science " +
                "(ICSESS), 2020, pp. 1-5, doi: 10.1109/ICSESS49938.2020.9237715.\n" +
                "\n" +
                "Johnson, C. (2012). CyberSafety: CyberSecurity and " +
                "Safety-Critical Software Engineering. In: Dale, C., " +
                "Anderson, T. (eds) Achieving Systems Safety. Springer, " +
                "London. https://doi.org/10.1007/978-1-4471-2494-8_8\n" +
                "\n" +
                "Maurice Dawson, Pedro Taveras, Danielle Taylor, Applying Software " +
                "Assurance and Cybersecurity NICE Job " +
                "Tasks through Secure Software Engineering Labs, Procedia Computer " +
                "Science, Volume 164, 2019, Pages 301-312," +
                " ISSN 1877-0509, https://doi.org/10.1016/j.procs.2019.12.187.\n" +
                "\n" +
                "H. Gonzalez, R. Llamas-Contreras and O. Montaño-Rivas, \"When " +
                "Software Engineering meets Cybersecurity " +
                "at the classroom,\" 2019 7th International Conference in " +
                "Software Engineering Research and Innovation " +
                "(CONISOFT), 2019, pp. 49-54, doi: 10.1109/CONISOFT.2019.00017.\n" +
                "\n" +
                "Frederico Araujo and Teryl Taylor. 2020. Improving cybersecurity " +
                "hygiene through JIT patching. In Proceedings" +
                " of the 28th ACM Joint Meeting on European Software Engineering " +
                "Conference and Symposium on the Foundations" +
                " of Software Engineering (ESEC/FSE 2020). Association for " +
                "Computing Machinery, New York, NY, USA, 1421–1432." +
                " https://doi.org/10.1145/3368089.3417056.");
        projectDAO.addProject(projectDetails);
        projectDAO.setDirectorIDtoProject(projectDetails);
        projectDAO.setCodirectorIDtoProject(projectDetails);
        
        var projectCollaboration = new Project();
        projectCollaboration.setAcademicBodyId("UV-CA-127");
        projectCollaboration.setInvestigationProjectName(" ");
        projectCollaboration.setLGAC_Id(1);
        projectCollaboration.setInvestigationLine(
                "Estudio de los diversos métodos y enfoques para la gestión, modelado " +
                "y desarrollo de software, de manera que se obtenga software de calidad. " +
                        "Gestión de las diversas etapas " +
                "del proceso de desarrollo.");
        projectCollaboration.setApproximateDuration("6 meses");
        projectCollaboration.setModalityId(3);
        projectCollaboration.setReceptionWorkName("Diversidad en equipos de desarrollo y " +
                "su relación con la calidad de software");
        projectCollaboration.setRequisites("Tecnologías para la construcción de software, " +
                "Principios de Construcción de " +
                "Software, Diseño de Software, Procesos de Software, Administración de proyectos");
        projectCollaboration.setDirectorName("MCC. Juan Carlos Perez Arriaga");
        projectCollaboration.setCodirectorName("Dr. Angel Juan Sanchez Garcia");
        projectCollaboration.setStudentsParticipating(1);
        projectCollaboration.setInvestigationProjectDescription("En la medida que el software " +
                "cobra mayor relevancia en " +
                "las actividades diarias de una sociedad, el interés por tener productos de software " +
                "confiables se incrementa." +
                " Algunos estudios muestran una alta prevalencia de problemas de calidad en los " +
                "productos de software que " +
                "se desarrollan y la relación de estos problemas con la experiencia y " +
                "capacitación de programadores. Por " +
                "otra parte, ha habido un avance considerable en el diseño de estrategias " +
                "para mejora de la calidad del " +
                "software, sin embargo, en algunas organizaciones existe resistencia en la " +
                "adopción de prácticas orientadas" +
                " a la incorporación de dichas estrategias. Por otra parte, el aspecto humano " +
                "juega un papel fundamental " +
                "en el desarrollo de productos de software de calidad. Existen estudios donde " +
                "se definen algunas hipótesis" +
                " orientadas a validar si aspectos de diversidad de personalidad están " +
                "relacionadas positivamente en la " +
                "calidad del producto final.");
        projectCollaboration.setReceptionWorkDescription("El presente trabajo tiene como " +
                "finalidad realizar una revisión" +
                " sistemática de la literatura sobre la diversidad (personalidad, género, " +
                "entre otros aspectos) de los" +
                " equipos de desarrollo y la relación que guardan con la calidad de los " +
                "productos de software que desarrollan." +
                " Adicionalmente, se pretende que la revisión sistemática documente el " +
                "impacto que tienen los hallazgos " +
                "identificados en la revisión en los productos de software.");
        projectCollaboration.setExpectedResults("Documento que contenga:\n" +
                "- Reporte de la revisión sistemática de la literatura\n" +
                "- Artículo para publicación en evento académico");
        projectCollaboration.setRecommendedBibliography("V. Pieterse, M. Leeu, and M. van Eekelen, " +
                "\"How personality " +
                "diversity influences team performance in student software engineering teams,\" " +
                "2018 Conference on " +
                "Information Communications Technology and Society (ICTAS), 2018, pp. 1-6, doi: " +
                "10.1109/ICTAS.2018.8368749.\n" +
                "\n" +
                "Meiyappan Nagappan, Thomas Zimmermann, and Christian Bird. 2013. Diversity in software engineering " +
                "research. In Proceedings of the 2013 9th Joint Meeting on Foundations of " +
                "Software Engineering (ESEC/FSE 2013)." +
                " Association for Computing Machinery, New York, NY, USA, 466–476. " +
                "https://doi.org/10.1145/2491411.2491415\n" +
                "\n" +
                "Luiz Fernando Capretz and Faheem Ahmed. 2010. Why do we need " +
                "personality diversity in software engineering?" +
                " SIGSOFT Softw. Eng. Notes 35, 2 (March 2010), 1–11. https://doi.org/10.1145/1734103.1734111\n" +
                "\n" +
                "G. Catolino, F. Palomba, D. A. Tamburri, A. Serebrenik, and F. Ferrucci, " +
                "\"Gender Diversity and Women " +
                "in Software Teams: How Do They Affect Community Smells?,\" " +
                "2019 IEEE/ACM 41st International Conference " +
                "on Software Engineering: Software Engineering in Society (ICSE-SEIS), 2019, pp. 11-20, doi: 10.1109/" +
                "ICSE-SEIS.2019.00010.");
        projectDAO.addProject(projectCollaboration);
        projectDAO.setDirectorIDtoProject(projectCollaboration);
        projectDAO.setCodirectorIDtoProject(projectCollaboration);
    }

    @AfterEach
    void tearDown() throws SQLException {
        var userDAO = new UserDAO();
        var projectDAO = new ProjectDAO();
        
        userDAO.deleteUserByUsername("perezJuanC");
        userDAO.deleteUserByUsername("sanchezAngelJ");
        userDAO.deleteUserByUsername("limonHectorX");
        userDAO.deleteUserByUsername("reyesItzelA");
        userDAO.deleteUserByUsername("dominguezIsidroS");
        userDAO.deleteUserByUsername("ocharanJorgeO");
        userDAO.deleteUserByUsername("cortesMariaK");
        userDAO.deleteUserByUsername("alonsoOscarR");
        
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Recomendaciones de Accesibilidad para el " +
                "Desarrollo de Software"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Análisis de técnicas de procesamiento de " +
                "lenguaje natural para la " +
                "ingeniería de requisitos en idioma español."));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Análisis de las tecnologías para el " +
                "desarrollo de Development Bots"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Prácticas de Ciberseguridad en " +
                "Ingeniería de Software"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Diversidad en equipos de " +
                "desarrollo y su relación con la calidad de software"));
        
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Aplicaciones del Análisis " +
                "Clúster en la Ingeniería de Software"));;
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Ejemplo trabajo para Verificar"));
        projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Ejemplo trabajo para Declinar"));
    }

    @Test
    void testAddProject() throws SQLException {
        System.out.println("Test addProject");
        var projectDAO = new ProjectDAO();
        var project = new Project();
        
        project.setAcademicBodyId("UV-CA-127");
        project.setInvestigationProjectName("Ingeniería de Software e Inteligencia Artificial");
        project.setLGAC_Id(1);
        project.setInvestigationLine("Administración de proyectos");
        project.setApproximateDuration("12 meses");
        project.setModalityId(1);
        project.setReceptionWorkName("Aplicaciones del Análisis Clúster en la Ingeniería de Software");
        project.setRequisites("Capacidad de análisis, abstracción, lectura de documentos en inglés, Inteligencia " +
                "Artificial Aplicada a la Ingeniería de Software");
        project.setDirectorName("Dr. Angel Juan Sanchez García");
        project.setCodirectorName("Dr. Oscar Alonso Ramirez");
        project.setStudentsParticipating(1);
        project.setInvestigationProjectDescription("Muchos de los factores que impactan en el tiempo y " +
                "confiabilidad de " +
                "un proyecto de Software, pueden deberse al factor humano que pudieran ser solventados " +
                "desarrollando muchas" +
                " tareas de manera automática y tomando decisiones autónomas, haciendo más " +
                "eficiente la labor de un Ingeniero" +
                " de Software y evitando el sesgo del humano. Por otro lado, la Inteligencia " +
                "Artificial carece hoy en día de " +
                "metodologías de desarrollo de software que permitan la construcción de " +
                "sistemas de cómputo de calidad, y que " +
                "se adapten al tipo de sistemas que permitan la experimentación e " +
                "investigación en el área de Inteligencia " +
                "Artificial. Por lo tanto, surge la necesidad de colaborar entre " +
                "ambas disciplinas para fortalecer los " +
                "resultados de ambas áreas de investigación, aportando las fortalezas de cada una en la otra. \n" +
                "\n" +
                "Es por ello que el presente proyecto busca desarrollar colaboración " +
                "entre la Ingeniería de Software y la" +
                " Inteligencia Artificial, para contribuir al desarrollo de ambas disciplinas, " +
                "mediante la aplicación de " +
                "técnicas de Inteligencia Artificial que aporten soluciones a problemas de " +
                "procesos y del producto de software," +
                " así como la aplicación de estrategias, métodos y procesos que soporten a la " +
                "investigación, desarrollo, y " +
                "experimentación en el ámbito de la inteligencia Artificial.");
        project.setReceptionWorkDescription("La inteligencia Artificial es una disciplina que " +
                "recientemente está colaborando" +
                " con la Ingeniería de Software. Entre algunas técnicas que aporta la " +
                "Inteligencia Artificial, se encuentran" +
                " las que tienen que ver con el aprendizaje automático (supervisado " +
                "y no supervisado). Dentro de las técnicas" +
                " de aprendizaje no supervisado, se encuentra el análisis clúster " +
                "(agrupación). Los enfoques de agrupación de" +
                " software pueden ayudar con la tarea de comprender sistemas de " +
                "software grandes y complejos al descomponer los" +
                " automáticamente en subsistemas más pequeños y fáciles de administrar " +
                "(Reflexion Analysis, Software Evolution, " +
                "Information Recovery).\n" +
                "Tomando en consideración lo anteriormente mencionado, el objetivo de " +
                "este trabajo monográfico, es realizar una" +
                " presentación sucinta del estado actual de aplicaciones del aprendizaje " +
                "no supervisado, específicamente de las" +
                " técnicas de análisis clúster, en la Ingeniería de Software para " +
                "explorar posibles aportaciones en el área.");
        project.setExpectedResults("Revisión Sistemática de la Literatura\n" +
                "Trabajo de Monografía\n" +
                "Un borrador de artículo\n");
        project.setRecommendedBibliography("Russel, S., Norving, P. (2009). Artificial Intelligence: " +
                "A modern Aproach, 3rd edition, Pearson.\n" +
                "Ponce, P.. (2010). Inteligencia Artificial con aplicaciones en la Ingeniería, Alfaomega, 2010.\n" +
                "Maqbool, O., & Babri, H. (2007). " +
                "Hierarchical clustering for software architecture recovery. IEEE Transactions" +
                " on Software Engineering, 33(11), 759-780.\n" +
                "Patel, C., Hamou-Lhadj, A., & Rilling, J. (2009, March). Software clustering using " +
                "dynamic analysis and" +
                " static dependencies. In 2009 13th European Conference on Software Maintenance and " +
                "Reengineering (pp. 27-36). IEEE.\n" +
                "Bittencourt, R. A., & Guerrero, D. D. S. (2009, March). Comparison of graph " +
                "clustering algorithms for " +
                "recovering software architecture module views. In 2009 13th European Conference " +
                "on Software Maintenance" +
                " and Reengineering (pp. 251-254). IEEE.\n" +
                "Zhang, Q., Qiu, D., Tian, Q., & Sun, L. (2010, August). Object-oriented software " +
                "architecture recovery " +
                "using a new hybrid clustering algorithm. In 2010 Seventh International Conference " +
                "on Fuzzy Systems and " +
                "Knowledge Discovery (Vol. 6, pp. 2546-2550). IEEE.\n" +
                "Khan, Q., Akram, U., Butt, W. H., & Rehman, S. (2016, December). Implementation " +
                "and evaluation of optimized" +
                " algorithm for software architectures analysis " +
                "through unsupervised learning (clustering). In 2016 17th " +
                "International Conference on Sciences and " +
                "Techniques of Automatic Control and Computer Engineering (STA) " +
                "(pp. 266-276). IEEE.");
        
        int expectedResult = 1;
        int actualResult = projectDAO.addProject(project);
        
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    void testSetDirectorIDtoProject() throws SQLException {
        System.out.println("Test setDirectorIDtoProject");
        var projectDAO = new ProjectDAO();
        
        var projectDirectorSet = new Project();
        projectDirectorSet.setAcademicBodyId("UV-CA-127");
        projectDirectorSet.setInvestigationProjectName("Ingeniería de Software e Inteligencia Artificial");
        projectDirectorSet.setLGAC_Id(1);
        projectDirectorSet.setInvestigationLine("Administración de proyectos");
        projectDirectorSet.setApproximateDuration("12 meses");
        projectDirectorSet.setModalityId(1);
        projectDirectorSet.setReceptionWorkName("Aplicaciones del Análisis Clúster en la Ingeniería de Software");
        projectDirectorSet.setRequisites("Capacidad de análisis, abstracción, lectura de documentos en inglés, " +
                "Inteligencia Artificial Aplicada a la Ingeniería de Software");
        projectDirectorSet.setDirectorName("Dr. Angel Juan Sanchez García");
        projectDirectorSet.setCodirectorName("Dr. Oscar Alonso Ramirez");
        projectDirectorSet.setStudentsParticipating(1);
        projectDirectorSet.setInvestigationProjectDescription("Muchos de los factores que impactan en el " +
                "tiempo y confiabilidad de " +
                "un proyecto de Software, pueden deberse al factor humano que pudieran ser solventados " +
                "desarrollando muchas" +
                " tareas de manera automática y tomando decisiones autónomas, haciendo más eficiente " +
                "la labor de un Ingeniero" +
                " de Software y evitando el sesgo del humano. Por otro lado, la Inteligencia " +
                "Artificial carece hoy en día de " +
                "metodologías de desarrollo de software que permitan la construcción de " +
                "sistemas de cómputo de calidad, y que " +
                "se adapten al tipo de sistemas que permitan la experimentación e " +
                "investigación en el área de Inteligencia " +
                "Artificial. Por lo tanto, surge la necesidad de colaborar entre " +
                "ambas disciplinas para fortalecer los " +
                "resultados de ambas áreas de investigación, aportando las fortalezas de cada una en la otra. \n" +
                "\n" +
                "Es por ello que el presente proyecto busca desarrollar colaboración " +
                "entre la Ingeniería de Software y la" +
                " Inteligencia Artificial, para contribuir al desarrollo de ambas " +
                "disciplinas, mediante la aplicación de " +
                "técnicas de Inteligencia Artificial que aporten soluciones a " +
                "problemas de procesos y del producto de software," +
                " así como la aplicación de estrategias, métodos y procesos que " +
                "soporten a la investigación, desarrollo, y " +
                "experimentación en el ámbito de la inteligencia Artificial.");
        projectDirectorSet.setReceptionWorkDescription("La inteligencia Artificial es una disciplina " +
                "que recientemente está colaborando" +
                " con la Ingeniería de Software. Entre algunas técnicas que aporta la Inteligencia " +
                "Artificial, se encuentran" +
                " las que tienen que ver con el aprendizaje automático (supervisado y no supervisado). " +
                "Dentro de las técnicas" +
                " de aprendizaje no supervisado, se encuentra el análisis clúster (agrupación). " +
                "Los enfoques de agrupación de" +
                " software pueden ayudar con la tarea de comprender sistemas de software " +
                "grandes y complejos al descomponer los" +
                " automáticamente en subsistemas más pequeños y fáciles de administrar " +
                "(Reflexion Analysis, Software Evolution, " +
                "Information Recovery).\n" +
                "Tomando en consideración lo anteriormente mencionado, el objetivo de este trabajo " +
                "monográfico, es realizar una" +
                " presentación sucinta del estado actual de aplicaciones del aprendizaje no " +
                "supervisado, específicamente de las" +
                " técnicas de análisis clúster, en la Ingeniería de Software para explorar " +
                "posibles aportaciones en el área.");
        projectDirectorSet.setExpectedResults("Revisión Sistemática de la Literatura\n" +
                "Trabajo de Monografía\n" +
                "Un borrador de artículo\n");
        projectDirectorSet.setRecommendedBibliography("Russel, S., Norving, P. (2009). " +
                "Artificial Intelligence: A modern Aproach, 3rd edition, Pearson.\n" +
                "Ponce, P.. (2010). Inteligencia Artificial con aplicaciones en la Ingeniería, Alfaomega, 2010.\n" +
                "Maqbool, O., & Babri, H. (2007). Hierarchical clustering for " +
                "software architecture recovery. IEEE Transactions" +
                " on Software Engineering, 33(11), 759-780.\n" +
                "Patel, C., Hamou-Lhadj, A., & Rilling, J. (2009, March). " +
                "Software clustering using dynamic analysis and" +
                " static dependencies. In 2009 13th European Conference on " +
                "Software Maintenance and Reengineering (pp. 27-36). IEEE.\n" +
                "Bittencourt, R. A., & Guerrero, D. D. S. (2009, March). " +
                "Comparison of graph clustering algorithms for " +
                "recovering software architecture module views. In 2009 13th " +
                "European Conference on Software Maintenance" +
                " and Reengineering (pp. 251-254). IEEE.\n" +
                "Zhang, Q., Qiu, D., Tian, Q., & Sun, L. (2010, August). " +
                "Object-oriented software architecture recovery " +
                "using a new hybrid clustering algorithm. In 2010 Seventh " +
                "International Conference on Fuzzy Systems and " +
                "Knowledge Discovery (Vol. 6, pp. 2546-2550). IEEE.\n" +
                "Khan, Q., Akram, U., Butt, W. H., & Rehman, S. (2016, December). Implementation and evaluation of optimized" +
                " algorithm for software architectures analysis through unsupervised learning (clustering). In 2016 17th " +
                "International Conference on Sciences and Techniques of Automatic Control and Computer Engineering (STA) " +
                "(pp. 266-276). IEEE.");
        projectDAO.addProject(projectDirectorSet);
        
        int expectedResult = 1;
        int actualResult = projectDAO.setDirectorIDtoProject(projectDirectorSet);
        assertEquals(expectedResult,actualResult);
    }
    
    @Test
    void testSetCodirectorIDtoProject() throws SQLException {
        System.out.println("Test setCodirectorIDtoProject");
        var projectDAO = new ProjectDAO();
        
        var projectCodirectorSet = new Project();
        projectCodirectorSet.setAcademicBodyId("UV-CA-127");
        projectCodirectorSet.setInvestigationProjectName("Ingeniería de Software e Inteligencia Artificial");
        projectCodirectorSet.setLGAC_Id(1);
        projectCodirectorSet.setInvestigationLine("Administración de proyectos");
        projectCodirectorSet.setApproximateDuration("12 meses");
        projectCodirectorSet.setModalityId(1);
        projectCodirectorSet.setReceptionWorkName("Aplicaciones del Análisis Clúster en la Ingeniería de Software");
        projectCodirectorSet.setRequisites("Capacidad de análisis, abstracción, lectura de documentos en inglés, Inteligencia Artificial Aplicada a la Ingeniería de Software");
        projectCodirectorSet.setDirectorName("Dr. Angel Juan Sanchez García");
        projectCodirectorSet.setCodirectorName("Dr. Oscar Alonso Ramirez");
        projectCodirectorSet.setStudentsParticipating(1);
        projectCodirectorSet.setInvestigationProjectDescription("Muchos de los factores que impactan en el tiempo y confiabilidad de " +
                "un proyecto de Software, pueden deberse al factor humano que pudieran ser solventados desarrollando muchas" +
                " tareas de manera automática y tomando decisiones autónomas, haciendo más eficiente la labor de un Ingeniero" +
                " de Software y evitando el sesgo del humano. Por otro lado, la Inteligencia Artificial carece hoy en día de " +
                "metodologías de desarrollo de software que permitan la construcción de sistemas de cómputo de calidad, y que " +
                "se adapten al tipo de sistemas que permitan la experimentación e investigación en el área de Inteligencia " +
                "Artificial. Por lo tanto, surge la necesidad de colaborar entre ambas disciplinas para fortalecer los " +
                "resultados de ambas áreas de investigación, aportando las fortalezas de cada una en la otra. \n" +
                "\n" +
                "Es por ello que el presente proyecto busca desarrollar colaboración entre la Ingeniería de Software y la" +
                " Inteligencia Artificial, para contribuir al desarrollo de ambas disciplinas, mediante la aplicación de " +
                "técnicas de Inteligencia Artificial que aporten soluciones a problemas de procesos y del producto de software," +
                " así como la aplicación de estrategias, métodos y procesos que soporten a la investigación, desarrollo, y " +
                "experimentación en el ámbito de la inteligencia Artificial.");
        projectCodirectorSet.setReceptionWorkDescription("La inteligencia Artificial es una disciplina que recientemente está colaborando" +
                " con la Ingeniería de Software. Entre algunas técnicas que aporta la Inteligencia Artificial, se encuentran" +
                " las que tienen que ver con el aprendizaje automático (supervisado y no supervisado). Dentro de las técnicas" +
                " de aprendizaje no supervisado, se encuentra el análisis clúster (agrupación). Los enfoques de agrupación de" +
                " software pueden ayudar con la tarea de comprender sistemas de software grandes y complejos al descomponer los" +
                " automáticamente en subsistemas más pequeños y fáciles de administrar (Reflexion Analysis, Software Evolution, " +
                "Information Recovery).\n" +
                "Tomando en consideración lo anteriormente mencionado, el objetivo de este trabajo monográfico, es realizar una" +
                " presentación sucinta del estado actual de aplicaciones del aprendizaje no supervisado, específicamente de las" +
                " técnicas de análisis clúster, en la Ingeniería de Software para explorar posibles aportaciones en el área.");
        projectCodirectorSet.setExpectedResults("Revisión Sistemática de la Literatura\n" +
                "Trabajo de Monografía\n" +
                "Un borrador de artículo\n");
        projectCodirectorSet.setRecommendedBibliography("Russel, S., Norving, P. (2009). Artificial Intelligence: A modern Aproach, 3rd edition, Pearson.\n" +
                "Ponce, P.. (2010). Inteligencia Artificial con aplicaciones en la Ingeniería, Alfaomega, 2010.\n" +
                "Maqbool, O., & Babri, H. (2007). Hierarchical clustering for software architecture recovery. IEEE Transactions" +
                " on Software Engineering, 33(11), 759-780.\n" +
                "Patel, C., Hamou-Lhadj, A., & Rilling, J. (2009, March). Software clustering using dynamic analysis and" +
                " static dependencies. In 2009 13th European Conference on Software Maintenance and Reengineering (pp. 27-36). IEEE.\n" +
                "Bittencourt, R. A., & Guerrero, D. D. S. (2009, March). Comparison of graph clustering algorithms for " +
                "recovering software architecture module views. In 2009 13th European Conference on Software Maintenance" +
                " and Reengineering (pp. 251-254). IEEE.\n" +
                "Zhang, Q., Qiu, D., Tian, Q., & Sun, L. (2010, August). Object-oriented software architecture recovery " +
                "using a new hybrid clustering algorithm. In 2010 Seventh International Conference on Fuzzy Systems and " +
                "Knowledge Discovery (Vol. 6, pp. 2546-2550). IEEE.\n" +
                "Khan, Q., Akram, U., Butt, W. H., & Rehman, S. (2016, December). Implementation and evaluation of optimized" +
                " algorithm for software architectures analysis through unsupervised learning (clustering). In 2016 17th " +
                "International Conference on Sciences and Techniques of Automatic Control and Computer Engineering (STA) " +
                "(pp. 266-276). IEEE.");
        projectDAO.addProject(projectCodirectorSet);
        
        int expectedResult = 1;
        int actualResult = projectDAO.setCodirectorIDtoProject(projectCodirectorSet);
        assertEquals(expectedResult,actualResult);
    }
    
    @Test
    void testUpdateProjectStateToVerified() throws SQLException {
        System.out.println("Test updateProjectState");
        var projectDAO = new ProjectDAO();
        
        var projectToVerify = new Project();
        projectToVerify.setAcademicBodyId("UV-CA-127");
        projectToVerify.setInvestigationProjectName("Ingeniería de Software e Inteligencia Artificial");
        projectToVerify.setLGAC_Id(1);
        projectToVerify.setInvestigationLine("Administración de proyectos");
        projectToVerify.setApproximateDuration("12 meses");
        projectToVerify.setModalityId(1);
        projectToVerify.setReceptionWorkName("Aplicaciones del Análisis Clúster en la Ingeniería de Software");
        projectToVerify.setRequisites("Capacidad de análisis, abstracción, lectura de documentos en inglés, Inteligencia Artificial Aplicada a la Ingeniería de Software");
        projectToVerify.setDirectorName("Dr. Angel Juan Sanchez García");
        projectToVerify.setCodirectorName("Dr. Oscar Alonso Ramirez");
        projectToVerify.setStudentsParticipating(1);
        projectToVerify.setInvestigationProjectDescription("Muchos de los factores que impactan en el tiempo y confiabilidad de " +
                "un proyecto de Software, pueden deberse al factor humano que pudieran ser solventados desarrollando muchas" +
                " tareas de manera automática y tomando decisiones autónomas, haciendo más eficiente la labor de un Ingeniero" +
                " de Software y evitando el sesgo del humano. Por otro lado, la Inteligencia Artificial carece hoy en día de " +
                "metodologías de desarrollo de software que permitan la construcción de sistemas de cómputo de calidad, y que " +
                "se adapten al tipo de sistemas que permitan la experimentación e investigación en el área de Inteligencia " +
                "Artificial. Por lo tanto, surge la necesidad de colaborar entre ambas disciplinas para fortalecer los " +
                "resultados de ambas áreas de investigación, aportando las fortalezas de cada una en la otra. \n" +
                "\n" +
                "Es por ello que el presente proyecto busca desarrollar colaboración entre la Ingeniería de Software y la" +
                " Inteligencia Artificial, para contribuir al desarrollo de ambas disciplinas, mediante la aplicación de " +
                "técnicas de Inteligencia Artificial que aporten soluciones a problemas de procesos y del producto de software," +
                " así como la aplicación de estrategias, métodos y procesos que soporten a la investigación, desarrollo, y " +
                "experimentación en el ámbito de la inteligencia Artificial.");
        projectToVerify.setReceptionWorkDescription("La inteligencia Artificial es una disciplina que recientemente está colaborando" +
                " con la Ingeniería de Software. Entre algunas técnicas que aporta la Inteligencia Artificial, se encuentran" +
                " las que tienen que ver con el aprendizaje automático (supervisado y no supervisado). Dentro de las técnicas" +
                " de aprendizaje no supervisado, se encuentra el análisis clúster (agrupación). Los enfoques de agrupación de" +
                " software pueden ayudar con la tarea de comprender sistemas de software grandes y complejos al descomponer los" +
                " automáticamente en subsistemas más pequeños y fáciles de administrar (Reflexion Analysis, Software Evolution, " +
                "Information Recovery).\n" +
                "Tomando en consideración lo anteriormente mencionado, el objetivo de este trabajo monográfico, es realizar una" +
                " presentación sucinta del estado actual de aplicaciones del aprendizaje no supervisado, específicamente de las" +
                " técnicas de análisis clúster, en la Ingeniería de Software para explorar posibles aportaciones en el área.");
        projectToVerify.setExpectedResults("Revisión Sistemática de la Literatura\n" +
                "Trabajo de Monografía\n" +
                "Un borrador de artículo\n");
        projectToVerify.setRecommendedBibliography("Russel, S., Norving, P. (2009). Artificial Intelligence: A modern Aproach, 3rd edition, Pearson.\n" +
                "Ponce, P.. (2010). Inteligencia Artificial con aplicaciones en la Ingeniería, Alfaomega, 2010.\n" +
                "Maqbool, O., & Babri, H. (2007). Hierarchical clustering for software architecture recovery. IEEE Transactions" +
                " on Software Engineering, 33(11), 759-780.\n" +
                "Patel, C., Hamou-Lhadj, A., & Rilling, J. (2009, March). Software clustering using dynamic analysis and" +
                " static dependencies. In 2009 13th European Conference on Software Maintenance and Reengineering (pp. 27-36). IEEE.\n" +
                "Bittencourt, R. A., & Guerrero, D. D. S. (2009, March). Comparison of graph clustering algorithms for " +
                "recovering software architecture module views. In 2009 13th European Conference on Software Maintenance" +
                " and Reengineering (pp. 251-254). IEEE.\n" +
                "Zhang, Q., Qiu, D., Tian, Q., & Sun, L. (2010, August). Object-oriented software architecture recovery " +
                "using a new hybrid clustering algorithm. In 2010 Seventh International Conference on Fuzzy Systems and " +
                "Knowledge Discovery (Vol. 6, pp. 2546-2550). IEEE.\n" +
                "Khan, Q., Akram, U., Butt, W. H., & Rehman, S. (2016, December). Implementation and evaluation of optimized" +
                " algorithm for software architectures analysis through unsupervised learning (clustering). In 2016 17th " +
                "International Conference on Sciences and Techniques of Automatic Control and Computer Engineering (STA) " +
                "(pp. 266-276). IEEE.");
        projectDAO.addProject(projectToVerify);
        
        int expectedResult = 1;
        int actualResult = projectDAO.updateProjectState(projectDAO.getProjectIDByTitle("Aplicaciones del Análisis Clúster en la Ingeniería de Software"),"Verificado");
        
        assertEquals(expectedResult,actualResult);
    }
    
    @Test
    void testUpdateProjectStateToDeclined() throws SQLException {
        System.out.println("Test updateProjectState");
        var projectDAO = new ProjectDAO();
        
        var projectToDecline = new Project();
        projectToDecline.setAcademicBodyId("UV-CA-127");
        projectToDecline.setInvestigationProjectName("Ingeniería de Software e Inteligencia Artificial");
        projectToDecline.setLGAC_Id(1);
        projectToDecline.setInvestigationLine("Administración de proyectos");
        projectToDecline.setApproximateDuration("12 meses");
        projectToDecline.setModalityId(1);
        projectToDecline.setReceptionWorkName("Aplicaciones del Análisis Clúster en la Ingeniería de Software");
        projectToDecline.setRequisites("Capacidad de análisis, abstracción, lectura de documentos en inglés, Inteligencia Artificial Aplicada a la Ingeniería de Software");
        projectToDecline.setDirectorName("Dr. Angel Juan Sanchez García");
        projectToDecline.setCodirectorName("Dr. Oscar Alonso Ramirez");
        projectToDecline.setStudentsParticipating(1);
        projectToDecline.setInvestigationProjectDescription("Muchos de los factores que impactan en el tiempo y confiabilidad de " +
                "un proyecto de Software, pueden deberse al factor humano que pudieran ser solventados desarrollando muchas" +
                " tareas de manera automática y tomando decisiones autónomas, haciendo más eficiente la labor de un Ingeniero" +
                " de Software y evitando el sesgo del humano. Por otro lado, la Inteligencia Artificial carece hoy en día de " +
                "metodologías de desarrollo de software que permitan la construcción de sistemas de cómputo de calidad, y que " +
                "se adapten al tipo de sistemas que permitan la experimentación e investigación en el área de Inteligencia " +
                "Artificial. Por lo tanto, surge la necesidad de colaborar entre ambas disciplinas para fortalecer los " +
                "resultados de ambas áreas de investigación, aportando las fortalezas de cada una en la otra. \n" +
                "\n" +
                "Es por ello que el presente proyecto busca desarrollar colaboración entre la Ingeniería de Software y la" +
                " Inteligencia Artificial, para contribuir al desarrollo de ambas disciplinas, mediante la aplicación de " +
                "técnicas de Inteligencia Artificial que aporten soluciones a problemas de procesos y del producto de software," +
                " así como la aplicación de estrategias, métodos y procesos que soporten a la investigación, desarrollo, y " +
                "experimentación en el ámbito de la inteligencia Artificial.");
        projectToDecline.setReceptionWorkDescription("La inteligencia Artificial es una disciplina que recientemente está colaborando" +
                " con la Ingeniería de Software. Entre algunas técnicas que aporta la Inteligencia Artificial, se encuentran" +
                " las que tienen que ver con el aprendizaje automático (supervisado y no supervisado). Dentro de las técnicas" +
                " de aprendizaje no supervisado, se encuentra el análisis clúster (agrupación). Los enfoques de agrupación de" +
                " software pueden ayudar con la tarea de comprender sistemas de software grandes y complejos al descomponer los" +
                " automáticamente en subsistemas más pequeños y fáciles de administrar (Reflexion Analysis, Software Evolution, " +
                "Information Recovery).\n" +
                "Tomando en consideración lo anteriormente mencionado, el objetivo de este trabajo monográfico, es realizar una" +
                " presentación sucinta del estado actual de aplicaciones del aprendizaje no supervisado, específicamente de las" +
                " técnicas de análisis clúster, en la Ingeniería de Software para explorar posibles aportaciones en el área.");
        projectToDecline.setExpectedResults("Revisión Sistemática de la Literatura\n" +
                "Trabajo de Monografía\n" +
                "Un borrador de artículo\n");
        projectToDecline.setRecommendedBibliography("Russel, S., Norving, P. (2009). Artificial Intelligence: A modern Aproach, 3rd edition, Pearson.\n" +
                "Ponce, P.. (2010). Inteligencia Artificial con aplicaciones en la Ingeniería, Alfaomega, 2010.\n" +
                "Maqbool, O., & Babri, H. (2007). Hierarchical clustering for software architecture recovery. IEEE Transactions" +
                " on Software Engineering, 33(11), 759-780.\n" +
                "Patel, C., Hamou-Lhadj, A., & Rilling, J. (2009, March). Software clustering using dynamic analysis and" +
                " static dependencies. In 2009 13th European Conference on Software Maintenance and Reengineering (pp. 27-36). IEEE.\n" +
                "Bittencourt, R. A., & Guerrero, D. D. S. (2009, March). Comparison of graph clustering algorithms for " +
                "recovering software architecture module views. In 2009 13th European Conference on Software Maintenance" +
                " and Reengineering (pp. 251-254). IEEE.\n" +
                "Zhang, Q., Qiu, D., Tian, Q., & Sun, L. (2010, August). Object-oriented software architecture recovery " +
                "using a new hybrid clustering algorithm. In 2010 Seventh International Conference on Fuzzy Systems and " +
                "Knowledge Discovery (Vol. 6, pp. 2546-2550). IEEE.\n" +
                "Khan, Q., Akram, U., Butt, W. H., & Rehman, S. (2016, December). Implementation and evaluation of optimized" +
                " algorithm for software architectures analysis through unsupervised learning (clustering). In 2016 17th " +
                "International Conference on Sciences and Techniques of Automatic Control and Computer Engineering (STA) " +
                "(pp. 266-276). IEEE.");
        projectDAO.addProject(projectToDecline);
        
        int expectedResult = 1;
        int actualResult = projectDAO.updateProjectState(projectDAO.getProjectIDByTitle("Aplicaciones del Análisis Clúster en la Ingeniería de Software"),"Declinado");
        
        assertEquals(expectedResult,actualResult);
    }

    @Test
    void testGetProjectsByStateVerified() throws SQLException {
        System.out.println("Test getProjectsByState with projectState parameter = 'Verificado'");
        var projectDAO = new ProjectDAO();
        
        var expectedList = new ArrayList<>();
        var simpleVerifiedProject = new SimpleProject();
        simpleVerifiedProject.setProjectID(projectDAO.getProjectIDByTitle("Análisis de técnicas de procesamiento de lenguaje natural para la " +
                "ingeniería de requisitos en idioma español."));
        simpleVerifiedProject.setReceptionWorkName("Análisis de técnicas de procesamiento de lenguaje natural para la " +
                "ingeniería de requisitos en idioma español.");
        simpleVerifiedProject.setProjectState("Verificado");
        expectedList.add(simpleVerifiedProject);
        
        var actualList = new ArrayList<>(projectDAO.getProjectsByState("Verificado"));
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetProjectsByStateNoDeclinedProjects() throws SQLException {
        System.out.println("Test getProjectsByState with projectState parameter = 'Declinado'");
        var projectDAO = new ProjectDAO();
        
        List<SimpleProject> actualList = new ArrayList<>(projectDAO.getProjectsByState("Declinado"));
        
        System.out.println("Contenido de actualList:");
        for (SimpleProject project : actualList) {
            System.out.println("ID: " + project.getProjectID());
            System.out.println("Título: " + project.getReceptionWorkName());
            System.out.println("Estado: " + project.getProjectState());
            System.out.println("--------------------");
        }
        assertTrue(actualList.isEmpty());
    }
    
    @Test
    void testGetProjectsByStateUnverified() throws SQLException {
        System.out.println("Test getProjectsByState with projectState parameter = 'Por revisar'");
        var projectDAO = new ProjectDAO();
        
        List<SimpleProject> expectedList = new ArrayList<>();
        var simpleUnverifiedProject = new SimpleProject();
        simpleUnverifiedProject.setProjectID(projectDAO.getProjectIDByTitle("Recomendaciones de Accesibilidad para el Desarrollo de Software"));
        simpleUnverifiedProject.setReceptionWorkName("Recomendaciones de Accesibilidad para el Desarrollo de Software");
        simpleUnverifiedProject.setProjectState("Por revisar");
        
        var simpleUnverifiedProject1 = new SimpleProject();
        simpleUnverifiedProject1.setProjectID(projectDAO.getProjectIDByTitle("Análisis de las tecnologías para el desarrollo de Development Bots"));
        simpleUnverifiedProject1.setReceptionWorkName("Análisis de las tecnologías para el desarrollo de Development Bots");
        simpleUnverifiedProject1.setProjectState("Por revisar");
        
        var simpleUnverifiedProject2 = new SimpleProject();
        simpleUnverifiedProject2.setProjectID(projectDAO.getProjectIDByTitle("Prácticas de Ciberseguridad en Ingeniería de Software"));
        simpleUnverifiedProject2.setReceptionWorkName("Prácticas de Ciberseguridad en Ingeniería de Software");
        simpleUnverifiedProject2.setProjectState("Por revisar");
        
        var simpleUnverifiedProject3 = new SimpleProject();
        simpleUnverifiedProject3.setProjectID(projectDAO.getProjectIDByTitle("Diversidad en equipos de desarrollo y su relación con la calidad de software"));
        simpleUnverifiedProject3.setReceptionWorkName("Diversidad en equipos de desarrollo y su relación con la calidad de software");
        simpleUnverifiedProject3.setProjectState("Por revisar");
        
        expectedList.add(simpleUnverifiedProject);
        expectedList.add(simpleUnverifiedProject1);
        expectedList.add(simpleUnverifiedProject2);
        expectedList.add(simpleUnverifiedProject3);
        
        var actualList = new ArrayList<>(projectDAO.getProjectsByState("Por revisar"));
        
        System.out.println("Contenido de expectedList:");
        for (SimpleProject project : expectedList) {
            System.out.println("ID: " + project.getProjectID());
            System.out.println("Título: " + project.getReceptionWorkName());
            System.out.println("Estado: " + project.getProjectState());
            System.out.println("--------------------");
        }
        
        System.out.println("Contenido de actualList:");
        for (SimpleProject project : actualList) {
            System.out.println("ID: " + project.getProjectID());
            System.out.println("Título: " + project.getReceptionWorkName());
            System.out.println("Estado: " + project.getProjectState());
            System.out.println("--------------------");
        }
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetAllProjects() throws SQLException {
        System.out.println("Test getAllProjects");
        var projectDAO = new ProjectDAO();
        List<SimpleProject> expectedList = new ArrayList<>();
        
        var simpleDeclinedProject = new SimpleProject();
        simpleDeclinedProject.setProjectID(projectDAO.getProjectIDByTitle("Recomendaciones de Accesibilidad para el Desarrollo de Software"));
        simpleDeclinedProject.setReceptionWorkName("Recomendaciones de Accesibilidad para el Desarrollo de Software");
        simpleDeclinedProject.setProjectState("Por revisar");
        
        var simpleVerifiedProject = new SimpleProject();
        simpleVerifiedProject.setProjectID(projectDAO.getProjectIDByTitle("Análisis de técnicas de procesamiento de lenguaje natural para la " +
                "ingeniería de requisitos en idioma español."));
        simpleVerifiedProject.setReceptionWorkName("Análisis de técnicas de procesamiento de lenguaje natural para la " +
                "ingeniería de requisitos en idioma español.");
        simpleVerifiedProject.setProjectState("Verificado");
        
        var simpleUnverifiedProject = new SimpleProject();
        simpleUnverifiedProject.setProjectID(projectDAO.getProjectIDByTitle("Análisis de las tecnologías para el desarrollo de Development Bots"));
        simpleUnverifiedProject.setReceptionWorkName("Análisis de las tecnologías para el desarrollo de Development Bots");
        simpleUnverifiedProject.setProjectState("Por revisar");
        
        var simpleProjectDetail = new SimpleProject();
        simpleProjectDetail.setProjectID(projectDAO.getProjectIDByTitle("Prácticas de Ciberseguridad en Ingeniería de Software"));
        simpleProjectDetail.setReceptionWorkName("Prácticas de Ciberseguridad en Ingeniería de Software");
        simpleProjectDetail.setProjectState("Por revisar");
        
        var simpleProjectCollaboration = new SimpleProject();
        simpleProjectCollaboration.setProjectID(projectDAO.getProjectIDByTitle("Diversidad en equipos de desarrollo y su relación con la calidad de software"));
        simpleProjectCollaboration.setReceptionWorkName("Diversidad en equipos de desarrollo y su relación con la calidad de software");
        simpleProjectCollaboration.setProjectState("Por revisar");
        
        expectedList.add(simpleDeclinedProject);
        expectedList.add(simpleVerifiedProject);
        expectedList.add(simpleUnverifiedProject);
        expectedList.add(simpleProjectDetail);
        expectedList.add(simpleProjectCollaboration);
        
        var actualList = new ArrayList<>(projectDAO.getAllProjects());
        
        System.out.println("Contenido de expectedList:");
        for (SimpleProject project : expectedList) {
            System.out.println("ID: " + project.getProjectID());
            System.out.println("Título: " + project.getReceptionWorkName());
            System.out.println("Estado: " + project.getProjectState());
            System.out.println("--------------------");
        }
        
        System.out.println("Contenido de actualList:");
        for (SimpleProject project : actualList) {
            System.out.println("ID: " + project.getProjectID());
            System.out.println("Título: " + project.getReceptionWorkName());
            System.out.println("Estado: " + project.getProjectState());
            System.out.println("--------------------");
        }
        
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetProjectsByParticipation() throws SQLException {
        System.out.println("Test getProjectsByCollaboration");
        var projectDAO = new ProjectDAO();
        var professorDAO = new ProfessorDAO();
        
        List<SimpleProject> expectedList = new ArrayList<>();
        
        var simpleCollaborationProject = new SimpleProject();
        simpleCollaborationProject.setProjectID(projectDAO.getProjectIDByTitle("Recomendaciones de Accesibilidad para el Desarrollo de Software"));
        simpleCollaborationProject.setReceptionWorkName("Recomendaciones de Accesibilidad para el Desarrollo de Software");
        simpleCollaborationProject.setProjectState("Por revisar");
        
        var simpleCollaborationProject2 = new SimpleProject();
        simpleCollaborationProject2.setProjectID(projectDAO.getProjectIDByTitle("Prácticas de Ciberseguridad en Ingeniería de Software"));
        simpleCollaborationProject2.setReceptionWorkName("Prácticas de Ciberseguridad en Ingeniería de Software");
        simpleCollaborationProject2.setProjectState("Por revisar");
        
        var simpleCollaborationProject3 = new SimpleProject();
        simpleCollaborationProject3.setProjectID(projectDAO.getProjectIDByTitle("Diversidad en equipos de desarrollo y su relación con la calidad de software"));
        simpleCollaborationProject3.setReceptionWorkName("Diversidad en equipos de desarrollo y su relación con la calidad de software");
        simpleCollaborationProject3.setProjectState("Por revisar");
        
        expectedList.add(simpleCollaborationProject);
        expectedList.add(simpleCollaborationProject2);
        expectedList.add(simpleCollaborationProject3);
        
        var actualList = new ArrayList<>(projectDAO.getProjectsByParticipation(professorDAO.getProfessorIdByUsername("perezJuanC")));
        
        System.out.println("Contenido de expectedList:");
        for (SimpleProject project : expectedList) {
            System.out.println("ID: " + project.getProjectID());
            System.out.println("Título: " + project.getReceptionWorkName());
            System.out.println("Estado: " + project.getProjectState());
            System.out.println("--------------------");
        }
        
        System.out.println("Contenido de actualList:");
        for (SimpleProject project : actualList) {
            System.out.println("ID: " + project.getProjectID());
            System.out.println("Título: " + project.getReceptionWorkName());
            System.out.println("Estado: " + project.getProjectState());
            System.out.println("--------------------");
        }
        
        assertEquals(expectedList,actualList);
    }
    
    @Test
    void testGetProjectInfoByID() throws SQLException {
        System.out.println("Test getProjectInfoByID");
        var projectDAO = new ProjectDAO();
        
        DetailedProject expectedDetailedProject = new DetailedProject();
        expectedDetailedProject.setProjectID(projectDAO.getProjectIDByTitle("Prácticas de Ciberseguridad en Ingeniería de Software"));
        expectedDetailedProject.setAcademicBodyName("Ingeniería y Tecnología de Software");
        expectedDetailedProject.setInvestigationProjectName(" ");
        expectedDetailedProject.setLgacName("L1. Gestión, modelado y desarrollo de software");
        expectedDetailedProject.setInvestigationLine("Estudio de los diversos métodos y enfoques para la gestión, modelado y " +
                "desarrollo de software, de manera que se obtenga software de calidad. Gestión de las diversas etapas del" +
                " proceso de desarrollo.");
        expectedDetailedProject.setApproxDuration("12 meses");
        expectedDetailedProject.setReceptionWorkModality("Monografía");
        expectedDetailedProject.setReceptionWorkName("Prácticas de Ciberseguridad en Ingeniería de Software");
        expectedDetailedProject.setRequisites("Tecnologías para la construcción de software, Principios de Construcción de Software," +
                " Diseño de Software, Procesos de Software, Administración de proyectos");
        expectedDetailedProject.setDirector("MCC. Juan Carlos Perez Arriaga");
        expectedDetailedProject.setCoDirector("Dr. Hector Xavier Limon Riaño");
        expectedDetailedProject.setNumberStudents(1);
        expectedDetailedProject.setInvestigationDescription("La ciberseguridad se ha vuelto un aspecto muy relevante debido " +
                "al alto índice de brechas de seguridad reportadas en productos de software. En recientes años el término" +
                " \"shift left security\" ha cobrado importancia, ya que pretende la incorporación de prácticas de seguridad" +
                " en el desarrollo de software en etapas tempranas del proceso. Actualmente existen algunos retos derivados " +
                "de considerar la seguridad en etapas tempranas en el proceso de desarrollo, entre dichos retos destacan:" +
                " conocimiento de fallas de seguridad comunes, mejora de los procesos de colaboración con equipos de seguridad," +
                " diseminación de actividades enfocadas a la higiene del código para prevenir algún defecto que comprometa la" +
                " seguridad del producto, entre otros aspectos. El considerar actividades de seguridad en el proceso de desarrollo" +
                " de software permite que se desarrollen productos menos propensos a vulnerabilidades, propician que los " +
                "programadores generen conocimiento a partir de la identificación de fallas conocidas, consolidad una cultura" +
                " de higiene de código, minimizar los costos asociados a fallas que pudiera detectarse a tiempo.");
        expectedDetailedProject.setReceptionWorkDescription("El presente trabajo tiene como finalidad, realizar un mapeo sistemático" +
                " de la literatura sobre las prácticas de ciberseguridad identificadas en el proceso de desarrollo de software," +
                " así como reportar elementos como: tipo de práctica, fase en la que se lleva a cabo, evidencia de su utilidad," +
                " entre otros aspectos.");
        expectedDetailedProject.setExpectedResults("Documento que contenga:\n" +
                "- Reporte de la revisión sistemática de la literatura\n" +
                "- Artículo para publicación en evento académico");
        expectedDetailedProject.setBibliography("J. Straub, \"Software Engineering: The First Line of Defense for " +
                "Cybersecurity,\" 2020 IEEE 11th International Conference on Software Engineering and Service Science " +
                "(ICSESS), 2020, pp. 1-5, doi: 10.1109/ICSESS49938.2020.9237715.\n" +
                "\n" +
                "Johnson, C. (2012). CyberSafety: CyberSecurity and Safety-Critical Software Engineering. In: Dale, C., " +
                "Anderson, T. (eds) Achieving Systems Safety. Springer, London. https://doi.org/10.1007/978-1-4471-2494-8_8\n" +
                "\n" +
                "Maurice Dawson, Pedro Taveras, Danielle Taylor, Applying Software Assurance and Cybersecurity NICE Job " +
                "Tasks through Secure Software Engineering Labs, Procedia Computer Science, Volume 164, 2019, Pages 301-312," +
                " ISSN 1877-0509, https://doi.org/10.1016/j.procs.2019.12.187.\n" +
                "\n" +
                "H. Gonzalez, R. Llamas-Contreras and O. Montaño-Rivas, \"When Software Engineering meets Cybersecurity " +
                "at the classroom,\" 2019 7th International Conference in Software Engineering Research and Innovation " +
                "(CONISOFT), 2019, pp. 49-54, doi: 10.1109/CONISOFT.2019.00017.\n" +
                "\n" +
                "Frederico Araujo and Teryl Taylor. 2020. Improving cybersecurity hygiene through JIT patching. In Proceedings" +
                " of the 28th ACM Joint Meeting on European Software Engineering Conference and Symposium on the Foundations" +
                " of Software Engineering (ESEC/FSE 2020). Association for Computing Machinery, New York, NY, USA, 1421–1432." +
                " https://doi.org/10.1145/3368089.3417056.");
        expectedDetailedProject.setProjectState("Por revisar");
        
        DetailedProject actualDetailedProject = projectDAO.getProjectInfoByID(projectDAO.getProjectIDByTitle("Prácticas de Ciberseguridad en Ingeniería de Software"));
        
        System.out.println("Objeto esperado:");
        System.out.println(expectedDetailedProject);
        
        System.out.println("Objeto actual:");
        System.out.println(expectedDetailedProject);
        
        assertEquals(expectedDetailedProject, actualDetailedProject);
    }
    
    @Test
    void testGetLgacList() throws SQLException {
        System.out.println("Test getLgacList");
        ProjectDAO projectDAO = new ProjectDAO();
        
        List<String> expectedList = new ArrayList<>();
        expectedList.add("L1. Gestión, modelado y desarrollo de software");
        expectedList.add("L2. Tecnologías de software");
        
        List<String> actualList = new ArrayList<>(projectDAO.getLgacList());
        
        assertEquals(expectedList, actualList);
    }
    
    @Test
    void testGetRWModalitiesList() throws SQLException {
        System.out.println("Test getRWModalitiesList");
        ProjectDAO projectDAO = new ProjectDAO();
        
        List<String> expectedList = new ArrayList<>();
        expectedList.add("Monografía");
        expectedList.add("Revisión Multivocal de la Literatura");
        expectedList.add("Revisión Sistemática de la Literatura");
        expectedList.add("Tesis");
        expectedList.add("Trabajo Práctico-Técnico");
        
        List<String> actualList = new ArrayList<>(projectDAO.getRWModalitiesList());
        
        assertEquals(expectedList, actualList);
    }
    
    @Test
    void testGetAcademicBodyIDs() throws SQLException {
        System.out.println("Test getAcademicBodyIDs");
        var projectDAO = new ProjectDAO();
        
        List<String> expectedList = new ArrayList<>();
        expectedList.add("UV-CA-127");
        
        List<String> actualList = new ArrayList<>(projectDAO.getAcademicBodyIDs());
        assertEquals(expectedList, actualList);
    }
    
    @Test
    void testDeleteProjectByID() throws SQLException {
        System.out.println("Test deleteProjectByTitle");
        var projectDAO = new ProjectDAO();
        
        var projectCollaboration = new Project();
        projectCollaboration.setAcademicBodyId("UV-CA-127");
        projectCollaboration.setInvestigationProjectName(" ");
        projectCollaboration.setLGAC_Id(2);
        projectCollaboration.setInvestigationLine(" ");
        projectCollaboration.setApproximateDuration("12 meses");
        projectCollaboration.setModalityId(5);
        projectCollaboration.setReceptionWorkName("Mantenimiento y mejoras de seguridad del Sistema de Evaluación de Código");
        projectCollaboration.setRequisites(" Desarrollo de Sistemas Web, Desarrollo de Aplicaciones,  Programación Segura ");
        projectCollaboration.setDirectorName("Dr. Hector Xavier Limon Riaño");
        projectCollaboration.setCodirectorName("Dr. Angel Juan Sanchez Garcia");
        projectCollaboration.setStudentsParticipating(1);
        projectCollaboration.setInvestigationProjectDescription(" ");
        projectCollaboration.setReceptionWorkDescription("En la facultad de Estadística e Informática, desde el ciclo escolar" +
                " agosto 2015-enero 2016 se utiliza el Sistema de Evaluación de Código como apoyo en diversas experiencias" +
                " educativas de programación. Dicho sistema permite establecer y evaluar de forma automática ejercicios de " +
                "programación presentes en prácticas de clase y exámenes. El sistema ha pasado por diversas versiones, siendo" +
                " mejorado en 2018 como parte del trabajo recepcional \"Sistema de evaluación de prácticas y exámenes de " +
                "experiencias educativas de programación\" de la licenciatura en Ingeniería de Software.  A partir de entonces," +
                " se han realizado diversas mejoras al sistema como parte de una etapa de mantenimiento, sin embargo, están " +
                "pendientes varias mejoras sugeridas por los docentes y alumnos que han utilizado el sistema, así como " +
                "correcciones de errores detectados. De la misma forma, en el año 2021, en el trabajo recepcional \"Ambiente" +
                " para pruebas de penetración de sistemas web\" de la licenciatura en Redes y Servicios de Cómputo, se llevó " +
                "a cabo una evaluación de seguridad del sistema, encontrándose una gran cantidad de problemas de seguridad, " +
                "mismos que se encuentran descritos en un reporte de pruebas de penetración con mitigaciones seguridad asociadas." +
                " El objetivo de este trabajo es continuar con el mantenimiento del sistema de evaluación de código, especialmente" +
                " enfocándose en corregir los problemas de seguridad detectados, y a su vez, analizándose la pertinencia de las" +
                " funcionalidades nuevas sugeridas por maestros y alumnos, así como llevando a cabo las correcciones a los errores" +
                " detectados en el sistema. ");
        projectCollaboration.setExpectedResults("Implementación de mejoras al Sistema de Evaluación de Código Sistema " +
                "desplegado en el clúster de la Facultad de Estadística e Informática Artículo con investigación referente" +
                " al seguimiento de un reporte de pruebas de penetración ");
        projectCollaboration.setRecommendedBibliography("de Jiménez, R. E. L. (2016, November). Pentesting on web applications" +
                " using ethicalhacking. In 2016 IEEE 36th Central American and Panama Convention (CONCAPAN XXXVI) (pp. 1-6). " +
                "IEEE. Mohammed, N. M., Niazi, M., Alshayeb, M., & Mahmood, S. (2017). Exploring software security approaches " +
                "in software development lifecycle: A systematic mapping study. Computer Standards & Interfaces, 50, 107-115. " +
                "de Vicente Mohino, J., Bermejo Higuera, J., Bermejo Higuera, J. R., & Sicilia Montalvo, J. A. (2019). The " +
                "application of a new secure software development life cycle (S-SDLC) with agile methodologies. Electronics," +
                " 8(11), 1218. ");
        projectDAO.addProject(projectCollaboration);
        
        int expectedResult = 1;
        int actualResult = projectDAO.deleteProjectByID(projectDAO.getProjectIDByTitle("Mantenimiento y mejoras de seguridad" +
                " del Sistema de Evaluación de Código"));
        assertEquals(expectedResult,actualResult);
    }
}