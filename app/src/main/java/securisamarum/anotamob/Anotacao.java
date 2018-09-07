package securisamarum.anotamob;


public class Anotacao {
    Integer imagem,codigo,coracao, id, relacao;
    String diaStr,dataStr,observacao;

    public Integer[] myDrawable = {
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5,
            R.drawable.img_6,
            R.drawable.img_7,
            R.drawable.img_8,
            R.drawable.img_9
    };

    public Anotacao(Integer id, Integer codigo, String dia, String data, String observacao, Integer relacao) {
        this.id = id;
        this.codigo = codigo;
        this.diaStr = dia;
        this.dataStr = data;
        this.observacao = observacao;
        this.imagem = myDrawable[codigo-1];
        this.relacao = relacao;
        if(this.relacao==1) {
            this.coracao = R.drawable.coracao_sim;
        } else {
            this.coracao = R.drawable.coracao_nao;
        }
    }

    public Integer getImagem() {
        return imagem;
    }

    public Integer getCoracao() {
        return coracao;
    }

    public void setImagem(Integer imagem) {
        this.imagem = imagem;
    }

    public void setCoracao(Integer coracao) {
        this.coracao = coracao;
    }

    public void setCoracao() {
        if(this.relacao==1) {
            this.coracao = R.drawable.coracao_sim;
        } else {
            this.coracao = R.drawable.coracao_nao;
        }
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDiaStr() {
        return diaStr;
    }

    public void setDiaStr(String diaStr) {
        this.diaStr = diaStr;
    }

    public String getDataStr() {
        return dataStr;
    }

    public void setDataStr(String dataStr) {
        this.dataStr = dataStr;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRelacao() {
        return relacao;
    }

    public void setRelacao(Integer relacao) {
        this.relacao = relacao;
    }
}
