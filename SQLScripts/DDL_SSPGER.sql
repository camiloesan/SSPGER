DROP DATABASE IF EXISTS SSPGER;

CREATE DATABASE SSPGER DEFAULT CHARACTER SET utf8;
USE SSPGER;

CREATE TABLE CuentasAcceso (
	ID_usuario int not null auto_increment,
	nombreUsuario nvarchar(28) not null,
	contrasena nvarchar(64) not null,
    correoInstitucional nvarchar(30) not null,
    tipoUsuario enum('Administrador', 'Estudiante', 'Profesor', 'RepresentanteCA') not null,
    PRIMARY KEY(ID_usuario),
    UNIQUE (nombreUsuario),
    UNIQUE (correoInstitucional)
);

CREATE TABLE Estudiantes (
	matricula nvarchar(10) not null,
	nombre nvarchar(80),
	apellidos nvarchar(80),
	nombreUsuario nvarchar(28),
    PRIMARY KEY(matricula)
);

CREATE TABLE Profesores (
	ID_profesor int not null auto_increment,
	grado enum('Dr.','Dra.', 'MCC.') not null ,
	nombre nvarchar(80),
	apellidos nvarchar(80),
	nombreUsuario nvarchar(28),
    PRIMARY KEY(ID_profesor)
);

CREATE TABLE CuerpoAcademico (
	claveCA nvarchar (10) not null,
	nombreCA nvarchar (45),
    DES_adscripción nvarchar(40),
    unidad_adscripción nvarchar(40),
    PRIMARY KEY (claveCA)
);

CREATE TABLE Proyectos (
	ID_proyecto int not null auto_increment,
	claveCA nvarchar(10),
	nombreProyectoInvestigación nvarchar(200),
	LGAC int not null,
	lineaInvestigacion nvarchar(300),
	duracionAprox nvarchar(10),
	ID_modalidadTR int,
	nombreTrabajoRecepcional nvarchar(200),
	requisitos nvarchar(500),
	ID_director int,
	ID_codirector int,
	alumnosParticipantes int,
	espaciosDisponibles int,
	descripcionProyectoInvestigacion nvarchar(5000),
	descripcionTrabajoRecepcional nvarchar(5000),
	resultadosEsperados nvarchar(1000),
	bibliografiaRecomendada nvarchar(6000),
	estado enum('Verificado','Por revisar','Declinado') default 'Por revisar',
	etapa enum('Proyecto guiado', 'Trabajo Recepcional') default 'Proyecto guiado',
    PRIMARY KEY(ID_proyecto),
    UNIQUE (nombreTrabajoRecepcional)
);


CREATE TABLE ModalidadesTR(
	ID_modalidadTR int not null auto_increment,
	modalidadTR enum('Monografía','Revisión Multivocal de la Literatura','Revisión Sistemática de la Literatura','Tesis','Trabajo Práctico-Técnico'),
    PRIMARY KEY(ID_modalidadTR)
);

CREATE TABLE LGAC (
    ID_lgac int not null auto_increment,
	clave nvarchar(2) not null,
	nombre nvarchar(50),
	descripcion varchar(400),
    PRIMARY KEY(ID_lgac)
);

CREATE TABLE Avances (
	ID_avance int not null auto_increment,
	nombre nvarchar(30),
	descripcion nvarchar(800),
	fechaInicio date not null,
	fechaEntrega date not null,
	ID_proyecto int,
    PRIMARY KEY(ID_avance)
);

CREATE TABLE Evidencias (
	ID_evidencia int not null auto_increment,
	titulo nvarchar(30) not null,
	estado enum('Por revisar', 'Revisado') default 'Por revisar',
	calificacion int,
	descripcion nvarchar(100),
	ID_avance int,
	matriculaEstudiante nvarchar(10),
	fechaEntrega Date,
    CHECK (calificacion>=0 and calificacion<=10),
    PRIMARY KEY(ID_evidencia)
);

CREATE TABLE SolicitudesProyecto (
	ID_solicitudProyecto int not null auto_increment,
	ID_proyecto int,
	matriculaEstudiante varchar(10),
	estado enum('Aceptado','Por validar','Rechazado') default 'Por validar',
	motivos varchar(850),
	PRIMARY KEY(ID_solicitudProyecto)
);

CREATE TABLE Retroalimentacion (
    ID_retroalimentacion int not null auto_increment,
    ID_evidencia int,
    textoRetroalimentacion nvarchar(850),
    PRIMARY KEY (ID_retroalimentacion)
);
				
