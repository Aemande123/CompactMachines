package org.dave.compactmachines3.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.dave.compactmachines3.CompactMachines3;
import org.dave.compactmachines3.misc.ConfigurationHandler;
import org.dave.compactmachines3.misc.CreativeTabCompactMachines3;
import org.dave.compactmachines3.reference.GuiIds;
import org.dave.compactmachines3.world.WorldSavedDataMachines;
import org.dave.compactmachines3.world.tools.StructureTools;
import org.dave.compactmachines3.world.tools.TeleportationTools;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPersonalShrinkingDevice extends ItemBase {
    public ItemPersonalShrinkingDevice() {
        super();

        this.setCreativeTab(CreativeTabCompactMachines3.COMPACTMACHINES3_TAB);
        this.setMaxStackSize(1);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if(hand == EnumHand.OFF_HAND) {
            return new ActionResult(EnumActionResult.FAIL, stack);
        }

        if(world.provider.getDimension() != ConfigurationHandler.Settings.dimensionId) {
            player.openGui(CompactMachines3.instance, GuiIds.PSD_GUIDE.ordinal(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        }

        if(!world.isRemote && world.provider.getDimension() == ConfigurationHandler.Settings.dimensionId && player instanceof EntityPlayerMP) {
            EntityPlayerMP serverPlayer = (EntityPlayerMP)player;

            if(player.isSneaking()) {
                int coords = StructureTools.getCoordsForPos(player.getPosition());
                Vec3d pos = player.getPositionVector();
                WorldSavedDataMachines.INSTANCE.addSpawnPoint(coords, pos.x, pos.y, pos.z);

                TextComponentTranslation tc = new TextComponentTranslation("item.compactmachines3.psd.spawnpoint_set");
                tc.getStyle().setColor(TextFormatting.GREEN);
                player.sendStatusMessage(tc, false);

                return new ActionResult(EnumActionResult.SUCCESS, stack);
            }

            TeleportationTools.teleportPlayerOutOfMachine(serverPlayer);
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        }

        return new ActionResult(EnumActionResult.FAIL, stack);
    }



}
