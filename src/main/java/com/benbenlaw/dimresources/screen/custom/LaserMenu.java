package com.benbenlaw.dimresources.screen.custom;

import com.benbenlaw.core.screen.SimpleAbstractContainerMenu;
import com.benbenlaw.core.screen.util.slot.InputSlot;
import com.benbenlaw.core.screen.util.slot.ResultSlot;
import com.benbenlaw.dimresources.block.entity.LaserBlockEntity;
import com.benbenlaw.dimresources.screen.DRMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


public class LaserMenu extends SimpleAbstractContainerMenu {
    protected LaserBlockEntity blockEntity;
    protected Level level;
    protected ContainerData data;
    protected Player player;
    protected BlockPos blockPos;

    public LaserMenu(int containerID, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerID, inventory, extraData.readBlockPos(), new SimpleContainerData(15));
    }

    public LaserMenu(int containerID, Inventory inventory, BlockPos blockPos, ContainerData data) {
        super(DRMenuTypes.LASER_MENU.get(), containerID, inventory, blockPos, 12);
        this.player = inventory.player;
        this.blockPos = blockPos;
        this.level = inventory.player.level();
        this.blockEntity = (LaserBlockEntity) this.level.getBlockEntity(blockPos);
        this.data = data;

        assert blockEntity != null;

        int[] inputX = {32, 104};
        int[] outputX = {80, 152};
        int[] yPositions = {17, 35, 53};

        for (int column = 0; column < 2; column++) {
            for (int row = 0; row < 3; row++) {
                int index = column * 3 + row;

                this.addSlot(new InputSlot(blockEntity.getItemHandler(), blockEntity.getItemHandler()::set, index, inputX[column], yPositions[row]));
                this.addSlot(new ResultSlot( blockEntity.getItemHandler(), blockEntity.getItemHandler()::set, index + 6, outputX[column],yPositions[row]));
            }
        }

        addDataSlots(data);
    }

    public boolean isCrafting(int slot) {
        return data.get(slot) > 0;
    }

    public int getScaledProgress(int slot) {

        int progress = this.data.get(slot);
        int maxProgress = this.data.get(slot + 6);

        int progressArrowSize = 24;

        return maxProgress != 0 && progress != 0
                ? progress * progressArrowSize / maxProgress
                : 0;
    }

    public boolean hasEnergy() {
        return data.get(13) > 14 ;
    }

    public int getEnergyFilled() {

        int progress = this.data.get(13);
        int maxProgress = this.data.get(14);  // Max Progress
        int progressArrowSize = 52; // This is the height in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
}
