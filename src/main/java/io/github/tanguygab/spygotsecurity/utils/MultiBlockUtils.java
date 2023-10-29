package io.github.tanguygab.spygotsecurity.utils;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.data.type.Chest.Type;

public class MultiBlockUtils {

    public static Block getSide(Block block) {
        if (block.getBlockData() instanceof org.bukkit.block.data.type.Chest data
                && block.getState() instanceof Chest state
                && state.getInventory().getHolder() instanceof DoubleChest chest) {
            Chest other = (Chest) (data.getType() == Type.RIGHT ? chest.getRightSide() : chest.getLeftSide());
            if (other != null) return other.getBlock();
        }
        return null;
    }

    public static void setSingle(Block... blocks) {
        for (Block block : blocks)
            if (block != null && block.getBlockData() instanceof org.bukkit.block.data.type.Chest data) {
                data.setType(Type.SINGLE);
                block.setBlockData(data);
            }
    }

}
