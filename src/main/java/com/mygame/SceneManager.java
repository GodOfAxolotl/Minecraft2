package com.mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import jme3tools.optimize.GeometryBatchFactory;

/**
 * Static Class,
 *
 * */
public class SceneManager extends AbstractAppState {

    int x = 40;
    int y = 40;
    int z = 40;

    private SimpleApplication app;
    private AssetManager      assetManager;
    public  Node              scene;
    private Node              rootNode;
    public  BulletAppState    physics;

    public Node blocks = new Node("Blocks");

    Block[][][] world;

    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        this.app          = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.rootNode     = this.app.getRootNode();
        this.physics      = new BulletAppState();
        world = new Block[x][y][z];
        //physics.setDebugEnabled(true);
        stateManager.attach(physics);
        //buildFloor();
        buildWorld();
    }

    public void buildFloor() {
        Box b;
        b = new Box( 15f, 0.5f, 15f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //mat.getAdditionalRenderState().setWireframe(true);
        //mat.setTexture("ColorMap", assetManager.loadTexture(new TextureKey("Textures/grass.png")));
        mat.setColor("Color", ColorRGBA.White);

        Geometry block = new Geometry("grass", b);
        block.setLocalTranslation(0, -2f, 0);
        block.setMaterial(mat);

        rootNode.attachChild(block);

        RigidBodyControl block_phy = new RigidBodyControl(0);
        block.addControl(block_phy);
        physics.getPhysicsSpace().add(block_phy);
    }



    public void buildWorld() {
        int z  = 4;
        for(int i = 0; i < x; i+=1) {
            for(int j = 0; j < y; j+=1) {
                for(int k = 0; k < z; k+= 1) {

                    switch(k) {
                        case 0:
                            world[i][j][k] = new Block((i) *2, k*2 -2, (j) * 2, -2, assetManager); //Coincidentally, k matches the Grafikindex, this needs to be refactured for new layers.
                            blocks.attachChild(world[i][j][k].getGeometry());
                            physics.getPhysicsSpace().add(world[i][j][k].getBlock_phy());
                            break;
                        case 1:
                            world[i][j][k] = new Block((i) *2, k*2-2, (j) * 2, 2, assetManager); //Coincidentally, k matches the Grafikindex, this needs to be refactured for new layers.
                            blocks.attachChild(world[i][j][k].getGeometry());
                            physics.getPhysicsSpace().add(world[i][j][k].getBlock_phy());
                            break;
                        case 2:
                            world[i][j][k] = new Block((i) *2, k*2-2, (j) * 2, 1, assetManager); //Coincidentally, k matches the Grafikindex, this needs to be refactured for new layers.
                            blocks.attachChild(world[i][j][k].getGeometry());
                            physics.getPhysicsSpace().add(world[i][j][k].getBlock_phy());
                            break;
                        case 3:
                            world[i][j][k] = new Block((i) *2, k*2-2, (j) * 2, 0, assetManager); //Coincidentally, k matches the Grafikindex, this needs to be refactured for new layers.
                            blocks.attachChild(world[i][j][k].getGeometry());
                            physics.getPhysicsSpace().add(world[i][j][k].getBlock_phy());
                            break;
                        default:
                    }


                }
           }
        }

        rootNode.attachChild(blocks);
    }


    public void placeBlock(Camera camera, int idx) {
        Ray ray = new Ray(camera.getLocation(), camera.getDirection());
        CollisionResults results = new CollisionResults();
        blocks.collideWith(ray, results);

        if (results.size() > 0) {
            CollisionResult closest = results.getClosestCollision();
            Vector3f hitPoint = closest.getContactPoint();
            Vector3f hitNormal = closest.getContactNormal();

            Vector3f relativePosition = calculateRelativeBlockPosition(hitPoint, hitNormal, camera);

            placeBlockAtPosition(relativePosition, idx);
        }
    }

    private Vector3f calculateRelativeBlockPosition(Vector3f hitPoint, Vector3f hitNormal, Camera camera) {
        Vector3f cameraDirection = camera.getDirection();
        Vector3f cameraPosition = camera.getLocation();

        Vector3f hitToCamera = hitPoint.subtract(cameraPosition);
        cameraDirection.normalizeLocal();
        float distance = hitToCamera.dot(cameraDirection);
        Vector3f relativeBlockPosition = cameraPosition.add(cameraDirection.mult(distance));

        int gridSize = 2;
        float x = Math.round(relativeBlockPosition.x / gridSize) * gridSize;
        float y = Math.round(relativeBlockPosition.y / gridSize) * gridSize;
        float z = Math.round(relativeBlockPosition.z / gridSize) * gridSize;

        if (hitNormal.x < 0) {
            x -= gridSize;
        }
        if (hitNormal.y < 0) {
            y -= gridSize;
        }
        if (hitNormal.z < 0) {
            z -= gridSize;
        }

        Vector3f adjustedPosition = new Vector3f(x, y, z);//.add(hitNormal.mult(gridSize));

        return adjustedPosition;
    }

    private void placeBlockAtPosition(Vector3f position, int idx) {
        Block block = new Block(position, idx, assetManager);
        //world[(int)position.x][(int)position.y][(int)position.z] = block;
        blocks.attachChild(block.getGeometry());
        physics.getPhysicsSpace().add(block.getBlock_phy());
        rootNode.attachChild(blocks);
    }



    public void breakBlock(Camera cam) {
        CollisionResults r = new CollisionResults();
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        blocks.collideWith(ray, r);
        System.out.println(r.getClosestCollision());

        if(r.size() > 0 && r.getClosestCollision().getDistance() <= 9) {
            if( r.getClosestCollision().getGeometry().getName().endsWith("-2")) return;
            physics.getPhysicsSpace().remove(r.getClosestCollision().getGeometry().getControl(0));
            r.getClosestCollision().getGeometry().removeFromParent();
            rootNode.updateGeometricState();
        }
    }
}
