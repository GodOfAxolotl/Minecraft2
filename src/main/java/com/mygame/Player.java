package com.mygame;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Player extends Node {

    public BetterCharacterControl playerPhys;
    public Node model;

    public int inventorySlot = 0;

    public Player() {
        model      = new Node();
        playerPhys = new BetterCharacterControl(0.9f, 2f, 10f);
        playerPhys.setJumpForce(new Vector3f(0f,70,0));
        playerPhys.setGravity(new Vector3f(0, 100, 0));

    }

    public void jump(){
        playerPhys.jump();
    }

    public void walk(Vector3f direction) {
        playerPhys.setWalkDirection(direction);
    }

    public void stop() {
        playerPhys.setWalkDirection(Vector3f.ZERO);

    }
}


