package com.mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {


    private Vector3f walkDirection = new Vector3f(0,0,0);
    boolean left, right, up, down, shift = false;

    BitmapText hudText;
    Picture guiPic;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        this.getFlyByCamera().setEnabled(false);
        setDisplayStatView(true);
        setDisplayFps(true);
        flyCam.setEnabled(false);

        this.stateManager.attach(new SceneManager());
        this.stateManager.attach(new PlayerManager());
        this.stateManager.attach(new CameraManager());

        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.Black);                             // font color
        hudText.setText("Inventory: ");             // the text
        hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
        guiNode.attachChild(hudText);
        //rootNode.attachChild(guiNode);

        Picture guiPic = new Picture("HUD Picture");
        guiPic.setImage(assetManager, "Textures/diamand.png", true);
        guiPic.setWidth(40);
        guiPic.setHeight(40);
        guiPic.setPosition(settings.getWidth()/8, settings.getHeight()/8);
        guiNode.attachChild(guiPic);

        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.0f,-.5f,-.0f).normalizeLocal());
        rootNode.addLight(sun);

        //SceneManager.buildPlane(10, 10, assetManager, rootNode);

        viewPort.setBackgroundColor(ColorRGBA.fromRGBA255(50,70, 255, 255));        initKeys();
        initCrossHairs();
    }

    final private AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            if ("ScrollUp".equals(name)) {
                stateManager.getState(PlayerManager.class).player.inventorySlot++;
                if(stateManager.getState(PlayerManager.class).player.inventorySlot > 6)
                    stateManager.getState(PlayerManager.class).player.inventorySlot = 6;
                hudText.setText("Inventory: " + stateManager.getState(PlayerManager.class).player.inventorySlot);
                initPicture(stateManager.getState(PlayerManager.class).player.inventorySlot);

            } else if ("ScrollDown".equals(name)) {
                stateManager.getState(PlayerManager.class).player.inventorySlot--;
                if(stateManager.getState(PlayerManager.class).player.inventorySlot < 0)
                    stateManager.getState(PlayerManager.class).player.inventorySlot = 0;
                hudText.setText("Inventory: " + stateManager.getState(PlayerManager.class).player.inventorySlot);
                initPicture(stateManager.getState(PlayerManager.class).player.inventorySlot);
            }
        }
    };

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean value, float tpf) {

            if (name.equals("Jump")) {
                stateManager.getState(PlayerManager.class).player.jump();
            }
            if (name.equals("W")) {
                if (value) up = true;
                else up = false;
            }
            if (name.equals("A")) {
                if (value) left = true;
                else left = false;
            }
            if (name.equals("S")) {
                if (value) down = true;
                else down = false;
            }
            if (name.equals("D")) {
                if (value) right = true;
                else right = false;
            }
            if (name.equals("Shift")) {
                if (value) shift = true;
                else shift = false;
            }
            if(name.equals("Revive")) {
            }
            if(name.equals("Place") && value) {
                stateManager.getState(SceneManager.class).placeBlock(cam, stateManager.getState(PlayerManager.class).player.inventorySlot);
            }
            if(name.equals("Break") && value) {
                stateManager.getState(SceneManager.class).breakBlock(cam);
            }
        }
    };

    private void initKeys() {
        inputManager.addMapping("Jump",  new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Revive", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Shift", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("Place", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("Break", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("ScrollUp", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("ScrollDown", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addListener(actionListener, "Jump", "Revive", "W", "A", "S", "D", "Shift", "Place", "Break");
        inputManager.addListener(analogListener, "ScrollUp", "ScrollDown");
    }

    private void initCrossHairs() {
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+");
        ch.setLocalTranslation(
        settings.getWidth() / 2 - ch.getLineWidth() / 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }

    public void initPicture(int id) {
        guiPic = new Picture("HUD Picture");
        guiPic.setImage(assetManager, Block.getGraphic(id), true);
        guiPic.setWidth(40);
        guiPic.setHeight(40);
        guiPic.setPosition(settings.getWidth()/8, settings.getHeight()/8);
        guiNode.attachChild(guiPic);
    }
    @Override
    public void simpleUpdate(float tpf) {
        System.out.println(stateManager.getState(PlayerManager.class).player.inventorySlot);
        //System.out.println(Math.pow(tpf, -1));
        Vector3f camDir = cam.getDirection().clone();
        Vector3f camLeft = cam.getLeft().clone();
        camDir.y = 0;
        camLeft.y = 0;
        camDir.normalizeLocal();
        camLeft.normalizeLocal();
        walkDirection.set(0f, 0f, 0f);
        if (left)  walkDirection.addLocal(camLeft);
        if (right) walkDirection.addLocal(camLeft.negate());
        if (up) walkDirection.addLocal(camDir);
        if (down) walkDirection.addLocal(camDir.negate());
        if(shift) walkDirection = walkDirection.mult(2);

        stateManager.getState(PlayerManager.class).player.walk(walkDirection.mult(5));



        //stateManager.getState(PlayerManager.class).update(tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }



}

