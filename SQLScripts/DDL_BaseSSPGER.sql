CREATE DATABASE SSPGER;
USE SSPGER;

CREATE TABLE CuentasAcceso (
	ID_usuario int not null auto_increment,
	nombreUsuario varchar(15) not null, 
	contrasena nvarchar(16) not null,
    tipoUsuario enum('Administrador', 'Estudiante', 'Profesor', 'RepresentanteCA') not null,
    PRIMARY KEY(ID_usuario),
    UNIQUE (nombreUsuario)
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
	claveCA varchar (10) not null,
	nombreCA varchar (45),
    DES_adscripción varchar(40),
    unidad_adscripción varchar(40),
    responsable int,
    PRIMARY KEY (claveCA)
);

CREATE TABLE ExperienciasEducativas (
	NRC int not null,
	nombre enum('Proyecto Guiado','Experiencia Recepcional'),
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
	claveCA varchar(10),
	nombreProyectoInvestigación varchar(200), 
	LGAC varchar(2),
	lineaInvestigacion varchar(300),
	duracionAprox varchar(10),
	ID_modalidadTR int,
	nombreTrabajoRecepcional varchar(200),
	requisitos varchar(500),
	ID_director int not null,
	alumnosParticipantes int,
	descripcionProyectoInvestigacion nvarchar(2000),
	descripcionTrabajoRecepcional nvarchar(2000),
	resultadosEsperados nvarchar(2000),
	bibliografiaRecomendada nvarchar(2000),
	estado enum('Verificado','Por revisar','Declinado') default 'Por revisar',
	etapa varchar(20),
	NRC int,
    PRIMARY KEY(ID_proyecto)
);

CREATE TABLE ModalidadesTR(
	ID_modalidadTR int not null auto_increment,
	modalidadTR enum('Monografía','Revisión Multivocal de la Literatura','Revisión Sistemática de la Literatura','Tesis','Trabajo Práctico-Técnico'),
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
	fechaInicio date not null,
	fechaEntrega date not null,
	ID_profesor int,
	ID_proyecto int,
    UNIQUE(nombre),
    PRIMARY KEY(ID_avance)
);

CREATE TABLE Evidencias (
	ID_evidencia int not null auto_increment,
	titulo varchar(30) not null,
	estado enum('Por revisar', 'Revisado') default 'Por revisar',
	calificacion int,
	descripcion varchar(100),
	ID_profesor int,
	ID_avance int,
	ID_proyecto int,
	matriculaEstudiante varchar(10),
    CHECK (calificacion>=0 and calificacion<=10),
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
	estado enum('Aceptado','Por validar','Rechazado') default 'Por validar',
	motivos varchar(850),
	PRIMARY KEY(ID_solicitudProyecto)
);
				
