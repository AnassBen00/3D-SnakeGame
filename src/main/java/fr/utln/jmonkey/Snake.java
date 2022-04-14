package fr.utln.jmonkey;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/** cette classe permet le creation d'un objet snake**/
public class Snake {
    Geometry body;

    Snake(Node rootNode, AssetManager assetManager)
    {
        Box b = new Box(Vector3f.UNIT_X, 5f, 5f, 5f);
        body = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture meatTexture = assetManager.loadTexture(
                "snake_texture.jpg");
        meatTexture.setWrap(Texture.WrapMode.Repeat);
        mat.setTexture("ColorMap", meatTexture);
        body.setMaterial(mat);
        rootNode.attachChild(body);
    }

    public void snakeMovement(Vector3f position)
    {
        body.move(position);
    }
}
