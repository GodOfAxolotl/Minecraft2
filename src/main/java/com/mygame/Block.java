package com.mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class Block extends Node {

    Geometry block;
    RigidBodyControl block_phy;


    public Block(int x, int y, int z, int idx, AssetManager assetManager) {
        Box b;
        b = new Box(1f, 1f, 1f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.getAdditionalRenderState().setWireframe(true);
        mat.setTexture("ColorMap", assetManager.loadTexture(new TextureKey(getGraphic(idx))));

        mat.setColor("Color", ColorRGBA.White);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        block = new Geometry("grass" + x + y + z + idx, b);
        System.out.println(block.getName());
        block.setLocalTranslation(x, y, z);
        block_phy = new RigidBodyControl(0f);
        block.addControl(block_phy);
        block.getControl(RigidBodyControl.class).setGravity(Vector3f.ZERO);

        block.setMaterial(mat);

    }


    public Block(float x, float y, float z, int idx, AssetManager assetManager) {
        Box b;
        b = new Box(1f, 1f, 1f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.getAdditionalRenderState().setWireframe(true);
        mat.setTexture("ColorMap", assetManager.loadTexture(new TextureKey(getGraphic(idx))));

        mat.setColor("Color", ColorRGBA.White);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        block = new Geometry("grass" + x + y + z, b);
        block.setLocalTranslation(x, y, z);
        block_phy = new RigidBodyControl(0f);
        block.addControl(block_phy);
        block.getControl(RigidBodyControl.class).setGravity(Vector3f.ZERO);

        block.setMaterial(mat);

    }


    public Block(Vector3f vec, int idx, AssetManager assetManager) {
        Box b;
        b = new Box(1f, 1f, 1f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.getAdditionalRenderState().setWireframe(true);

        mat.setTexture("ColorMap", assetManager.loadTexture(new TextureKey(getGraphic(idx))));

        mat.setColor("Color", ColorRGBA.White);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        block = new Geometry("grass" + vec.x + vec.y + vec.z, b);
        block.setLocalTranslation(vec.x, vec.y, vec.z);
        block_phy = new RigidBodyControl(0f);
        block.addControl(block_phy);
        block.getControl(RigidBodyControl.class).setGravity(Vector3f.ZERO);

        block.setMaterial(mat);

    }

    public static String getGraphic(int id) {
        switch(id) {
            case 0:
                return "Textures/grass.jpg";
            case 1:
                return "Textures/dirt.jpg";
            case 2:
                return "Textures/cobble.png";
            case 3:
                return "Textures/plank.png";
            case 4:
                return "Textures/diamand.png";
            case 5:
                return "Textures/quarz.png";
            case 6:
                return "Textures/glass.png";
            case -2:
                return "Textures/bedrock.png";
            default:
                return "Textures/grass.png";
        }
    }

    public Geometry getGeometry() {
        return block;
    }

    public RigidBodyControl getBlock_phy() {
        return block_phy;
    }

}
