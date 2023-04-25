USE SSPGER;

ALTER TABLE Estudiantes
    ADD CONSTRAINT FK_Estudiantes_CuentasAcceso FOREIGN KEY (nombreUsuario) REFERENCES CuentasAcceso (nombreUsuario) ON DELETE CASCADE;

ALTER TABLE Profesores ADD
    CONSTRAINT FK_Profesores_CuentasAcceso FOREIGN KEY (nombreUsuario) REFERENCES CuentasAcceso (nombreUsuario) ON DELETE CASCADE;

ALTER TABLE CuerpoAcademico ADD
    CONSTRAINT FK_CuerpoAcademico_Profesores FOREIGN KEY (responsable) REFERENCES Profesores (ID_profesor) ON DELETE CASCADE;

ALTER TABLE Proyectos
    ADD CONSTRAINT FK_Proyectos_LGAC FOREIGN KEY (LGAC) REFERENCES LGAC (ID_lgac) ON DELETE CASCADE,
    ADD CONSTRAINT FK_Proyectos_ModalidadesTR FOREIGN KEY (ID_modalidadTR) REFERENCES ModalidadesTR (ID_modalidadTR) ON DELETE CASCADE,
    ADD CONSTRAINT FK_Proyectos_Directores FOREIGN KEY (ID_director) REFERENCES Profesores (ID_profesor) ON DELETE CASCADE,
    ADD CONSTRAINT FK_Proyectos_Codirectores FOREIGN KEY (ID_codirector) REFERENCES Profesores (ID_profesor) ON DELETE CASCADE;

ALTER TABLE Avances
    ADD CONSTRAINT FK_Avances_Proyectos FOREIGN KEY (ID_proyecto) REFERENCES Proyectos (ID_proyecto) ON DELETE CASCADE;

ALTER TABLE Evidencias
    ADD CONSTRAINT FK_Evidencias_Profesores FOREIGN KEY (ID_profesor) REFERENCES Profesores (ID_profesor) ON DELETE CASCADE,
    ADD CONSTRAINT FK_Evidencias_Estudiantes FOREIGN KEY (matriculaEstudiante) REFERENCES Estudiantes (matricula) ON DELETE CASCADE;

ALTER TABLE SolicitudesProyecto
    ADD CONSTRAINT FK_SolicitudesProyecto_Proyectos FOREIGN KEY (ID_proyecto) REFERENCES Proyectos (ID_proyecto) ON DELETE CASCADE,
    ADD CONSTRAINT FK_SolicitudesProyecto_Estudiantes FOREIGN KEY (matriculaEstudiante) REFERENCES Estudiantes (matricula) ON DELETE CASCADE;
