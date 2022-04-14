package fr.utln.jmonkey;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;

import java.util.Random;

/** cette classe permet le creation de l'objet food**/
public class Food {
    Random rand = new Random();
    Spatial food;
    Vector3f position;

    Food(Node rootNode, AssetManager assetManager)
    {
        int x = (rand.nextInt()%10);
        int y = (rand.nextInt()%10);
        int z = (rand.nextInt()%10);

        position = new Vector3f(x*10, y*10, z*10);

        food = assetManager.loadModel("Meat.obj");
        Material mat_default = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");
        Texture meatTexture = assetManager.loadTexture(
                "Meat_texture.jpg");
        meatTexture.setWrap(Texture.WrapMode.Repeat);
        mat_default.setTexture("DiffuseMap", meatTexture);
        food.setMaterial(mat_default);
        food.setLocalTranslation(position);
        food.setLocalScale(2,3,2);
        rootNode.attachChild(food);

        AmbientLight al = new AmbientLight();
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(Vector3f.UNIT_XYZ.negate());
        rootNode.addLight(dl);
    }
}
