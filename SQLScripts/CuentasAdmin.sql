USE SSPGER;

INSERT INTO CuentasAcceso (nombreUsuario, contrasena, tipoUsuario)
    VALUES ('LMCV', SHA2('lmcv123',256), 'Administrador'), 
           ('BDMG', SHA2('bdmg123',256), 'Administrador'),
           ('CES', SHA2('ces123',256), 'Administrador');
