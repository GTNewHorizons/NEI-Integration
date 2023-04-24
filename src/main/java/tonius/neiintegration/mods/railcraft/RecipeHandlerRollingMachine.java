package tonius.neiintegration.mods.railcraft;

import net.minecraft.client.gui.inventory.GuiContainer;

import codechicken.lib.gui.GuiDraw;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;

public abstract class RecipeHandlerRollingMachine extends RecipeHandlerBase {

    private static Class<? extends GuiContainer> guiClass;

    @Override
    public void prepare() {
        guiClass = Utils.getClass("mods.railcraft.client.gui.GuiRollingMachine");
    }

    @Override
    public String getRecipeID() {
        return "railcraft.rollingmachine";
    }

    @Override
    public String getRecipeName() {
        return Utils.translate("tile.railcraft.machine.alpha.rolling.machine.name", false);
    }

    @Override
    public String getGuiTexture() {
        return "railcraft:textures/gui/gui_rolling.png";
    }

    @Override
    public void loadTransferRects() {
        this.addTransferRect(84, 39, 24, 8);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }

    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(0, 2, 5, 11, 150, 65);
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(84, 36, 176, 0, 25, 12, 60, 0);
    }

}
