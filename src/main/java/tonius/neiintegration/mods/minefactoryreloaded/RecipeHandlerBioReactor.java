package tonius.neiintegration.mods.minefactoryreloaded;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.api.IFactoryPlantable;
import tonius.neiintegration.PositionedFluidTank;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;

public class RecipeHandlerBioReactor extends RecipeHandlerBase {

    private static Map<Item, IFactoryPlantable> plantables;

    @Override
    public void prepare() {
        plantables = MFRRegistry.getPlantables();
    }

    public class CachedBioReactorRecipe extends CachedBaseRecipe {

        public PositionedStack input;
        public PositionedFluidTank biofuelOutput;

        public CachedBioReactorRecipe(ItemStack input) {
            this.input = new PositionedStack(input, 9, 6);
            this.biofuelOutput = new PositionedFluidTank(
                    FluidRegistry.getFluidStack("biofuel", 4000),
                    4000,
                    new Rectangle(121, 2, 16, 60),
                    RecipeHandlerBioReactor.this.getGuiTexture(),
                    new Point(176, 0));
            this.biofuelOutput.showAmount = false;
        }

        @Override
        public PositionedStack getResult() {
            this.randomRenderPermutation(this.input, RecipeHandlerBioReactor.this.cycleticks / 20);
            return this.input;
        }

        @Override
        public PositionedFluidTank getFluidTank() {
            return this.biofuelOutput;
        }

    }

    @Override
    public String getRecipeName() {
        return Utils.translate("tile.mfr.machine.bioreactor.name", false);
    }

    @Override
    public String getRecipeID() {
        return "minefactoryreloaded.bioreactor";
    }

    @Override
    public String getGuiTexture() {
        return MineFactoryReloadedCore.guiFolder + "bioreactor.png";
    }

    @Override
    public void loadTransferRects() {
        this.addTransferRect(80, 25, 22, 15);
    }

    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(8, 5, 7, 14, 54, 54);
        GuiDraw.drawTexturedModalRect(120, 0, 131, 13, 38, 65);
        this.changeToOverlayTexture();
        GuiDraw.drawTexturedModalRect(80, 25, 0, 0, 22, 15);
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(139, 0, 176, 58, 8, 62, 0.3F, 3);
        this.drawProgressBar(149, 0, 185, 58, 8, 62, 120, 11);
    }

    @Override
    public List<String> provideTooltip(GuiRecipe<?> guiRecipe, List<String> currenttip, CachedBaseRecipe crecipe,
            Point relMouse) {
        super.provideTooltip(guiRecipe, currenttip, crecipe, relMouse);
        if (new Rectangle(139, 2, 8, 60).contains(relMouse)) {
            currenttip.add(Utils.translate("handler.bioreactor.efficiency"));
            currenttip.add(EnumChatFormatting.GRAY + Utils.translate("handler.bioreactor.efficiency.1"));
            currenttip.add(EnumChatFormatting.GRAY + Utils.translate("handler.bioreactor.efficiency.2"));
            currenttip.add(EnumChatFormatting.GRAY + Utils.translate("handler.bioreactor.efficiency.3"));
        }
        return currenttip;
    }

    @Override
    public void loadAllRecipes() {
        for (Item i : plantables.keySet()) {
            ItemStack plantable = new ItemStack(i, 1, OreDictionary.WILDCARD_VALUE);
            if (plantable != null && plantables.get(i).canBePlanted(plantable, true)) {
                this.arecipes.add(new CachedBioReactorRecipe(plantable));
            }
        }
    }

    @Override
    public void loadCraftingRecipes(FluidStack result) {
        if (result.getFluid().getName() != null && result.getFluid().getName().equals("biofuel")) {
            this.loadAllRecipes();
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        for (Item i : plantables.keySet()) {
            ItemStack plantable = new ItemStack(i, 1, ingredient.getItemDamage());
            if (ingredient.getItem() == i && plantables.get(i).canBePlanted(plantable, true)) {
                this.arecipes.add(new CachedBioReactorRecipe(plantable));
            }
        }
    }

}
