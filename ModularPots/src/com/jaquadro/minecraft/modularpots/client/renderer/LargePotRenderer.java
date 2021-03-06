package com.jaquadro.minecraft.modularpots.client.renderer;

import com.jaquadro.minecraft.modularpots.block.BlockLargePot;
import com.jaquadro.minecraft.modularpots.client.ClientProxy;
import com.jaquadro.minecraft.modularpots.tileentity.TileEntityLargePot;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import static com.jaquadro.minecraft.modularpots.block.BlockLargePot.Direction;
import static com.jaquadro.minecraft.modularpots.block.BlockLargePot.Direction.*;

public class LargePotRenderer implements ISimpleBlockRenderingHandler
{
    private float[] baseColor = new float[3];
    private float[] activeRimColor = new float[3];
    private float[] activeInWallColor = new float[3];
    private float[] activeBottomColor = new float[3];
    private float[] activeSubstrateColor = new float[3];

    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockLargePot))
            return;

        renderInventoryBlock((BlockLargePot) block, metadata, modelId, renderer);
    }

    private void renderInventoryBlock (BlockLargePot block, int metadata, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;

        int damage = metadata;
        metadata &= 15;

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);

        GL11.glRotatef(90, 0, 1, 0);
        GL11.glTranslatef(-.5f, -.5f, -.5f);

        // Bottom
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, -1, 0);
        renderer.renderFaceYNeg(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
        tessellator.draw();

        // Sides
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 0, -1);
        renderer.renderFaceZNeg(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 0, 1);
        renderer.renderFaceZPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1, 0, 0);
        renderer.renderFaceXNeg(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1, 0, 0);
        renderer.renderFaceXPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();

        boolean blendEnabled = GL11.glIsEnabled(GL11.GL_BLEND);
        if (!blendEnabled)
            GL11.glEnable(GL11.GL_BLEND);

        // Overlay
        if ((damage & 0xFF00) != 0) {
            IIcon icon = block.getOverlayIcon((damage >> 8) & 255);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0, 0, -1);
            renderer.renderFaceZNeg(block, 0, 0, 0, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0, 0, 1);
            renderer.renderFaceZPos(block, 0, 0, 0, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1, 0, 0);
            renderer.renderFaceXNeg(block, 0, 0, 0, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1, 0, 0);
            renderer.renderFaceXPos(block, 0, 0, 0, icon);
            tessellator.draw();
        }

        if (!blendEnabled)
            GL11.glDisable(GL11.GL_BLEND);

        // Top Lip
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 1, 0);

        float dim = .0625f;
        renderer.setRenderBounds(0, 0, 0, 1, 1, dim);
        renderer.renderFaceYPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        renderer.setRenderBounds(0, 0, 1 - dim, 1, 1, 1);
        renderer.renderFaceYPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        renderer.setRenderBounds(0, 0, 0, dim, 1, 1);
        renderer.renderFaceYPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        renderer.setRenderBounds(1 - dim, 0, 0, 1, 1, 1);
        renderer.renderFaceYPos(block, 0, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));

        tessellator.draw();

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);

        // Interior Sides
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 0, -1);
        renderer.renderFaceZNeg(block, 0, 0, 1 - dim, renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 0, 1);
        renderer.renderFaceZPos(block, 0, 0, dim - 1, renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1, 0, 0);
        renderer.renderFaceXNeg(block, 1 - dim, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1, 0, 0);
        renderer.renderFaceXPos(block, dim - 1, 0, 0, renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
        tessellator.draw();

        // Interior Bottom
        tessellator.startDrawingQuads();
        tessellator.setNormal(0, 1, 0);
        renderer.renderFaceYPos(block, 0, dim - 1, 0, renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
        tessellator.draw();

        GL11.glTranslatef(.5f, .5f, .5f);
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockLargePot))
            return false;

        try {
            if (ClientProxy.renderPass == 0)
                return renderWorldBlockPass0(world, x, y, z, (BlockLargePot) block, modelId, renderer);
            else if (ClientProxy.renderPass == 1)
                return renderWorldBlockPass1(world, x, y, z, (BlockLargePot) block, modelId, renderer);
            }
        catch (Exception e) { }

        return false;
    }

    private void renderEmptyPlane (Block block, int x, int y, int z, RenderBlocks renderer) {
        renderer.setRenderBounds(0, 0, 0, 0, 0, 0);
        renderer.renderFaceYNeg(block, x, y, z, Blocks.hardened_clay.getIcon(0, 0));
    }

    private boolean renderWorldBlockPass1 (IBlockAccess world, int x, int y, int z, BlockLargePot block, int modelId, RenderBlocks renderer) {
        TileEntityLargePot tileEntity = block.getTileEntity(world, x, y, z);
        if (tileEntity == null) {
            renderEmptyPlane(block, x, y, z, renderer);
            return true;
        }

        Tessellator tessellator = Tessellator.instance;
        IIcon icon = block.getOverlayIcon(tileEntity.getCarving());
        boolean didRender = false;

        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);

        if (icon != null) {
            tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
            tessellator.setColorOpaque_F(1f, 1f, 1f);

            boolean connectZNeg = block.isCompatibleNeighbor(world, x, y, z, 0, -1);
            boolean connectZPos = block.isCompatibleNeighbor(world, x, y, z, 0, 1);
            boolean connectXNeg = block.isCompatibleNeighbor(world, x, y, z, -1, 0);
            boolean connectXPos = block.isCompatibleNeighbor(world, x, y, z, 1, 0);

            if (!connectXNeg)
                renderer.renderFaceXNeg(block, x, y, z, icon);
            if (!connectXPos)
                renderer.renderFaceXPos(block, x, y, z, icon);
            if (!connectZNeg)
                renderer.renderFaceZNeg(block, x, y, z, icon);
            if (!connectZPos)
                renderer.renderFaceZPos(block, x, y, z, icon);

            didRender = !(connectXNeg && connectXPos && connectZNeg && connectZPos);
        }

        if (tileEntity.getSubstrate() instanceof ItemBlock) {
            Block substrate = Block.getBlockFromItem(tileEntity.getSubstrate());
            int substrateData = tileEntity.getSubstrateData();

            if (substrate == Blocks.water) {
                calculateBaseColor(activeSubstrateColor, substrate.getBlockColor());
                scaleColor(activeSubstrateColor, activeSubstrateColor, .9f);
                setTessellatorColor(tessellator, activeSubstrateColor);

                IIcon substrateIcon = renderer.getBlockIconFromSideAndMetadata(substrate, 1, substrateData);
                renderer.renderFaceYPos(block, x, y - .0625f, z, substrateIcon);

                didRender = true;
            }
        }

        if (!didRender)
            renderEmptyPlane(block, x, y, z, renderer);

        return true;
    }

    private boolean renderWorldBlockPass0 (IBlockAccess world, int x, int y, int z, BlockLargePot block, int modelId, RenderBlocks renderer) {
        renderer.renderStandardBlock(block, x, y, z);

        int data = world.getBlockMetadata(x, y, z);

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        IIcon icon = renderer.getBlockIconFromSideAndMetadata(block, 1, data);

        calculateBaseColor(baseColor, block.colorMultiplier(world, x, y, z));
        scaleColor(activeRimColor, baseColor, .8f);
        scaleColor(activeInWallColor, baseColor, .7f);
        scaleColor(activeBottomColor, baseColor, .6f);

        setTessellatorColor(tessellator, activeRimColor);

        float unit = 0.0625f;
        boolean connectZNeg = block.isCompatibleNeighbor(world, x, y, z, 0, -1);
        boolean connectZPos = block.isCompatibleNeighbor(world, x, y, z, 0, 1);
        boolean connectXNeg = block.isCompatibleNeighbor(world, x, y, z, -1, 0);
        boolean connectXPos = block.isCompatibleNeighbor(world, x, y, z, 1, 0);
        boolean connectZNegXNeg = block.isCompatibleNeighbor(world, x, y, z, -1, -1);
        boolean connectZNegXPos = block.isCompatibleNeighbor(world, x, y, z, 1, -1);
        boolean connectZPosXNeg = block.isCompatibleNeighbor(world, x, y, z, -1, 1);
        boolean connectZPosXPos = block.isCompatibleNeighbor(world, x, y, z, 1, 1);

        if (!connectZNeg) {
            renderer.setRenderBounds(0, 0, 0, 1, 1, unit);
            renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if (!connectZPos) {
            renderer.setRenderBounds(0, 0, 1 - unit, 1, 1, 1);
            renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if (!connectXNeg) {
            renderer.setRenderBounds(0, 0, 0, unit, 1, 1);
            renderer.renderFaceYPos(block, x, y, z, icon);
        }
        if (!connectXPos) {
            renderer.setRenderBounds(1 - unit, 0, 0, 1, 1, 1);
            renderer.renderFaceYPos(block, x, y, z, icon);
        }

        // Render interior corners
        if (connectZNeg && connectXPos && !connectZNegXPos)
            renderCorner(block, renderer, NorthEast, x, y, z, icon);
        if (connectZNeg && connectXNeg && !connectZNegXNeg)
            renderCorner(block, renderer, NorthWest, x, y, z, icon);
        if (connectZPos && connectXPos && !connectZPosXPos)
            renderCorner(block, renderer, SouthEast, x, y, z, icon);
        if (connectZPos && connectXNeg && !connectZPosXNeg)
            renderCorner(block, renderer, SouthWest, x, y, z, icon);

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);

        setTessellatorColor(tessellator, activeInWallColor);

        if (!connectXNeg)
            renderer.renderFaceXPos(block, x - 1 + unit, y, z, icon);
        if (!connectXPos)
            renderer.renderFaceXNeg(block, x + 1 - unit, y, z, icon);
        if (!connectZNeg)
            renderer.renderFaceZPos(block, x, y, z - 1 + unit, icon);
        if (!connectZPos)
            renderer.renderFaceZNeg(block, x, y, z + 1 - unit, icon);

        TileEntityLargePot tileEntity = block.getTileEntity(world, x, y, z);

        if (tileEntity != null && tileEntity.getSubstrate() instanceof ItemBlock) {
            Block substrate = Block.getBlockFromItem(tileEntity.getSubstrate());
            int substrateData = tileEntity.getSubstrateData();

            if (substrate != Blocks.water) {
                calculateBaseColor(activeSubstrateColor, substrate.getBlockColor());
                scaleColor(activeSubstrateColor, activeSubstrateColor, .8f);
                setTessellatorColor(tessellator, activeSubstrateColor);

                IIcon substrateIcon = renderer.getBlockIconFromSideAndMetadata(substrate, 1, substrateData);
                renderer.renderFaceYPos(block, x, y - unit, z, substrateIcon);
            }
            else {
                setTessellatorColor(tessellator, activeBottomColor);
                renderer.renderFaceYPos(block, x, y - 1 + unit, z, block.getBottomIcon(data));
            }
        }
        else {
            setTessellatorColor(tessellator, activeBottomColor);
            renderer.renderFaceYPos(block, x, y - 1 + unit, z, block.getBottomIcon(data));
        }

        return true;
    }

    private void renderCorner (BlockLargePot block, RenderBlocks renderer, Direction direction, int x, int y, int z, IIcon icon) {
        Tessellator tessellator = Tessellator.instance;
        float dim = .0625f;

        switch (direction) {
            case NorthEast:
                block.setBlockBounds(1 - dim, 0, 0, 1, 1, dim);
                renderer.setRenderBoundsFromBlock(block);
                setTessellatorColor(tessellator, activeRimColor);
                renderer.renderFaceYPos(block, x, y, z, icon);
                setTessellatorColor(tessellator, activeInWallColor);
                renderer.renderFaceXNeg(block, x, y, z, icon);
                renderer.renderFaceZPos(block, x, y, z, icon);
                break;
            case NorthWest:
                block.setBlockBounds(0, 0, 0, dim, 1, dim);
                renderer.setRenderBoundsFromBlock(block);
                setTessellatorColor(tessellator, activeRimColor);
                renderer.renderFaceYPos(block, x, y, z, icon);
                setTessellatorColor(tessellator, activeInWallColor);
                renderer.renderFaceXPos(block, x, y, z, icon);
                renderer.renderFaceZPos(block, x, y, z, icon);
                break;
            case SouthEast:
                block.setBlockBounds(1 - dim, 0, 1 - dim, 1, 1, 1);
                renderer.setRenderBoundsFromBlock(block);
                setTessellatorColor(tessellator, activeRimColor);
                renderer.renderFaceYPos(block, x, y, z, icon);
                setTessellatorColor(tessellator, activeInWallColor);
                renderer.renderFaceXNeg(block, x, y, z, icon);
                renderer.renderFaceZNeg(block, x, y, z, icon);
                break;
            case SouthWest:
                block.setBlockBounds(0, 0, 1 - dim, dim, 1, 1);
                renderer.setRenderBoundsFromBlock(block);
                setTessellatorColor(tessellator, activeRimColor);
                renderer.renderFaceYPos(block, x, y, z, icon);
                setTessellatorColor(tessellator, activeInWallColor);
                renderer.renderFaceXPos(block, x, y, z, icon);
                renderer.renderFaceZNeg(block, x, y, z, icon);
                break;
        }

        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
    }

    private void calculateBaseColor (float[] target, int color) {
        float r = (float)(color >> 16 & 255) / 255f;
        float g = (float)(color >> 8 & 255) / 255f;
        float b = (float)(color & 255) / 255f;

        if (EntityRenderer.anaglyphEnable) {
            float gray = (r * 30f + g * 59f + b * 11f) / 100f;
            float rg = (r * 30f + g * 70f) / 100f;
            float rb = (r * 30f + b * 70f) / 100f;

            r = gray;
            g = rg;
            b = rb;
        }

        target[0] = r;
        target[1] = g;
        target[2] = b;
    }

    private void scaleColor (float[] target, float[] source, float scale) {
        target[0] = source[0] * scale;
        target[1] = source[1] * scale;
        target[2] = source[2] * scale;
    }

    private void setTessellatorColor (Tessellator tessellator, float[] color) {
        tessellator.setColorOpaque_F(color[0], color[1], color[2]);
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return true;
    }

    @Override
    public int getRenderId () {
        return ClientProxy.largePotRenderID;
    }
}
