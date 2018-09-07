package securisamarum.anotamob;

public class Ciclo {
    Integer id;
    String dataStr;

    public Ciclo(Integer id, String dataStr) {
        this.id = id;
        this.dataStr = dataStr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }
}
