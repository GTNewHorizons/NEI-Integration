package tonius.neiintegration.mods.railcraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.API;
import mods.railcraft.api.crafting.ICokeOvenRecipe;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import tonius.neiintegration.RecipeHandlerBase;
import tonius.neiintegration.Utils;

public class RecipeHandlerCokeOven extends RecipeHandlerBase {

    private static Class<? extends GuiContainer> guiClass;

    @Override
    public void prepare() {
        guiClass = Utils.getClass("mods.railcraft.client.gui.GuiCokeOven");
        API.setGuiOffset(guiClass, -6, 11);
    }

    public class CachedCokeOvenRecipe extends CachedBaseRecipe {

        public List<PositionedStack> input;
        public List<PositionedStack> outputs = new ArrayList<>();
        public boolean hasFluidOutput;
        public int cookTime;

        public CachedCokeOvenRecipe(ICokeOvenRecipe recipe) {
            if (recipe.getInput() != null) {
                this.input = Collections.singletonList(new PositionedStack(recipe.getInput(), 21, 32));
            }
            if (recipe.getOutput() != null) {
                this.outputs.add(new PositionedStack(recipe.getOutput(), 67, 32));
            }
            if (recipe.getFluidOutput() != null) {
                this.hasFluidOutput = true;
                this.outputs.add(new PositionedStack.Fluid(recipe.getFluidOutput(), 95, 13, 48, 47, 64000));
            }
            this.cookTime = recipe.getCookTime();
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return this.input;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            return this.outputs;
        }
    }

    @Override
    public String getRecipeName() {
        return Utils.translate("railcraft.gui.coke.oven", false);
    }

    @Override
    public String getRecipeID() {
        return "railcraft.cokeoven";
    }

    @Override
    public String getGuiTexture() {
        return "railcraft:textures/gui/gui_coke_oven.png";
    }

    @Override
    public void loadTransferRects() {
        this.addTransferRect(39, 32, 22, 16);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return guiClass;
    }

    @Override
    public void drawBackground(int recipe) {
        this.changeToGuiTexture();
        GuiDraw.drawTexturedModalRect(10, 0, 5, 11, 137, 64);
    }

    @Override
    public void drawExtras(int recipe) {
        this.drawProgressBar(40, 32, 177, 61, 21, 16, 100, 0);
        this.drawProgressBar(21, 15, 176, 47, 14, 14, 100, 11);
        CachedCokeOvenRecipe crecipe = (CachedCokeOvenRecipe) this.arecipes.get(recipe);
        if (crecipe.hasFluidOutput) {
            this.changeToGuiTexture();
            GuiDraw.drawTexturedModalRect(95, 13, 176, 0, 48, 47);
        }
        GuiDraw.drawStringC(String.format(Utils.translate("ticks"), crecipe.cookTime), 64, 12, 0x372A1D, false);
    }

    @Override
    public void loadAllRecipes() {
        for (ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
            if (recipe == null) {
                continue;
            }
            this.arecipes.add(new CachedCokeOvenRecipe(recipe));
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        super.loadCraftingRecipes(result);
        for (ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
            if (recipe == null) {
                continue;
            }
            if (NEIServerUtils.areStacksSameType(result, recipe.getOutput())) {
                this.arecipes.add(new CachedCokeOvenRecipe(recipe));
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingred) {
        super.loadUsageRecipes(ingred);
        for (ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
            if (recipe == null) {
                continue;
            }
            if (Utils.areStacksSameTypeCraftingSafe(recipe.getInput(), ingred)) {
                CachedCokeOvenRecipe crecipe = new CachedCokeOvenRecipe(recipe);
                crecipe.setIngredientPermutation(crecipe.input, ingred);
                this.arecipes.add(crecipe);
            }
        }
    }

    @Override
    public void loadCraftingRecipes(FluidStack result) {
        for (ICokeOvenRecipe recipe : RailcraftCraftingManager.cokeOven.getRecipes()) {
            if (recipe == null) {
                continue;
            }
            if (Utils.areFluidsSameType(recipe.getFluidOutput(), result)) {
                this.arecipes.add(new CachedCokeOvenRecipe(recipe));
            }
        }
    }
}
