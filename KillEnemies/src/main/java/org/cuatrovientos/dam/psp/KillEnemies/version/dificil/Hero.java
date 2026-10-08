package org.cuatrovientos.dam.psp.KillEnemies.version.dificil;

public class Hero implements Character {

    private String nombre;
    private int enemigosMatados;
    private int amigosDefendidos;

    public Hero(String nombre) {
        this.nombre = nombre;
        enemigosMatados = 0;
        amigosDefendidos = 0;
    }
    
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean isEnemy() {
        return false;
    }

    public void attack(Enemy enemy) {
        System.out.println("¡He atacado a un enemigo!");
        enemy.kill();
        enemigosMatados++;
    }

    public void defend(Friend friend) {
        System.out.println("¡He defendido a un amigo!");
        friend.heal();
        amigosDefendidos++;
    }
    
    public void mostrarEstadisticas() {
        System.out.println("Héroe: " + nombre);
        System.out.println("Enemigos matados: " + enemigosMatados);
        System.out.println("Amigos defendidos: " + amigosDefendidos);
    }
}