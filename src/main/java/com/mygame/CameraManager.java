package com.mygame;


import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.ChaseCamera;


public class CameraManager extends AbstractAppState{

    private SimpleApplication app;
    private AppStateManager   stateManager;
    private Player            player;
    public  ChaseCamera       cam;

    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        this.app          = (SimpleApplication) app;
        this.stateManager = this.app.getStateManager();
        this.player       = this.stateManager.getState(PlayerManager.class).player;
        initCamera();
    }

    //Creates camera
    public void initCamera(){
        cam = new ChaseCamera(this.app.getCamera(), player.model, this.app.getInputManager());
        cam.setMinDistance(1);
        cam.setMaxDistance(1);
        cam.setDragToRotate(false);
        cam.setInvertVerticalAxis(true);
    }
}
