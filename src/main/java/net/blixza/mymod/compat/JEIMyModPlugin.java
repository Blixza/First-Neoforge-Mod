package net.blixza.mymod.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.blixza.mymod.MyMod;
import net.blixza.mymod.recipe.GrowthChamberRecipe;
import net.blixza.mymod.recipe.GrowthChamberRecipeInput;
import net.blixza.mymod.recipe.ModRecipes;
import net.blixza.mymod.screen.custom.GrowthChamberScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.List;

@JeiPlugin
public class JEIMyModPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(MyMod.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GrowthChamberRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()
        ));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();

        List<GrowthChamberRecipe> growthChamberRecipes = manager
                .getAllRecipesFor(ModRecipes.GROWTH_CHAMBER_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(GrowthChamberRecipeCategory.GROWTH_CHAMBER_RECIPE_RECIPE_TYPE, growthChamberRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GrowthChamberScreen.class, 74, 30, 22, 20,
                GrowthChamberRecipeCategory.GROWTH_CHAMBER_RECIPE_RECIPE_TYPE);
    }
}
