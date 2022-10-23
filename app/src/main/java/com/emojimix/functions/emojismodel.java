package com.emojimix.functions;



public class emojismodel {
    private String ojos;
    private String base;
    private String bocas;
    private String ojos_objetos;
    private String cejas;
    private String objetos;
    private String manos;
    private String tipo1;
    private String tipo2;
    private String objetoextra;
    private String fondo;

    private int width_height;
    private int left;
    private int top;
    private float rotacion;

    public String getojos() {
        return ojos;
    }
    public String getbase() {
        return base;
    }

    public String getbocas() {
        return bocas;
    }
    public String getojos_objetos() {
        return ojos_objetos;
    }
    public String getcejas() {
        return cejas;
    }
    public String getobjetos() {
        return objetos;
    }
    public String getmanos() {
        return manos;
    }
    public int getancho() {
        return width_height;
    }
    public int getleft() {
        return left;
    }
    public int gettop() {
        return top;
    }
    public float getrotacion() {
        return rotacion;
    }
    public String gettipo1() {
        return tipo1;
    }
    public String gettipo2() {
        return tipo2;
    }
    public String getextra() {
        return objetoextra;
    }
    public String getfondo() {
        return fondo;
    }

    public void setSuma(String ojos) {
        this.ojos = ojos;
    }
}
