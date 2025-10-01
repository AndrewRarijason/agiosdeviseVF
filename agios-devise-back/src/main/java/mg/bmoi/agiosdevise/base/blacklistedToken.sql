-- Création de la table
CREATE TABLE MGSTGADR.BLACKLISTED_TOKEN (
                                   ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                   TOKEN VARCHAR2(512) NOT NULL,
                                   EXPIRY_DATE TIMESTAMP NOT NULL,
                                   CONSTRAINT UK_BLACKLISTED_TOKEN UNIQUE (TOKEN)
);


-- Création d'un index sur la date d'expiration pour le nettoyage périodique
CREATE INDEX IDX_BLACKLISTED_TOKEN_EXPIRY ON MGSTGADR.BLACKLISTED_TOKEN (EXPIRY_DATE);
commit;
-- Procédure de nettoyage des tokens expirés
CREATE OR REPLACE PROCEDURE CLEAN_EXPIRED_TOKENS AS
BEGIN
    DELETE FROM MGSTGADR.BLACKLISTED_TOKEN
    WHERE EXPIRY_DATE < SYSTIMESTAMP;
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Tokens expirés nettoyés: ' || SQL%ROWCOUNT);
END;
/

-- Job planifié pour le nettoyage automatique (exécution toutes les heures)
BEGIN
    DBMS_SCHEDULER.CREATE_JOB (
        job_name        => 'CLEAN_TOKENS_JOB',
        job_type        => 'STORED_PROCEDURE',
        job_action      => 'CLEAN_EXPIRED_TOKENS',
        start_date      => SYSTIMESTAMP,
        repeat_interval => 'FREQ=MINUTELY; INTERVAL=30',
        enabled         => TRUE,
        comments        => 'Nettoyage automatique des tokens JWT expir�s toutes les 30 minutes'
    );
END;
/

select* from MGSTGADR.BLACKLISTED_TOKEN;

INSERT INTO MGSTGADR.BLACKLISTED_TOKEN (TOKEN, EXPIRY_DATE)
VALUES (
    'eyJhbGci',  -- Votre token JWT
    SYSTIMESTAMP + INTERVAL '1' HOUR
);

-- Vue pour surveiller les tokens blacklistés
CREATE OR REPLACE VIEW VW_ACTIVE_BLACKLISTED_TOKENS AS
SELECT ID, TOKEN, EXPIRY_DATE
FROM MGSTGADR.BLACKLISTED_TOKEN
WHERE EXPIRY_DATE >= SYSTIMESTAMP
ORDER BY EXPIRY_DATE;