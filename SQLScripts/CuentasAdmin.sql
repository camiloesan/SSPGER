USE SSPGER;

INSERT INTO CuentasAcceso (nombreUsuario, contrasena, correoInstitucional, tipoUsuario)
    VALUES ('LMCV', SHA2('lmcv123',256), 'zs21013875@estudiantes.uv.mx', 'Administrador'),
           ('BDMG', SHA2('bdmg123',256), 'bryamdmg@gmail.com','Administrador'),
           ('CES', SHA2('ces123',256), 'zs21013861@estudiantes.uv.mx','Administrador');