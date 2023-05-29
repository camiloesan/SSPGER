INSERT INTO ModalidadesTR (modalidadTR)
VALUES ('Monografía'),
       ('Revisión Multivocal de la Literatura'),
       ('Revisión Sistemática de la Literatura'),
       ('Tesis'),
       ('Trabajo Práctico-Técnico');

INSERT INTO LGAC (clave, nombre, descripcion)
VALUES ('L1','Gestión, modelado y desarrollo de software','Se orienta al estudio de los diversos métodos y enfoques para la gestión, modelado y desarrollo de software, de manera que se obtenga software de calidad. Gestión de las diversas etapas del proceso de desarrollo, incluyendo hasta la medición del proceso y artefactos. Modelado de los diversos artefactos en las distintas etapas del proceso de desarrollo.'),
       ('L2','Tecnologías de software','Se orienta al estudio de diversas propiedades, enfoques, métodos de modelado y herramientas que conforman cada una de las diversas tecnologías aplicables al desarrollo del software con vistas a su adaptación, mejora y sustitución en el medio nacional.');

INSERT INTO CuerpoAcademico (nombreCA, claveCA, DES_adscripción, unidad_adscripción)
VALUES ('Ingeniería y Tecnología de Software',
        'UV-CA-127',
        'Económico-Administrativa Xalapa',
        'Facultad de Estadística e Informática');