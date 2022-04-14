package fr.utln.jmonkey;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

import java.util.Vector;

/** Sample 7 - how to load an OgreXML model and play an animation,
 * using channels, a controller, and an AnimEventListener. */
public class RunGame extends SimpleApplication {
    Vector<Snake> snake = new Vector<Snake>();
    Vector<Food> food = new Vector<Food>();
    public static void main(String[] args) {
        RunGame app = new RunGame();
        app.start();
    }

    @Override
    public void simpleInitApp()
    {
        /** initialisation de la map du jeu**/
        initMap();

        /**fixer le point de vue en choisissant la location du camera**/
        cam.setLocation(new Vector3f(0, -300, 0));
        cam.lookAt(new Vector3f(0, 1, 0), Vector3f.UNIT_Y);

        inputManager.clearMappings();

        /**ajout des objets de Snake dans le vecteur**/
        snake.add(new Snake(rootNode, assetManager));
        snake.add(new Snake(rootNode, assetManager));
        snake.add(new Snake(rootNode, assetManager));
        
        lastTime = System.currentTimeMillis();

        initKeys();

        /** ajout du l'objet food dans le vecteur**/
        food.add(new Food(rootNode, assetManager));

    }

    /**cette fonction permet de creer un Box utilise pour la map de ce jeu**/
    public void createBox(Vector3f pos, float x, float y , float z)
    {
        Box b = new Box(pos, x,y,z);
        Geometry box = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);
        mat.getAdditionalRenderState().setWireframe(true);
        box.setMaterial(mat);
        rootNode.attachChild(box);
    }

    /** Cette fonction permet l'initialisation de la map **/
    public void initMap()
    {
        createBox(new Vector3f(100, 0, 0), 0, 100, 100);
        createBox(new Vector3f(-100, 0, 0), 0, 100, 100);

        createBox(new Vector3f(0, 100, 0), 100, 0, 100);
        createBox(new Vector3f(0, -100, 0), 100, 0, 100);

        createBox(new Vector3f(0, 0, 100), 100, 100, 0);
        createBox(new Vector3f(0, 0, -100), 100, 100, 0);
    }

    /**Cette fonction permet de definir les touches qui seront utilisees pendant le jeu**/
    public void initKeys()
    {   inputManager.addMapping("quit", new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("left", new KeyTrigger(keyInput.KEY_LEFT));
        inputManager.addMapping("up", new KeyTrigger(keyInput.KEY_UP));
        inputManager.addMapping("down", new KeyTrigger(keyInput.KEY_DOWN));
        inputManager.addMapping("right", new KeyTrigger(keyInput.KEY_RIGHT));
        inputManager.addMapping("top", new KeyTrigger(keyInput.KEY_W));
        inputManager.addMapping("bottom", new KeyTrigger(keyInput.KEY_S));

        inputManager.addListener(action, "quit","left","right","up","down","top","bottom");
    }

    public boolean quit, Left, Right, Up, Down, Top, Bottom;

    public void initControl()
    {
        quit = Left = Right = Up = Down = Top = Bottom = false;
    }

    public ActionListener action = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf)
        {
            if(isPressed)
            {
                initControl();
                if(name.equals("left"))
                {
                    Left = true;
                }
                else if(name.equals("right"))
                {
                    Right = true;
                }
                else if(name.equals("up"))
                {
                    Up = true;
                }
                else if(name.equals("down"))
                {
                    Down=true;
                }
                else if(name.equals("top"))
                {
                    Top=true;
                }
                else if(name.equals("bottom"))
                {
                    Bottom = true;
                }
                else if(name.equals("quit"))
                {
                    quit = true;
                    System.exit(1);
                }
            }
        }
    };

    long lastTime,currTime;

    @Override
    public void simpleUpdate(float tpf)
    {
        food.firstElement().food.rotate(0, 2*tpf, 0);
        currTime = System.currentTimeMillis();
        if((currTime-lastTime) > 300)
        {
            for(int i = snake.size()-1 ; i>0 ; i--)
            {
                snake.get(i).body.setLocalTranslation(snake.get(i-1).body.getLocalTranslation().clone());
            }
            lastTime = currTime;
            snakeHeadMovement();
        }
        mapControl();
        eat();
    }


    /**Cette focntion permet de verifier quand l'objet snake va manger l'objet food**/
    public void eat()
    {
        /** on verifie si la distance entre la tete de vecteur snake et le vecteur food est a 0**/
        if(snake.firstElement().body.getLocalTranslation().clone().distance(food.firstElement().position)==0)
        {
            /** on ajoute un objet snake dans le vecteur, on supprime le food et on ajoute un nouveau objet food*/
            snake.add(new Snake(rootNode, assetManager));
            snake.lastElement().body.setLocalTranslation(snake.firstElement().body.getLocalTranslation());
            rootNode.detachChild(food.firstElement().food);
            food.clear();
            food.add(new Food(rootNode, assetManager));
        }
    }

    /**cette fonction permet le controle du mouvement de snake*/
    public void snakeHeadMovement()
    {
        if(Left) {
            snake.firstElement().snakeMovement(new Vector3f(0, 0, -10));
        }
        else if(Right) {
            snake.firstElement().snakeMovement(new Vector3f(0, 0, 10));
        }
        else if(Up) {
            snake.firstElement().snakeMovement(new Vector3f(-10, 0, 0));
        }
        else if(Down) {
            snake.firstElement().snakeMovement(new Vector3f(10, 0, 0));

        }
        else if(Top) {
            snake.firstElement().snakeMovement(new Vector3f(0, -10, 0));
        }
        else if(Bottom) {
            snake.firstElement().snakeMovement(new Vector3f(0, 10, 0));
        }
    }

    /**Cette fonction permet le controle de la map, par exemple si le snake depasse la box(map) d'un cote il apparait de l'autre cote**/
    public void mapControl()
    {
        Vector3f pos = snake.firstElement().body.getLocalTranslation();

        if(snake.firstElement().body.getLocalTranslation().clone().z > 100)
        {
            snake.firstElement().body.setLocalTranslation(pos.x, pos.y, -100);
        }

        if(snake.firstElement().body.getLocalTranslation().clone().z < -100)
        {
            snake.firstElement().body.setLocalTranslation(pos.x, pos.y, 100);
        }

        if(snake.firstElement().body.getLocalTranslation().clone().y > 100)
        {
            snake.firstElement().body.setLocalTranslation(pos.x, -100, pos.z);
        }
        if(snake.firstElement().body.getLocalTranslation().clone().y < -100)
        {
            snake.firstElement().body.setLocalTranslation(pos.x, 100, pos.z);
        }

        if(snake.firstElement().body.getLocalTranslation().clone().x > 100)
        {
            snake.firstElement().body.setLocalTranslation(-100, pos.y, pos.z);
        }

        if(snake.firstElement().body.getLocalTranslation().clone().x < -100)
        {
            snake.firstElement().body.setLocalTranslation(100, pos.y, pos.z);
        }
    }

}