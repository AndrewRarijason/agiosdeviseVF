package mg.bmoi.agiosdevise.service.insertion;

import mg.bmoi.agiosdevise.DTO.HistoriqueArreteDto;
import mg.bmoi.agiosdevise.entity.HistoriqueArrete;
import mg.bmoi.agiosdevise.repository.HistoriqueArreteRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class InsertHistoArreteService {

    @Autowired
    private HistoriqueArreteRepository repository;

    private BigDecimal safe(BigDecimal value) {
        return value == null ? null : value.setScale(7, RoundingMode.HALF_UP);
    }

    private BigDecimal safe2(BigDecimal value) {
        return value == null ? null : value.setScale(2, RoundingMode.HALF_UP);
    }

    public void insertHistoriqueArrete(HistoriqueArreteDto dto) {
        HistoriqueArrete entity = repository.findByIdNcpAndIdDateDebutArreteAndIdDateFinArrete(
                dto.getNcp(), dto.getDateDebutArrete(), dto.getDateFinArrete()
        ).orElse(new HistoriqueArrete());

        entity.setAge(dto.getAge());
        entity.setDev(dto.getDev());
        if (entity.getId() == null) {
            entity.setId(new mg.bmoi.agiosdevise.entity.identifiants.HistoriqueArreteId());
        }
        entity.getId().setNcp(dto.getNcp());

        entity.setNomrest(dto.getNomrest());
        entity.setSoldeFinalCalcule (safe2(dto.getSoldeFinalCalcule()));

        entity.getId().setDateDebutArrete(dto.getDateDebutArrete());
        entity.getId().setDateFinArrete(dto.getDateFinArrete());

        entity.setSumMvtCrediteur (safe(dto.getSumMvtCrediteur()));
        entity.setTauxCrediteur (safe(dto.getTauxCrediteur()));
        entity.setInteretCrediteur (safe(dto.getInteretCrediteur()));
        entity.setSumMvtDebiteur (safe(dto.getSumMvtDebiteur()));
        entity.setTauxDebiteur (safe(dto.getTauxDebiteur()));
        entity.setInteretDebiteur (safe(dto.getInteretDebiteur()));
        entity.setNetAgios (safe2(dto.getNetAgios()));
        entity.setKo(dto.getKo());
        entity.setTcli(dto.getTcli());

        repository.save(entity);
    }
}