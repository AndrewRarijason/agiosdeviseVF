package mg.bmoi.agiosdevise.entity.identifiants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BkdarTempId implements Serializable {

    @Column (name = "DEV")
    private String dev;

    @Column (name = "NCP")
    private String ncp;

    @Column (name = "CLI")
    private String cli;

    public BkdarTempId() {}

    public BkdarTempId(String dev, String ncp, String cli) {
        this.dev = dev;
        this.ncp = ncp;
        this.cli = cli;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getNcp() {
        return ncp;
    }

    public void setNcp(String ncp) {
        this.ncp = ncp;
    }

    public String getCli() {
        return cli;
    }

    public void setCli(String cli) {
        this.cli = cli;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BkdarTempId that = (BkdarTempId) o;
        return Objects.equals(dev, that.dev) && Objects.equals(ncp, that.ncp) && Objects.equals(cli, that.cli);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dev, ncp, cli);
    }
}
