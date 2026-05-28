package com.benbenlaw.dimresources.integration.jei;

import com.benbenlaw.core.util.MouseUtil;
import com.benbenlaw.dimresources.DimResources;
import com.benbenlaw.dimresources.block.DRBlocks;
import com.benbenlaw.dimresources.event.ClientRecipeCache;
import com.benbenlaw.dimresources.item.DRDataComponent;
import com.benbenlaw.dimresources.item.DRItems;
import com.benbenlaw.dimresources.recipe.custom.LaserRecipe;
import com.benbenlaw.dimresources.recipe.custom.WeightedItemStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawablesView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.gui.widgets.IScrollGridWidget;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.ICraftingStationLookup;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStackTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class LaserRecipeCategory implements IRecipeCategory<LaserRecipe> {

    public static final Identifier TEXTURE = DimResources.identifier("textures/gui/laser_jei.png");
    public static final IRecipeType<LaserRecipe> RECIPE_TYPE = IRecipeType.create(DimResources.identifier("laser"), LaserRecipe.class);

    private final int width = 137;
    private final int height = 20;
    private final IDrawable icon;

    @Override
    public @Nullable Identifier getIdentifier(LaserRecipe recipe) {
        return ClientRecipeCache.getCachedLaserRecipes().stream()
                .filter(r -> r.equals(recipe))
                .findFirst()
                .map(r -> {
                    // Find the corresponding ID in the cache map
                    for (Map.Entry<Identifier, LaserRecipe> entry : ClientRecipeCache.cachedLaserRecipes.entrySet()) {
                        if (entry.getValue().equals(recipe)) {
                            return entry.getKey();
                        }
                    }
                    return null;
                })
                .orElse(null);
    }

    public LaserRecipeCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(DRBlocks.LASER.get()));
    }

    @Override
    public @NotNull IRecipeType<LaserRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jei.dimensionalresources.laser");
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LaserRecipe recipe, IFocusGroup focusGroup) {
        int centerX = 48;
        int centerY = 2;
        int slotWidth = 18;

        ItemStack plantLocator = new ItemStack(DRItems.PLANET_LOCATOR.get());
        plantLocator.set(DRDataComponent.PLANET, recipe.planet());

        builder.addSlot(RecipeIngredientRole.INPUT, 1, 2).add(plantLocator)
                .setBackground(DRJEIPlugin.slotDrawable, -1, -1);

        List<WeightedItemStack> items = recipe.output();
        int totalItems = items.size();
        int totalWeight = items.stream().mapToInt(WeightedItemStack::getWeight).sum();

        for (int i = 0; i < totalItems; i++) {
            int displayIndex = Math.min(i, 4);
            int xPos = centerX + (displayIndex * slotWidth);

            final int finalIndex = i;

            builder.addSlot(RecipeIngredientRole.OUTPUT, xPos, centerY)
                    .add(items.get(i).getStack())
                    .addRichTooltipCallback((slot, tooltip) -> {
                        float calculatedWeight = ((float) items.get(finalIndex).getWeight() / totalWeight) * 100;
                        tooltip.add(Component.translatable("jei.dimresource.weight", (int) calculatedWeight).withStyle(ChatFormatting.GOLD));
                    })
                    .setBackground(DRJEIPlugin.slotDrawable, -1, -1);
        }
    }



    @Override
    public void getTooltip(ITooltipBuilder tooltip, LaserRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (MouseUtil.isMouseAboveArea((int) mouseX, (int) mouseY, 19, 1, 0, 0, 28, 18)) {
            int duration = recipe.duration();
            String timeString = String.valueOf(duration);
            tooltip.add(Component.translatable("tooltip.core.ticks", timeString));
            tooltip.add(Component.translatable("tooltip.dimresources.laser_level", recipe.laserLevel()));
            tooltip.add(Component.translatable("tooltip.dimresources.rf_per_tick", recipe.rfPerTick()));
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, LaserRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotDrawablesView recipeSlots = builder.getRecipeSlots();
        List<IRecipeSlotDrawable> outputItems = recipeSlots.getSlots(RecipeIngredientRole.OUTPUT);

        if (outputItems.size() > 5) {
            IScrollGridWidget triggersGrid = builder.addScrollGridWidget(outputItems, 2, 1);
            triggersGrid.setPosition(47, 1);
        }
        builder.addAnimatedRecipeArrow(200).setPosition(21, 2);
    }

    @Override
    public void draw(LaserRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, 0, 0, 0, 0, width, height, width, height);
    }
}
