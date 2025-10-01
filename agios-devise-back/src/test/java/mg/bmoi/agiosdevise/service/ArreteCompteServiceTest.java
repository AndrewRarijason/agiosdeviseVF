package mg.bmoi.agiosdevise.service;

import mg.bmoi.agiosdevise.DTO.TransactionDto;
import mg.bmoi.agiosdevise.entity.Bkhis;
import mg.bmoi.agiosdevise.entity.identifiants.BkhisId;
import mg.bmoi.agiosdevise.repository.BkdarTempRepository;
import mg.bmoi.agiosdevise.repository.BkhisTempRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

class ArreteCompteServiceTest {

    @Test
    void testGestionTransactionPourChaqueCompte() {
        // Mock des repositories
   /*     BkdarTempRepository bkdarRepo = Mockito.mock(BkdarTempRepository.class);
        BkhisTempRepository bkhisRepo = Mockito.mock(BkhisTempRepository.class);

        // Données simulées
        List<Object[]> bkdarRows = new ArrayList<>();
        bkdarRows.add(new Object[]{
                "00010", "840", "01509701102", 0, 0, 0, 0, new BigDecimal("1000000"), "", "", "", "", "", java.sql.Date.valueOf("2025-01-01"), "", "", ""
        });
        Mockito.when(bkdarRepo.findAll()).thenReturn(bkdarRows);

        List<Bkhis> bkhisList = new ArrayList<>();
        bkhisList.add(bkhis("01509701102", "2025-01-07", "2025-01-06", new BigDecimal("100000"), 'D'));
        bkhisList.add(bkhis("01509701102", "2025-01-10", "2025-01-13", new BigDecimal("1679887"), 'C'));
        Mockito.when(bkhisRepo.findByIdNcpAndIdDcoAfterOrderByIdDcoAsc(Mockito.eq("01509701102"), Mockito.any()))
                .thenReturn(bkhisList);

        ArreteCompteService service = new ArreteCompteService(bkdarRepo, bkhisRepo);

        List<TransactionDto> result = service.gestionTransactionPourChaqueCompte(LocalDate.of(2025, 1, 1));

        result.forEach(tx -> System.out.println(
                "NCP: " + tx.getNcp() +
                        ", Date: " + tx.getDva() +
                        ", Montant: " + tx.getMon() +
                        ", Sens: " + tx.getSen() +
                        ", S1: " + tx.getS1() +
                        ", S2: " + tx.getS2() +
                        ", NbJours: " + tx.getNbJoursInactif()
        ));
    }

    private Bkhis bkhis(String ncp, String dco, String dva, BigDecimal mon, char sen) {
        Bkhis b = new Bkhis();
        b.setAge("00010");
        b.setDev("840");
        b.setMon(mon);
        b.setSen(sen);
        b.setId(new BkhisId(ncp, LocalDate.parse(dco), LocalDate.parse(dva), null, null));
        return b;*/
    }
}