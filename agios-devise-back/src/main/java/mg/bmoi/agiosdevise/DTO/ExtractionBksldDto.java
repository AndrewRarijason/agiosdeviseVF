package mg.bmoi.agiosdevise.DTO;

import java.math.BigDecimal;
import java.util.Objects;

public class ExtractionBksldDto {
    private final String age;
    private final String dev;
    private final String ncp;
    private final BigDecimal sde;

    public ExtractionBksldDto(String age, String dev, String ncp, BigDecimal sde) {
        this.age = Objects.requireNonNull(age, "AGE ne peut pas être null");
        this.dev = Objects.requireNonNull(dev, "DEV ne peut pas être null");
        this.ncp = Objects.requireNonNull(ncp, "NCP ne peut pas être null");
        this.sde = sde != null ? sde : BigDecimal.ZERO;
    }

    // Getters
    public String getAge() { return age; }
    public String getDev() { return dev; }
    public String getNcp() { return ncp; }
    public BigDecimal getSde() { return sde; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractionBksldDto that = (ExtractionBksldDto) o;
        return age.equals(that.age) &&
                dev.equals(that.dev) &&
                ncp.equals(that.ncp) &&
                sde.equals(that.sde);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, dev, ncp, sde);
    }
}