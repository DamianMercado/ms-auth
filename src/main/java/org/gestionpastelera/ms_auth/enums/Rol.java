package org.gestionpastelera.ms_auth.enums;

public enum Rol {
    ADMIN("ADMIN"),
    VENDEDOR_SUPERVISOR("VENDEDOR_SUPERVISOR"),
    VENDEDOR("VENDEDOR"),
    CLIENTE("CLIENTE"),
    CAJERO("CAJERO");


    private final String valor;

    Rol(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static Rol fromValor(String valor) {

        for (Rol r : values()) {

            if (r.valor.equalsIgnoreCase(valor)) {
                return r;
            }
        }

        throw new IllegalArgumentException(
                "Rol de usuario desconocido: " + valor
        );
    }
}
