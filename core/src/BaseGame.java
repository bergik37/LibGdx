import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
public abstract class BaseGame extends Game
{
    private static BaseGame game;
    public BaseGame()
    {
        game = this;
    }
    public static void setActiveScreen(BaseScreen s)
    {
        game.setScreen(s);
    }
    public void create()
    {
        // подготовка к нескольким классам / этапам приема дискретных входных данных
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor( im );
    }
}
