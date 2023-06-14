USE SSPGER;

ALTER TABLE Estudiantes
    ADD CONSTRAINT FK_Estudiantes_CuentasAcceso FOREIGN KEY (nombreUsuario) REFERENCES CuentasAcceso (nombreUsuario) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Profesores ADD
    CONSTRAINT FK_Profesores_CuentasAcceso FOREIGN KEY (nombreUsuario) REFERENCES CuentasAcceso (nombreUsuario) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Proyectos
    ADD CONSTRAINT FK_Proyectos_CuerpoAcademico FOREIGN KEY (claveCA) REFERENCES CuerpoAcademico (claveCA) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT FK_Proyectos_LGAC FOREIGN KEY (LGAC) REFERENCES LGAC (ID_lgac) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT FK_Proyectos_ModalidadesTR FOREIGN KEY (ID_modalidadTR) REFERENCES ModalidadesTR (ID_modalidadTR) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT FK_Proyectos_Directores FOREIGN KEY (ID_director) REFERENCES Profesores (ID_profesor) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT FK_Proyectos_Codirectores FOREIGN KEY (ID_codirector) REFERENCES Profesores (ID_profesor) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Avances
    ADD CONSTRAINT FK_Avances_Proyectos FOREIGN KEY (ID_proyecto) REFERENCES Proyectos (ID_proyecto) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Evidencias
    ADD CONSTRAINT FK_Evidencias_Estudiantes FOREIGN KEY (matriculaEstudiante) REFERENCES Estudiantes (matricula) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT FK_Evidencias_Avances FOREIGN KEY (ID_avance) REFERENCES Avances (ID_avance) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE SolicitudesProyecto
    ADD CONSTRAINT FK_SolicitudesProyecto_Proyectos FOREIGN KEY (ID_proyecto) REFERENCES Proyectos (ID_proyecto) ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT FK_SolicitudesProyecto_Estudiantes FOREIGN KEY (matriculaEstudiante) REFERENCES Estudiantes (matricula) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE Retroalimentacion
    ADD CONSTRAINT FK_Retroalimentaciones_Evidencias FOREIGN KEY (ID_evidencia) REFERENCES Evidencias (ID_evidencia) ON DELETE CASCADE ON UPDATE CASCADE;
