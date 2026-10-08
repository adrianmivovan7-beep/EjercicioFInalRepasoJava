package org.cuatrovientos.dam.psp.KillEnemies.version.facil;

public class Enemy implements Character {

    @Override
    public boolean isEnemy() {
        return true;
    }

    public void kill() {
        System.out.println("Ahhhggg, me mataste, bastardo!");
    }
}