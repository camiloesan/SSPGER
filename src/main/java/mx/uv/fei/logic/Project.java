package mx.uv.fei.logic;

public class Project {
    private int projectId;
    private String academicBodyId;
    private String investigationProjectName;
    private int LGAC_Id;
    private String investigationLine;
    private String approximateDuration;
    private int modalityId;
    private String receptionWorkName;
    private String requisites;
    private int directorID;
    private int studentsParticipating;
    private String investigationProjectDescription;
    private String receptionWorkDescription;
    private String expectedResults;
    private String recommendedBibliography;
    private String state;
    private String stage;
    private int NRC;

    public Project() {}

    public Project(String academicBodyId, String investigationProjectName, int LGAC_Id, String investigationLine, String approximateDuration, int modalityId, String receptionWorkName, String requisites, int directorID, int studentsParticipating, String investigationProjectDescription, String receptionWorkDescription, String expectedResults, String recommendedBibliography, String state, String stage, int NRC) {
        this.academicBodyId = academicBodyId;
        this.investigationProjectName = investigationProjectName;
        this.LGAC_Id = LGAC_Id;
        this.investigationLine = investigationLine;
        this.approximateDuration = approximateDuration;
        this.modalityId = modalityId;
        this.receptionWorkName = receptionWorkName;
        this.requisites = requisites;
        this.directorID = directorID;
        this.studentsParticipating = studentsParticipating;
        this.investigationProjectDescription = investigationProjectDescription;
        this.receptionWorkDescription = receptionWorkDescription;
        this.expectedResults = expectedResults;
        this.recommendedBibliography = recommendedBibliography;
        this.state = state;
        this.stage = stage;
        this.NRC = NRC;
    }

    public String getAcademicBodyId() {
        return academicBodyId;
    }

    public void setAcademicBodyId(String academicBodyId) {
        this.academicBodyId = academicBodyId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getInvestigationProjectName() {
        return investigationProjectName;
    }

    public void setInvestigationProjectName(String investigationProjectName) {
        this.investigationProjectName = investigationProjectName;
    }

    public int getLGAC_Id() {
        return LGAC_Id;
    }

    public void setLGAC_Id(int LGAC_Id) {
        this.LGAC_Id = LGAC_Id;
    }

    public String getInvestigationLine() {
        return investigationLine;
    }

    public void setInvestigationLine(String investigationLine) {
        this.investigationLine = investigationLine;
    }

    public String getApproximateDuration() {
        return approximateDuration;
    }

    public void setApproximateDuration(String approximateDuration) {
        this.approximateDuration = approximateDuration;
    }

    public int getModalityId() {
        return modalityId;
    }

    public void setModalityId(int modalityId) {
        this.modalityId = modalityId;
    }

    public String getReceptionWorkName() {
        return receptionWorkName;
    }

    public void setReceptionWorkName(String receptionWorkName) {
        this.receptionWorkName = receptionWorkName;
    }

    public String getRequisites(){
        return requisites;
    }

    public void setRequisites(String requisites) {
        this.requisites = requisites;
    }

    public int getDirectorID() {
        return directorID;
    }

    public void setDirectorID(int directorID) {
        this.directorID = directorID;
    }

    public int getStudentsParticipating() {
        return studentsParticipating;
    }

    public void setStudentsParticipating(int studentsParticipating) {
        this.studentsParticipating = studentsParticipating;
    }

    public String getInvestigationProjectDescription() {
        return investigationProjectDescription;
    }

    public void setInvestigationProjectDescription(String investigationProjectDescription) {
        this.investigationProjectDescription = investigationProjectDescription;
    }

    public String getReceptionWorkDescription() {
        return receptionWorkDescription;
    }

    public void setReceptionWorkDescription(String receptionWorkDescription) {
        this.receptionWorkDescription = receptionWorkDescription;
    }

    public String getExpectedResults() {
        return expectedResults;
    }

    public void setExpectedResults(String expectedResults) {
        this.expectedResults = expectedResults;
    }

    public String getRecommendedBibliography() {
        return recommendedBibliography;
    }

    public void setRecommendedBibliography(String recommendedBibliography) {
        this.recommendedBibliography = recommendedBibliography;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getNRC() {
        return NRC;
    }

    public void setNRC(int NRC) {
        this.NRC = NRC;
    }
}
