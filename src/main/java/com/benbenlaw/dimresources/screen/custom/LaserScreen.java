package com.benbenlaw.dimresources.screen.custom;

import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.DurationTooltip;
import com.benbenlaw.core.screen.util.FluidRenderingUtils;
import com.benbenlaw.dimresources.DimResources;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;

import java.util.List;

public class LaserScreen extends AbstractContainerScreen<LaserMenu> {

    private static final Identifier TEXTURE = DimResources.identifier("textures/gui/laser_gui.png");
    private static final Identifier PROGRESS_ARROW = Core.identifier("progress_arrow");
    private static final Identifier ENERGY_BAR = DimResources.identifier("energy_bar");


    public LaserScreen(LaserMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        int[][] arrowPositions = {{52, 17}, {52, 35}, {52, 53}, {124, 17}, {124, 35}, {124, 53}};

        for (int i = 0; i < 6; i++) {

            if (menu.isCrafting(i)) {
                int progress = menu.getScaledProgress(i);
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_ARROW, 24, 16, 0, 0,
                        x + arrowPositions[i][0], y + arrowPositions[i][1],progress + 1, 16);
            }
        }

        if (menu.hasEnergy()) {
            int currentEnergyHeight = menu.getEnergyFilled();
            int topOffset = 52 - currentEnergyHeight;

            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED,ENERGY_BAR, 16, 52,0, topOffset, x + 8, y + topOffset + 17, 16, currentEnergyHeight);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        //DurationTooltip.renderDurationTooltip(guiGraphics, mouseX, mouseY, x, y, 161, 5, menu.data.get(0), menu.data.get(1));
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        int barX = x + 8;
        int barY = y + 17;
        int barWidth = 16;
        int barHeight = 52;

        if (mouseX >= barX && mouseX <= barX + barWidth && mouseY >= barY && mouseY <= barY + barHeight) {
            int currentEnergy = menu.data.get(13);
            int maxEnergy = menu.data.get(14);

            Component text = Component.literal("Energy: "+currentEnergy+" / "+maxEnergy+" FE");
            graphics.setTooltipForNextFrame(this.font, text, mouseX, mouseY);

        }

    }
}