MERGE INTO "PUBLIC"."USER_ENTITY" (NAME, CLOSED, CONFIRMED, DESCRIPTION, EMAIL, NAMEBLOG, PASSWORD, ROLE)
VALUES ('admin', 0, 1, '', 'email', 'Какой-то блог',
'$2a$10$VukG7RhGnTZ0FZNBJULF9upk9zl7i9iUQRfe/2BP7FDcmMIlKCcy2', 'ADMIN');