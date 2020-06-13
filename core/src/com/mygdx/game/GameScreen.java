package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.npc.Ghost;
import com.mygdx.game.npc.cutscene.CutsceneManager;
import com.mygdx.game.npc.cutscene.CutsceneParser;
import com.mygdx.game.obj.House;
import com.mygdx.game.obj.panels.BottomPanel;
import com.mygdx.game.obj.panels.DialogPanel;
import com.mygdx.game.obj.panels.MonologPanel;
import com.mygdx.game.obj.panels.Panel;
import com.mygdx.game.save.SaveParser;
import com.mygdx.game.shit.level.LevelManager;
import com.mygdx.game.utils.FileUtils;


import org.json.JSONObject;

public class GameScreen implements Screen {

    public final MyGame myGame;
    public boolean reloadTextures = true;
    public OrthographicCamera camera;
    public boolean isSave;
    private SpriteBatch batch;
    private SpriteBatch menuBatch;
    private SpriteBatch panelBatch;
    private ShapeRenderer shapeRenderer;
    private Sprite inventarSprite;
    private House house;

    private Panel panels[];

    public GameScreen(MyGame myGame)
    {   //ставим скрин игры
        this.myGame = myGame;
        reloadTextures = true;
    }

    public JSONObject getJSONObjectFromFile(String name)
    {
        String body = FileUtils.getBody("json/" + name + ".json", true);
        return new JSONObject(body);
    }

    private JSONObject getJSONObjectFromLocalFile(String filepath) {
        FileHandle handle = Gdx.files.local(filepath);
        String body = handle.readString();
        return new JSONObject(body);
    }

    @Override
    public void show() {  //показывает на экране
        if (reloadTextures) {

            TextureManager.getInstance().init("all/new.atlas");

            house = new House(getJSONObjectFromFile(Const.JSON), myGame, new House.OnEndGame() {
                @Override
                public void onEnd() {
                    dispose();
                    myGame.goToMenuScreen();
                    FileHandle handle = Gdx.files.local(Const.SAVE);
                    if(handle.exists())
                        handle.delete();
                }
            });

            if (DebugOptions.PLAY_BACKGROUND_MUSIC)
                MusicManager.playMusic(Const.BACKGROUND_MUSIC_FILEPATH, true);
            FileHandle handle = Gdx.files.local(Const.SAVE);
            if (isSave && handle.exists()) {
                SaveParser.parse(house, getJSONObjectFromLocalFile(Const.SAVE));
                CutsceneManager.getInstance().init(CutsceneParser.parse("quests/cutscene.xml"), house);
            } else {
                CutsceneManager.getInstance().init(CutsceneParser.parse("quests/cutscene.xml"), house);
                LevelManager.getInstance().startLevel(house, "quests/level1");
            }

            batch = new SpriteBatch();

            camera = new OrthographicCamera(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
            camera.translate(Const.SCREEN_WIDTH / 2f, Const.SCREEN_HEIGHT / 2f);
            camera.update();

            menuBatch = new SpriteBatch();
            inventarSprite = TextureManager.getInstance().sprite(Const.INVENTAR_IMAGE_FILEPATH);
            inventarSprite.setBounds(0 * Const.W, 7f * Const.H, 2.22f * Const.H, 1.99f * Const.H);

            panelBatch = new SpriteBatch();
            shapeRenderer = new ShapeRenderer();

            panels = new Panel[] {
                    BottomPanel.getInstance(),
                    DialogPanel.getInstance(),
                    MonologPanel.getInstance()
            };
            if(isSave) {
                for (Ghost ghost : house.getGhosts()) {
                    if(ghost.isBad() &&  house.getCurrentRoom() == ghost.getRoom() && !ghost.isAgring()){
                        DialogPanel.getInstance().startDialog(ghost);
                        break;
                    }
                }
            }
        } else {
            reloadTextures = true;
        }
        Gdx.input.setInputProcessor(new InputListener(this, house));
        Gdx.input.setCatchBackKey(true);
    }


    @Override
    public void render(float delta) { //обновляем каждую секунду
        house.update(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        house.draw(batch, delta);
        batch.end();

        menuBatch.begin();
        inventarSprite.draw(menuBatch);
        menuBatch.end();

        for (Panel panel : panels) {
            if(panel.isPanelVisible()) {
                panel.draw(shapeRenderer, panelBatch);
                break;
            }
        }

        CutsceneManager.getInstance().update(delta);

    }

    public boolean menuBatchClick(float x, float y) { //кликаем на меню инвентаря
        if (inventarSprite.getBoundingRectangle().contains(x, y)) {
            MusicManager.pauseSound();
            myGame.setScreen(new InventarScreen(myGame, house));
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        System.out.println();
    }

    @Override
    public void pause() {
        System.out.println();
    }

    @Override
    public void resume() {
        System.out.println();

    }

    @Override
    public void hide() {
        System.out.println();
    }

    @Override
    public void dispose() { //удаление,отчистка
        batch.dispose();
        panelBatch.dispose();
        menuBatch.dispose();
        shapeRenderer.dispose();
        inventarSprite.getTexture().dispose();
        house.dispose();
        MusicManager.dispose();
        for (Panel panel : panels) {
            panel.dispose();
        }
        reloadTextures = true;

    }

}
