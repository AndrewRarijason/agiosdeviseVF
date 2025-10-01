-- Tables temporaires de stockage de donnÃ©es extraites --
-- BKSLD --
CREATE GLOBAL TEMPORARY TABLE TMP_BKSLD_D (
                                            AGE   VARCHAR2(10),
                                            DEV   VARCHAR2(3),
                                            NCP   VARCHAR2(20),
                                            SDE   NUMBER(18,2)
) ON COMMIT DELETE ROWS;

-- BKDAR --
CREATE GLOBAL TEMPORARY TABLE TMP_BKDAR_D (
                                            AGE      VARCHAR2(10),
                                            DEV      VARCHAR2(3),
                                            NCP      VARCHAR2(20),
                                            NBC      NUMBER,
                                            TXC      NUMBER(10,4),
                                            NBR      NUMBER,
                                            TAUX     NUMBER(10,4),
                                            SOLDE    NUMBER(18,2),
                                            NOMREST  VARCHAR2(100),
                                            ADR1     VARCHAR2(100),
                                            ADR2     VARCHAR2(100),
                                            CPOS     VARCHAR2(10),
                                            CLC      VARCHAR2(10),
                                            DATR     DATE,
                                            TCLI     VARCHAR2(10),
                                            VIL      VARCHAR2(50),
                                            CLI      VARCHAR2(20)
) ON COMMIT DELETE ROWS;


-- BKHIS --
CREATE GLOBAL TEMPORARY TABLE TMP_BKHIS_D (
                                            AGE VARCHAR2(10),
                                            DEV       VARCHAR2(3),
                                            NCP       VARCHAR2(20),
                                            DCO       DATE,
                                            DVA       DATE,
                                            MON       NUMBER(18,2),
                                            SEN       CHAR
) ON COMMIT DELETE ROWS;

-- Renommer la table existante (par exemple en version archivée)
ALTER TABLE TMP_BKHIS_D RENAME TO TMP_BKHIS_no;
-- Renommer la colonne "AGE" en "CODE_AGENCE" dans TMP_BKSLD


SELECT* FROM MGSTGADR.BKSLD_D;
SELECT* FROM MGSTGADR.TEST_BKDAR;
SELECT* FROM MGSTGADR.BKDAR_D;
SELECT* FROM MGSTGADR.TMP_BKHIS_D;


SELECT* FROM test;
CREATE TABLE test (nom VARCHAR(50));
INSERT INTO test (nom) VALUES ('Rakoto');
