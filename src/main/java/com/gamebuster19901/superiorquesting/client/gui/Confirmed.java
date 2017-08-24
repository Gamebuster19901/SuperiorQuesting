package com.gamebuster19901.superiorquesting.client.gui;

import java.util.Iterator;
import java.util.Map;

import com.gamebuster19901.superiorquesting.Main;
import com.gamebuster19901.superiorquesting.common.Debuggable;
import com.gamebuster19901.superiorquesting.common.item.ItemHeartCanister;
import com.gamebuster19901.superiorquesting.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.OreDictionary;

public final class Confirmed implements Debuggable{
	private Minecraft mc = Minecraft.getMinecraft();
	private long startTime;
	private long finishTime;
	private boolean	shouldRender = false;
	public void renderConfirmedOverlay(ScaledResolution scaledRes){
		GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        mc.getTextureManager().bindTexture(new ResourceLocation(Main.MODID + ":textures/item/misc/confirmed.png"));
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double)scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
	}
	
	@SubscribeEvent
	public void onGameOverlayRender (RenderGameOverlayEvent.Post e){
		if(e.getType() == ElementType.HELMET){
			if (shouldRender){
				renderConfirmedOverlay(e.getResolution());
			}
		}
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e){
		EntityPlayerSP p = mc.player;
		if (p != null){
			for(int i = 0; i < mc.player.inventory.getSizeInventory(); i++){
				ItemStack slotItem = mc.player.inventory.getStackInSlot(i);
				ItemStack target = new ItemStack(ItemHeartCanister.ITEM, 3, 2);
				if(ItemStack.areItemStacksEqual(slotItem, target) && slotItem.getMetadata() == target.getMetadata()){
					shouldRender = true;
					SoundManager manager = ReflectionHelper.getPrivateValue(SoundHandler.class, mc.getSoundHandler(),  "sndManager");
					Map playingSounds = ReflectionHelper.getPrivateValue(SoundManager.class, manager, "playingSounds");
					Iterator it = playingSounds.keySet().iterator();
					boolean play = true;
					while(it.hasNext()){
						PositionedSound s = (PositionedSound)playingSounds.get(it.next());
						ResourceLocation loc = ReflectionHelper.getPrivateValue(PositionedSound.class, s, "positionedSoundLocation");
						if((loc.getResourceDomain() + ":" + loc.getResourcePath()).equals(Main.MODID + ":confirmed")){
							play = false;
						}
						else if ((loc.getResourceDomain() + ":" + loc.getResourcePath()).contains("minecraft:music")){
							mc.getSoundHandler().stopSound(s);
						}
						debug(loc.getResourceDomain() + ":" + loc.getResourcePath());
					}
					if (play){
						p.world.playSound(p.posX, p.posY, p.posZ, ClientProxy.CONFIRMED, SoundCategory.MUSIC, 99999, 1, false);
					}
					return;
				}
			}
			/*
			 * Some debug things
			 */
			if(Debuggable.debug){
				if (!p.getHeldItemMainhand().isEmpty()){
					int[] ids = OreDictionary.getOreIDs(p.getHeldItemMainhand());
					for(int id : ids){
						debug(OreDictionary.getOreName(id));
					}
					if(ids.length == 0){
						debug("none");
					}
				}
			}
		}
		shouldRender = false;
	}
}
