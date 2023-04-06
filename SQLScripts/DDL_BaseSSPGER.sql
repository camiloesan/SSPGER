CREATE DATABASE SSPGER;
USE SSPGER;

CREATE TABLE CuentasAcceso (
	ID_usuario int not null auto_increment,
	nombreUsuario varchar(15), 
	contrasena nvarchar(16),
	tipoUsuario varchar(20),
    PRIMARY KEY(ID_usuario)
);

CREATE TABLE Estudiantes (
	matricula varchar(10) not null,
	nombre varchar(30),
	apellidoPaterno varchar(30),
	apellidoMaterno varchar(30),
	correoInstitucional nvarchar(30),
	NRC int,
	ID_usuario int,
    PRIMARY KEY(matricula)
);

CREATE TABLE Profesores (
	ID_profesor int not null auto_increment,
	nombre varchar(30),
	apellidoPaterno varchar(30), 
	apellidoMaterno varchar(30),
	correoInstitucional nvarchar(30),
	ID_usuario int,
    PRIMARY KEY(ID_profesor)
);

CREATE TABLE CuerpoAcademico (
	clave varchar (10) not null,
    DES_adscripción varchar(15),
    unidad_adscripción varchar(15),
    responsable int,
    PRIMARY KEY (clave)
);

CREATE TABLE ExperienciasEducativas (
	NRC int not null,
	nombre varchar(20),
	ID_profesor int,
    PRIMARY KEY(NRC)
);

CREATE TABLE CodirectoresProyecto (
	ID_codirectorProyecto int not null auto_increment,
	ID_proyecto int,
	ID_profesor int,
	PRIMARY KEY(ID_codirectorProyecto)
);

CREATE TABLE Proyectos (
	ID_proyecto int not null auto_increment,
	claveCuerpoAcademico varchar(10),
	nombreProyectoInvestigación varchar(30), 
	LGAC varchar(2),
	lineaInvestigacion varchar(200),
	duracionAprox varchar(10),
	ID_modalidadTR int,
	nombreTrabajoRecepcional varchar(30),
	requisitos varchar(500),
	ID_director int,
	alumnosParticipantes int,
	descripcionProyectoInvestigacion nvarchar(2000),
	descripcionTrabajoRecepcional nvarchar(2000),
	resultadosEsperados nvarchar(2000),
	bibliografiaRecomendada nvarchar(2000),
	estado varchar(15) default 'Por revisar.',
	etapa varchar(20),
	NRC int,
    PRIMARY KEY(ID_proyecto)
);

CREATE TABLE ModalidadesTR(
	ID_modalidadTR int not null auto_increment,
	modalidadTR varchar(15),
    PRIMARY KEY(ID_modalidadTR)
);

CREATE TABLE LGAC (
	clave varchar(2) not null,
	nombre varchar(50),
	descripcion varchar(400),
    PRIMARY KEY(clave)
);

CREATE TABLE Avances (
	ID_avance int not null auto_increment,
	nombre varchar(30),
	descripcion nvarchar(800),
	fechaInicio date,
	fechaEntrega date,
	ID_profesor int,
	ID_proyecto int,
    PRIMARY KEY(ID_avance)
);

CREATE TABLE Evidencias (
	ID_evidencia int not null auto_increment,
	titulo varchar(30),
	estado varchar(15) default 'Por revisar.',
	calificacion int,
	descripcion varchar(100),
	ID_profesor int,
	ID_avance int,
	ID_proyecto int,
	matriculaEstudiante varchar(10),
    PRIMARY KEY(ID_Evidencia)
);

CREATE TABLE Reportes (
	ID_reporte int not null auto_increment,
	titulo varchar (30),
	descripcion varchar (200),
	ID_profesor int,
	PRIMARY KEY(ID_reporte)
);

CREATE TABLE ReporteEstudiantes (
	ID_reporteEstudiante int not null auto_increment,
	matricula varchar(10),
	ID_reporte int,
	PRIMARY KEY(ID_reporteEstudiante)
);

CREATE TABLE SolicitudesProyecto (
	ID_solicitudProyecto int not null auto_increment,
	ID_proyecto int,
	matriculaEstudiante varchar(10),
	estado varchar(20) default 'Por aceptar',
	motivos varchar(850),
	PRIMARY KEY(ID_solicitudProyecto)
);
				
